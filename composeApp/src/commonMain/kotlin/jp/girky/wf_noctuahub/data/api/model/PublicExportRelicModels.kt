package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Public Export の ExportRelicArcane_ja.json をパースするためのレスポンスデータモデル
 */
@Serializable
data class ExportRelicArcaneResponse(
  @SerialName("ExportRelicArcane") val exportRelics: List<ExportRelic>? = emptyList()
)

/**
 * Void レリックの基本情報
 */
@Serializable
data class ExportRelic(
  @SerialName("uniqueName") val uniqueName: String,
  @SerialName("name") val name: String,
  @SerialName("description") val description: String? = null,
  @SerialName("relicRewards") val relicRewards: List<RelicReward>? = emptyList()
)

/**
 * 各レリックに含まれる報酬情報
 */
@Serializable
data class RelicReward(
  @SerialName("rewardName") val rewardName: String,
  @SerialName("rarity") val rarity: String, // "COMMON", "UNCOMMON", "RARE"
  @SerialName("itemCount") val itemCount: Int = 1
)
