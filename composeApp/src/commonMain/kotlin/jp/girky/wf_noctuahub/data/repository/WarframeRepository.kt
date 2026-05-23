package jp.girky.wf_noctuahub.data.repository

import jp.girky.wf_noctuahub.data.api.WarframeApiClient
import jp.girky.wf_noctuahub.data.api.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WarframeRepository(private val apiClient: WarframeApiClient) {

  private val _worldState = MutableStateFlow<WorldStateResponse?>(null)
  val worldState: StateFlow<WorldStateResponse?> = _worldState.asStateFlow()

  // Public Export の uniqueName (e.g. /Lotus/Language/...) と日本語名のマッピング辞書
  private val localizationDict = mutableMapOf<String, String>()
  
  // リージョンの詳細情報をそのまま引き出せる辞書 (勢力やレベル帯などを得るため)
  private val regionDict = mutableMapOf<String, ExportRegion>()

  /**
   * WorldState をフェッチして内部状態を更新する
   */
  suspend fun refreshWorldState() {
    val newState = apiClient.fetchWorldState()
    _worldState.value = newState
  }

  /**
   * アプリ起動時に Public Export の各種辞書をダウンロード・解凍・パースしオンメモリに保持する
   */
  suspend fun initializeLocalization() {
    val manifest = apiClient.fetchPublicExportManifest()
    
    // Customs (一般的なアイテム名やテキスト) の取得
    val customsLine = manifest.find { it.startsWith("ExportCustoms_ja.json") }
    if (customsLine != null) {
      val customsResponse: ExportCustomsResponse = apiClient.fetchExportJson(customsLine)
      customsResponse.exportCustoms?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Regions (ノード名など)
    val regionsLine = manifest.find { it.startsWith("ExportRegions_ja.json") }
    if (regionsLine != null) {
      val regionsResponse: ExportRegionsResponse = apiClient.fetchExportJson(regionsLine)
      regionsResponse.exportRegions?.forEach { region ->
        val planet = region.systemName ?: ""
        val formatted = if (planet.isNotBlank()) "${region.name} ($planet)" else region.name
        localizationDict[region.uniqueName] = formatName(formatted)
        regionDict[region.uniqueName] = region
      }
    }

    // Warframes (フレーム名) の取得
    val warframesLine = manifest.find { it.startsWith("ExportWarframes_ja.json") }
    if (warframesLine != null) {
      val warframesResponse: ExportWarframesResponse = apiClient.fetchExportJson(warframesLine)
      warframesResponse.exportWarframes?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Weapons (武器名) の取得
    val weaponsLine = manifest.find { it.startsWith("ExportWeapons_ja.json") }
    if (weaponsLine != null) {
      val weaponsResponse: ExportWeaponsResponse = apiClient.fetchExportJson(weaponsLine)
      weaponsResponse.exportWeapons?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Resources (リソース名) の取得
    val resourcesLine = manifest.find { it.startsWith("ExportResources_ja.json") }
    if (resourcesLine != null) {
      val resourcesResponse: ExportResourcesResponse = apiClient.fetchExportJson(resourcesLine)
      resourcesResponse.exportResources?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Gear (ギア名) の取得
    val gearLine = manifest.find { it.startsWith("ExportGear_ja.json") }
    if (gearLine != null) {
      val gearResponse: ExportGearResponse = apiClient.fetchExportJson(gearLine)
      gearResponse.exportGear?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // Sentinels (センチネル・ペット名) の取得
    val sentinelsLine = manifest.find { it.startsWith("ExportSentinels_ja.json") }
    if (sentinelsLine != null) {
      val sentinelsResponse: ExportSentinelsResponse = apiClient.fetchExportJson(sentinelsLine)
      sentinelsResponse.exportSentinels?.forEach { item ->
        localizationDict[item.uniqueName] = formatName(item.name)
      }
    }

    // イベント専用ノードの手動マッピングを追加
    localizationDict["EventNode5"] = "Kronia リレー (土星)"
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
    // "/Lotus/StoreItems/" を "/Lotus/" に置換したキーでも検索する
    val cleaned = uniqueName.replace("/Lotus/StoreItems/", "/Lotus/")
    localizationDict[cleaned]?.let { return it }
    
    localizationDict[uniqueName]?.let { return it }

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
}
