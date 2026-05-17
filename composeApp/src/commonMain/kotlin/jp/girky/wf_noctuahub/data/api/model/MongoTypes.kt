package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonArray

@Serializable
data class MongoId(
    @SerialName("\$oid")
    val oid: String? = null
)

@Serializable
data class MongoNumberLong(
    @SerialName("\$numberLong")
    val numberLong: String? = null
)

@Serializable
data class MongoDate(
    @SerialName("\$date")
    val date: MongoNumberLong? = null
) {
    val epochMillis: Long
        get() = date?.numberLong?.toLongOrNull() ?: 0L
}

@Serializable
data class WsCountedItem(
    @SerialName("ItemType") val itemType: String? = null,
    @SerialName("ItemCount") val itemCount: Int? = 0
)

@Serializable
data class WsRewardSurrogate(
    @SerialName("countedItems") val countedItems: List<WsCountedItem>? = emptyList(),
    @SerialName("items") val items: List<String>? = emptyList(),
    @SerialName("credits") val credits: Int? = 0,
    @SerialName("asString") val asString: String? = null
)

@Serializable(with = WsRewardSerializer::class)
data class WsReward(
    val countedItems: List<WsCountedItem>? = emptyList(),
    val items: List<String>? = emptyList(),
    val credits: Int? = 0,
    val asString: String? = null
)

object WsRewardSerializer : kotlinx.serialization.KSerializer<WsReward> {
    override val descriptor = WsRewardSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: WsReward) {
        val surrogate = WsRewardSurrogate(value.countedItems, value.items, value.credits, value.asString)
        encoder.encodeSerializableValue(WsRewardSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): WsReward {
        val jsonDecoder = decoder as? JsonDecoder
        val element = jsonDecoder?.decodeJsonElement()
        if (element == null || element is JsonArray) {
            return WsReward()
        }
        val surrogate = jsonDecoder.json.decodeFromJsonElement(WsRewardSurrogate.serializer(), element)
        return WsReward(surrogate.countedItems, surrogate.items, surrogate.credits, surrogate.asString)
    }
}
