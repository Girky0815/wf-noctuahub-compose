package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.Serializable

/**
 * Primeシリーズ定義データ全体のトップレベル構造
 *
 * @property updatedAt ISO 8601形式のタイムスタンプ
 * @property series PrimeSeriesItemのリスト
 */
@Serializable
data class PrimeSeriesData(
  val updatedAt: String,
  val series: List<PrimeSeriesItem> = emptyList()
)

/**
 * Primeシリーズ（各Primeパック）のデータ構造
 *
 * @property id シリーズID (例: frost_prime_pack)
 * @property name シリーズ名 (例: Frost Prime)
 * @property releaseDate リリース日 (例: 2013-05-03)
 * @property items シリーズに含まれるPrimeアイテムのリスト
 */
@Serializable
data class PrimeSeriesItem(
  val id: String,
  val name: String,
  val releaseDate: String,
  val items: List<PrimeItem> = emptyList()
)

/**
 * 各Primeアイテムのデータ構造
 *
 * @property uniqueName PublicExportと同一の内部ID (例: /Lotus/Powersuits/Frost/FrostPrime)
 * @property name アイテム名 (例: Frost Prime)
 * @property category アイテムカテゴリ (例: WARFRAME, PRIMARY, SECONDARY, MELEE, SENTINEL, ARCHGUN 等)
 */
@Serializable
data class PrimeItem(
  val uniqueName: String,
  val name: String,
  val category: String
)
