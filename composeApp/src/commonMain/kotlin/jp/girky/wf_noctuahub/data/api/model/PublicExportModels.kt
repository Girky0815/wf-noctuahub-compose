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
    @SerialName("systemName") val systemName: String? = null
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
