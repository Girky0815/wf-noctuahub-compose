package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportCustomsResponse(
  @SerialName("ExportCustoms") val exportCustoms: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportResourcesResponse(
  @SerialName("ExportResources") val exportResources: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportRegion(
  @SerialName("uniqueName") val uniqueName: String,
  @SerialName("name") val name: String,
  @SerialName("systemName") val systemName: String? = null,
  @SerialName("missionIndex") val missionIndex: Int? = null,
  @SerialName("factionIndex") val factionIndex: Int? = null,
  @SerialName("minEnemyLevel") val minEnemyLevel: Int? = null,
  @SerialName("maxEnemyLevel") val maxEnemyLevel: Int? = null
)

@Serializable
data class ExportRegionsResponse(
  @SerialName("ExportRegions") val exportRegions: List<ExportRegion>? = emptyList()
)

// 多くの Export 系 JSON は以下のような似た構造を持つため使いまわせる
@Serializable
data class ExportItem(
  @SerialName("uniqueName") val uniqueName: String,
  @SerialName("name") val name: String,
  @SerialName("description") val description: String? = null
)

@Serializable
data class ExportWarframesResponse(
  @SerialName("ExportWarframes") val exportWarframes: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportWeaponsResponse(
  @SerialName("ExportWeapons") val exportWeapons: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportGearResponse(
  @SerialName("ExportGear") val exportGear: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportSentinelsResponse(
  @SerialName("ExportSentinels") val exportSentinels: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportUpgradesResponse(
  @SerialName("ExportUpgrades") val exportUpgrades: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportFlavourResponse(
  @SerialName("ExportFlavour") val exportFlavour: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportKeysResponse(
  @SerialName("ExportKeys") val exportKeys: List<ExportItem>? = emptyList()
)

@Serializable
data class ExportRelicArcaneResponse(
  @SerialName("ExportRelicArcane") val exportRelicArcane: List<ExportItem>? = emptyList()
)


