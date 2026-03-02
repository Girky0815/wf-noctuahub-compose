package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MongoId(
    @SerialName("\$oid")
    val oid: String? = null
)

@Serializable
// To safely parse Long as string when needed from JSON
data class MongoNumberLong(
    @SerialName("\$numberLong")
    val numberLong: String? = null
)

@Serializable
data class MongoDate(
    @SerialName("\$date")
    val date: MongoNumberLong? = null
) {
    /**
     * Helper to safely return epoch milliseconds.
     */
    val epochMillis: Long
        get() = date?.numberLong?.toLongOrNull() ?: 0L
}

@Serializable
data class CountedItem(
    @SerialName("ItemType") val itemType: String? = null,
    @SerialName("ItemCount") val itemCount: Int = 1
)

@Serializable
data class Reward(
    @SerialName("countedItems") val countedItems: List<CountedItem>? = emptyList(),
    @SerialName("items") val items: List<String>? = emptyList(),
    @SerialName("credits") val credits: Int = 0
)
