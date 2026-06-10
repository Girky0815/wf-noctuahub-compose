package jp.girky.wf_noctuahub.utils

expect object CacheUtils {
  /**
   * キャッシュファイルを保存する
   */
  fun saveCacheFile(fileName: String, content: String)

  /**
   * キャッシュファイルを読み込む。存在しない場合はnullを返す
   */
  fun loadCacheFile(fileName: String): String?

  /**
   * キャッシュファイルを削除する
   */
  fun deleteCacheFile(fileName: String): Boolean
}
