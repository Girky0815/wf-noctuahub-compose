package jp.girky.wf_noctuahub.utils

/**
 * Warframe Wiki 日本語版 (wikiwiki.jp/warframe) の各ページへの URL を生成するユーティリティクラス
 */
object WikiUtils {

  private val incarnonNameMap = mapOf(
    "CeramicDagger" to "Ceramic Dagger",
    "DualToxocyst" to "Dual Toxocyst",
    "AckAndBrunt" to "Ack & Brunt",
    "DualRaza" to "Dual Raza",
    "NamiSolo" to "Nami Solo",
    "DualIchor" to "Dual Ichor",
    "ReaperPrime" to "Reaper Prime"
  )

  /**
   * インカーノン武器の内部名（API値）を正規化された英語正式名（スペースあり）に変換する
   */
  fun getNormalizedIncarnonName(rawChoice: String): String {
    return incarnonNameMap[rawChoice] ?: rawChoice
  }

  /**
   * Prime Resurgence の Warframe 用 Wiki URL を生成する
   * @param localizedName 表示名（例: "Mesa Prime" または "Mesa Prime Blueprint"）
   * 規則: 末尾の "Prime" (および設計図等がある場合はそれも含む) を取り除いた本体の名前を大文字にし、末尾に "#prime" を付加する
   */
  fun getResurgenceWarframeUrl(localizedName: String): String {
    // " Prime" および設計図などの不要部分を取り除く（大文字小文字を無視）
    var baseName = localizedName
      .replace("(?i)\\s+Blueprint$".toRegex(), "")
      .replace("(?i)\\s+Prime$".toRegex(), "")
      .trim()
    
    val upperName = baseName.uppercase()
    return "https://wikiwiki.jp/warframe/$upperName#prime"
  }

  /**
   * 武器用 Wiki URL を生成する
   * @param localizedName 表示名（例: "Prisma Grinlok"）
   * 規則: すべて大文字に変換し、空白は "%20"、「&」は全角「＆」(%EF%BC%86) に変換する
   */
  fun getWeaponUrl(localizedName: String): String {
    // 設計図などがある場合は取り除く
    val cleanName = localizedName
      .replace("(?i)\\s+Blueprint$".toRegex(), "")
      .replace("(?i)\\s+設計図$".toRegex(), "")
      .trim()
      
    val encoded = cleanName.uppercase()
      .replace("&", "＆")
      .replace(" ", "%20")
      .replace("＆", "%EF%BC%86")
    return "https://wikiwiki.jp/warframe/$encoded"
  }

  /**
   * Prime Resurgence の武器用 Wiki URL を生成する
   * @param localizedName 表示名（例: "Cobra & Crane Prime"）
   */
  fun getResurgenceWeaponUrl(localizedName: String): String {
    return getWeaponUrl(localizedName)
  }

  /**
   * 通常サーキットの Warframe 用 Wiki URL を生成する
   * @param rawChoice APIから返される名前（例: "Saryn"）
   */
  fun getCircuitWarframeUrl(rawChoice: String): String {
    val upperName = rawChoice.trim().uppercase()
    return "https://wikiwiki.jp/warframe/$upperName"
  }

  /**
   * 鋼サーキットのインカーノン武器用 Wiki URL を生成する
   * @param rawChoice APIから返される名前（例: "Dual Toxocyst"）
   */
  fun getCircuitIncarnonUrl(rawChoice: String): String {
    val normalized = getNormalizedIncarnonName(rawChoice)
    val encoded = normalized.uppercase()
      .replace("&", "＆")
      .replace(" ", "%20")
      .replace("＆", "%EF%BC%86")
    return "https://wikiwiki.jp/warframe/Incarnon/$encoded"
  }
}
