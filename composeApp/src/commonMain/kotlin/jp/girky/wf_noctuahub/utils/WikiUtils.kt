package jp.girky.wf_noctuahub.utils

/**
 * Warframe Wiki 日本語版 (wikiwiki.jp/warframe) の各ページへの URL を生成するユーティリティクラス
 */
object WikiUtils {

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
   * Prime Resurgence の武器用 Wiki URL を生成する
   * @param localizedName 表示名（例: "Cobra & Crane Prime"）
   * 規則: すべて大文字に変換し、空白は "%20"、「&」は全角「＆」(%EF%BC%86) に変換する
   */
  fun getResurgenceWeaponUrl(localizedName: String): String {
    // 設計図などがある場合は取り除く
    val cleanName = localizedName
      .replace("(?i)\\s+Blueprint$".toRegex(), "")
      .trim()
      
    val encoded = cleanName.uppercase()
      .replace("&", "＆")
      .replace(" ", "%20")
      .replace("＆", "%EF%BC%86")
    return "https://wikiwiki.jp/warframe/$encoded"
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
    val encoded = rawChoice.uppercase()
      .replace("&", "＆")
      .replace(" ", "%20")
      .replace("＆", "%EF%BC%86")
    return "https://wikiwiki.jp/warframe/Incarnon/$encoded"
  }
}
