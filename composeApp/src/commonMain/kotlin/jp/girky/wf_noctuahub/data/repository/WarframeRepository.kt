package jp.girky.wf_noctuahub.data.repository

import jp.girky.wf_noctuahub.data.api.WarframeApiClient
import jp.girky.wf_noctuahub.data.api.model.*
import jp.girky.wf_noctuahub.utils.CacheUtils
import jp.girky.wf_noctuahub.platform.getAppUpdater
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonPrimitive

class WarframeRepository(
  private val apiClient: WarframeApiClient,
  private val appSettings: AppSettings
) {

  private val _worldState = MutableStateFlow<WorldStateResponse?>(null)
  val worldState: StateFlow<WorldStateResponse?> = _worldState.asStateFlow()

  // Public Export の uniqueName (e.g. /Lotus/Language/...) と日本語名のマッピング辞書
  private val localizationDict = mutableMapOf<String, String>()
  
  // リージョンの詳細情報をそのまま引き出せる辞書 (勢力やレベル帯などを得るため)
  private val regionDict = mutableMapOf<String, ExportRegion>()

  // MODの uniqueName と説明文（効果内容）のマッピング辞書
  private val modDescriptionDict = mutableMapOf<String, String>()

  /**
   * WorldState をフェッチして内部状態を更新する
   */
  suspend fun refreshWorldState() {
    val newState = apiClient.fetchWorldState()
    _worldState.value = newState
  }

  /**
   * 指定されたマニフェストファイル行（例: ExportCustoms_ja.json!00_...）から
   * データを取得する。キャッシュが存在し、更新不要であればキャッシュから読み込み、
   * そうでなければダウンロードしてキャッシュに書き込む。
   */
  private suspend inline fun <reified T> getLocalizedData(
    manifestLine: String,
    needsDownload: Boolean
  ): T? {
    val fileName = manifestLine.substringBefore("!") // 例: "ExportCustoms_ja.json"
    
    var jsonString: String? = null
    
    // 更新不要であればキャッシュの読み込みを試みる
    if (!needsDownload) {
      jsonString = CacheUtils.loadCacheFile(fileName)
      if (jsonString != null) {
        println("Load localized data from cache: $fileName")
      }
    }
    
    // キャッシュがない、または更新が必要な場合はダウンロードする
    if (jsonString == null) {
      println("Download localized data: $manifestLine")
      try {
        val cleanManifestLine = manifestLine.trim()
        val url = "http://content.warframe.com/PublicExport/Manifest/$cleanManifestLine"
        val response = apiClient.client.get(url)
        jsonString = response.readBytes().decodeToString()
        
        // キャッシュへ保存
        CacheUtils.saveCacheFile(fileName, jsonString)
      } catch (e: Exception) {
        e.printStackTrace()
        // エラー時は、もしあれば古いキャッシュからのロードを試みる
        jsonString = CacheUtils.loadCacheFile(fileName)
      }
    }
    
    if (jsonString == null) return null
    
    return try {
      val jsonParser = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true
        isLenient = true
      }
      jsonParser.decodeFromString<T>(jsonString)
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  /**
   * アプリ起動時に Public Export の各種辞書をダウンロード・解凍・パースしオンメモリに保持する
   */
  suspend fun initializeLocalization() {
    // すでに辞書がロードされている場合は処理をスキップ（Pull-to-refresh等での無駄な通信を防止）
    if (localizationDict.isNotEmpty()) {
      println("initializeLocalization: Already loaded. Skipping.")
      return
    }

    var manifest: List<String> = emptyList()
    var needsDownload = true
    var currentManifestText = ""
    
    try {
      manifest = apiClient.fetchPublicExportManifest()
      currentManifestText = manifest.joinToString("\n")
    } catch (e: Exception) {
      e.printStackTrace()
      // マニフェスト取得失敗時はキャッシュからのロードのみを試みるため、needsDownloadをfalseにする
      needsDownload = false
    }

    if (manifest.isNotEmpty()) {
      val lastManifestText = appSettings.getLastManifest()
      val lastAppVersion = appSettings.getLastAppVersion()
      val currentAppVersion = getAppUpdater().getAppVersionName()

      // マニフェスト内容、アプリバージョン、ローカルのキャッシュファイル存在をチェック
      val files = listOf(
        "ExportCustoms_ja.json",
        "ExportRegions_ja.json",
        "ExportWarframes_ja.json",
        "ExportWeapons_ja.json",
        "ExportResources_ja.json",
        "ExportGear_ja.json",
        "ExportSentinels_ja.json",
        "ExportUpgrades_ja.json",
        "ExportFlavour_ja.json",
        "ExportKeys_ja.json",
        "ExportRelicArcane_ja.json"
      )
      val allCachesExist = files.all { CacheUtils.loadCacheFile(it) != null }

      needsDownload = (currentManifestText != lastManifestText) || 
                      (currentAppVersion != lastAppVersion) || 
                      !allCachesExist
    }

    // Customs (一般的なアイテム名やテキスト) の取得
    val customsLine = manifest.find { it.startsWith("ExportCustoms_ja.json") }
      ?: if (!needsDownload) "ExportCustoms_ja.json" else null
    if (customsLine != null) {
      val customsResponse: ExportCustomsResponse? = getLocalizedData(customsLine, needsDownload)
      customsResponse?.exportCustoms?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Regions (ノード名など)
    val regionsLine = manifest.find { it.startsWith("ExportRegions_ja.json") }
      ?: if (!needsDownload) "ExportRegions_ja.json" else null
    if (regionsLine != null) {
      val regionsResponse: ExportRegionsResponse? = getLocalizedData(regionsLine, needsDownload)
      regionsResponse?.exportRegions?.forEach { region ->
        val planet = region.systemName ?: ""
        val formatted = if (planet.isNotBlank()) "${region.name} ($planet)" else region.name
        localizationDict[region.uniqueName] = formatName(formatted)
        regionDict[region.uniqueName] = region
      }
    }

    // Warframes (フレーム名) の取得
    val warframesLine = manifest.find { it.startsWith("ExportWarframes_ja.json") }
      ?: if (!needsDownload) "ExportWarframes_ja.json" else null
    if (warframesLine != null) {
      val warframesResponse: ExportWarframesResponse? = getLocalizedData(warframesLine, needsDownload)
      warframesResponse?.exportWarframes?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Weapons (武器名) の取得
    val weaponsLine = manifest.find { it.startsWith("ExportWeapons_ja.json") }
      ?: if (!needsDownload) "ExportWeapons_ja.json" else null
    if (weaponsLine != null) {
      val weaponsResponse: ExportWeaponsResponse? = getLocalizedData(weaponsLine, needsDownload)
      weaponsResponse?.exportWeapons?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Resources (リソース名) の取得
    val resourcesLine = manifest.find { it.startsWith("ExportResources_ja.json") }
      ?: if (!needsDownload) "ExportResources_ja.json" else null
    if (resourcesLine != null) {
      val resourcesResponse: ExportResourcesResponse? = getLocalizedData(resourcesLine, needsDownload)
      resourcesResponse?.exportResources?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Gear (ギア名) の取得
    val gearLine = manifest.find { it.startsWith("ExportGear_ja.json") }
      ?: if (!needsDownload) "ExportGear_ja.json" else null
    if (gearLine != null) {
      val gearResponse: ExportGearResponse? = getLocalizedData(gearLine, needsDownload)
      gearResponse?.exportGear?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Sentinels (センチネル・ペット名) の取得
    val sentinelsLine = manifest.find { it.startsWith("ExportSentinels_ja.json") }
      ?: if (!needsDownload) "ExportSentinels_ja.json" else null
    if (sentinelsLine != null) {
      val sentinelsResponse: ExportSentinelsResponse? = getLocalizedData(sentinelsLine, needsDownload)
      sentinelsResponse?.exportSentinels?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Upgrades (MOD) の取得
    val upgradesLine = manifest.find { it.startsWith("ExportUpgrades_ja.json") }
      ?: if (!needsDownload) "ExportUpgrades_ja.json" else null
    if (upgradesLine != null) {
      var upgradesResponse: ExportUpgradesResponse? = getLocalizedData(upgradesLine, needsDownload)
      if (upgradesResponse?.exportUpgrades.isNullOrEmpty() && !needsDownload && manifest.isNotEmpty()) {
        val forceLine = manifest.find { it.startsWith("ExportUpgrades_ja.json") }
        if (forceLine != null) {
          upgradesResponse = getLocalizedData(forceLine, true)
        }
      }
      upgradesResponse?.exportUpgrades?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
        var descStr: String? = null
        item.description?.let { descElement ->
          try {
            val rawStr = when (descElement) {
              is JsonArray -> {
                if (descElement.isNotEmpty()) {
                  descElement[0].jsonPrimitive.content
                } else {
                  ""
                }
              }
              else -> descElement.jsonPrimitive.content
            }
            if (rawStr.isNotBlank()) {
              descStr = rawStr
            }
          } catch (e: Exception) {
            val rawStr = descElement.toString().trim().removeSurrounding("\"")
            if (rawStr.isNotBlank()) {
              descStr = rawStr
            }
          }
        }

        if (descStr.isNullOrBlank()) {
          val lastStat = item.levelStats?.lastOrNull()
          val firstStatText = lastStat?.stats?.firstOrNull()
          if (!firstStatText.isNullOrBlank()) {
            descStr = firstStatText
          }
        }

        descStr?.let { raw ->
          val cleaned = raw.replace(Regex("<[^>]*>"), "")
          if (cleaned.isNotBlank()) {
            modDescriptionDict[item.uniqueName] = formatName(cleaned)
          }
        }
      }
    }

    // Flavour (スキン、装飾品など) の取得
    val flavourLine = manifest.find { it.startsWith("ExportFlavour_ja.json") }
      ?: if (!needsDownload) "ExportFlavour_ja.json" else null
    if (flavourLine != null) {
      var flavourResponse: ExportFlavourResponse? = getLocalizedData(flavourLine, needsDownload)
      if (flavourResponse?.exportFlavour.isNullOrEmpty() && !needsDownload && manifest.isNotEmpty()) {
        val forceLine = manifest.find { it.startsWith("ExportFlavour_ja.json") }
        if (forceLine != null) {
          flavourResponse = getLocalizedData(forceLine, true)
        }
      }
      flavourResponse?.exportFlavour?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Keys (クエスト・キーなど) の取得
    val keysLine = manifest.find { it.startsWith("ExportKeys_ja.json") }
      ?: if (!needsDownload) "ExportKeys_ja.json" else null
    if (keysLine != null) {
      var keysResponse: ExportKeysResponse? = getLocalizedData(keysLine, needsDownload)
      if (keysResponse?.exportKeys.isNullOrEmpty() && !needsDownload && manifest.isNotEmpty()) {
        val forceLine = manifest.find { it.startsWith("ExportKeys_ja.json") }
        if (forceLine != null) {
          keysResponse = getLocalizedData(forceLine, true)
        }
      }
      keysResponse?.exportKeys?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // RelicArcane (レリック・アルケイン) の取得
    val relicArcaneLine = manifest.find { it.startsWith("ExportRelicArcane_ja.json") }
      ?: if (!needsDownload) "ExportRelicArcane_ja.json" else null
    if (relicArcaneLine != null) {
      var relicArcaneResponse: ExportRelicArcaneResponse? = getLocalizedData(relicArcaneLine, needsDownload)
      if (relicArcaneResponse?.exportRelicArcane.isNullOrEmpty() && !needsDownload && manifest.isNotEmpty()) {
        val forceLine = manifest.find { it.startsWith("ExportRelicArcane_ja.json") }
        if (forceLine != null) {
          relicArcaneResponse = getLocalizedData(forceLine, true)
        }
      }
      relicArcaneResponse?.exportRelicArcane?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // イベント専用ノードの手動マッピングを追加
    localizationDict["EventNode5"] = "Kronia リレー (土星)"

    // バロ吉関連アイテムの手動フォールバックマッピングを追加
    localizationDict["/Lotus/Storeitems/Types/BoosterPacks/BaroTreasureBox"] = "Void サープラス"
    localizationDict["/Lotus/StoreItems/Types/BoosterPacks/BaroTreasureBox"] = "Void サープラス"
    localizationDict["/Lotus/Storeitems/Types/Keys/MummyQuestKeyBlueprint"] = "Inaros の砂の設計図"
    localizationDict["/Lotus/StoreItems/Types/Keys/MummyQuestKeyBlueprint"] = "Inaros の砂の設計図"
    localizationDict["/Lotus/Storeitems/Types/Storeitems/AvatarImages/Factions/GlyphFactionGrineer"] = "グリフ: グリニア勢力"
    localizationDict["/Lotus/StoreItems/Types/StoreItems/AvatarImages/Factions/GlyphFactionGrineer"] = "グリフ: グリニア勢力"
    localizationDict["/Lotus/Storeitems/Types/Storeitems/AvatarImages/AvatarImageBaroIcon"] = "プロファイルアイコン: Baro Ki'Teer"
    localizationDict["/Lotus/StoreItems/Types/StoreItems/AvatarImages/AvatarImageBaroIcon"] = "プロファイルアイコン: Baro Ki'Teer"

    // 正常にWebからロードできた場合はマニフェストとアプリバージョン情報を更新保存
    if (manifest.isNotEmpty() && needsDownload) {
      appSettings.setLastManifest(currentManifestText)
      val currentAppVersion = getAppUpdater().getAppVersionName()
      appSettings.setLastAppVersion(currentAppVersion)
      println("Saved new manifest & app version ($currentAppVersion)")
    }
  }

  /**
   * 全角記号を半角記号に正規化する
   */
  private fun formatName(name: String): String {
    return name
      .replace("％", "%")
      .replace("：", ": ")
  }

  /**
   * アイテムの内部名（uniqueName）を日本語に翻訳する。
   * 表記ゆれを正規化したキーでもマップを検索する
   */
  fun localize(uniqueName: String): String {
    // 1. 完全一致で検索
    localizationDict[uniqueName]?.let { return it }

    // 2. "/Lotus/StoreItems/" (大文字小文字無視) を "/Lotus/" に置換したキーで検索
    val cleaned = uniqueName.replace("/Lotus/StoreItems/", "/Lotus/", ignoreCase = true)
    localizationDict[cleaned]?.let { return it }

    // 3. 末尾のセグメントで末尾一致検索 (例: "/ElectEventPistolMod")
    val lastSegment = uniqueName.substringAfterLast("/")
    if (lastSegment.isNotBlank()) {
      val suffix = "/$lastSegment"
      val match = localizationDict.entries.find { (key, _) ->
        key.endsWith(suffix, ignoreCase = true)
      }
      if (match != null) {
        return match.value
      }
    }

    // もし uniqueName が /Lotus/ で始まらない rawName（例: "CeramicDagger", "Zylok", "Saryn", "Mirage"）の場合
    if (!uniqueName.startsWith("/Lotus/")) {
      val suffix = "/$uniqueName"
      val cleanRaw = uniqueName.replace(" ", "")
      val cleanSuffix = "/$cleanRaw"

      // 優先順位 1: キーの値 (value) が rawName と大文字小文字無視で完全一致し、
      // かつキーが /Lotus/Powersuits/ で始まるものを探す (例: "Mirage" -> "/Lotus/Powersuits/Harlequin/Harlequin")
      val valuePowersuitsMatch = localizationDict.entries.find { (key, value) ->
        key.startsWith("/Lotus/Powersuits/", ignoreCase = true) && value.equals(uniqueName, ignoreCase = true)
      }
      if (valuePowersuitsMatch != null) {
        return valuePowersuitsMatch.value
      }

      // 優先順位 2: キーの値 (value) が rawName と大文字小文字無視で完全一致し、
      // かつキーが /Lotus/Weapons/ で始まるものを探す
      val valueWeaponsMatch = localizationDict.entries.find { (key, value) ->
        key.startsWith("/Lotus/Weapons/", ignoreCase = true) && value.equals(uniqueName, ignoreCase = true)
      }
      if (valueWeaponsMatch != null) {
        return valueWeaponsMatch.value
      }

      // 優先順位 3: Warframe（フレーム）のパスでの末尾一致（例: /Lotus/Powersuits/...）
      val warframeMatch = localizationDict.entries.find { (key, _) ->
        key.startsWith("/Lotus/Powersuits/", ignoreCase = true) && 
        (key.endsWith(suffix, ignoreCase = true) || key.endsWith(cleanSuffix, ignoreCase = true))
      }
      if (warframeMatch != null) {
        return warframeMatch.value
      }

      // 優先順位 4: Weapon（武器）のパスでの末尾一致（例: /Lotus/Weapons/...）
      val weaponMatch = localizationDict.entries.find { (key, _) ->
        key.startsWith("/Lotus/Weapons/", ignoreCase = true) && 
        (key.endsWith(suffix, ignoreCase = true) || key.endsWith(cleanSuffix, ignoreCase = true))
      }
      if (weaponMatch != null) {
        return weaponMatch.value
      }

      // 優先順位 5: 一般的な末尾一致
      val suffixMatch = localizationDict.entries.find { (key, _) ->
        key.endsWith(suffix, ignoreCase = true)
      }
      if (suffixMatch != null) {
        return suffixMatch.value
      }

      val cleanSuffixMatch = localizationDict.entries.find { (key, _) ->
        key.endsWith(cleanSuffix, ignoreCase = true)
      }
      if (cleanSuffixMatch != null) {
        return cleanSuffixMatch.value
      }
    }
    
    return uniqueName
  }

  /**
   * ノード（Region）の詳細な設定情報を取得する
   */
  fun getRegionInfo(uniqueName: String): ExportRegion? {
    return regionDict[uniqueName]
  }

  /**
   * MODの説明文（効果）を取得する
   */
  fun getModDescription(uniqueName: String): String? {
    val cleaned = uniqueName.replace("/Lotus/StoreItems/", "/Lotus/", ignoreCase = true)
    modDescriptionDict[cleaned]?.let { return it }

    modDescriptionDict[uniqueName]?.let { return it }

    // 末尾一致フォールバック
    val lastSegment = uniqueName.substringAfterLast("/")
    if (lastSegment.isNotBlank()) {
      val suffix = "/$lastSegment"
      val match = modDescriptionDict.entries.find { (key, _) ->
        key.endsWith(suffix, ignoreCase = true)
      }
      if (match != null) {
        return match.value
      }
    }
    return null
  }
}
