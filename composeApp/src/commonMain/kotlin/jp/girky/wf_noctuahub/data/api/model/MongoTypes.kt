package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder

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
data class RewardSurrogate(
    @SerialName("countedItems") val countedItems: List<CountedItem>? = emptyList(),
    @SerialName("items") val items: List<String>? = emptyList(),
    @SerialName("credits") val credits: Int = 0
)

@Serializable(with = RewardSerializer::class)
data class Reward(
    val countedItems: List<CountedItem>? = emptyList(),
    val items: List<String>? = emptyList(),
    val credits: Int = 0
)

object RewardSerializer : kotlinx.serialization.KSerializer<Reward> {
    override val descriptor = RewardSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Reward) {
        val surrogate = RewardSurrogate(value.countedItems, value.items, value.credits)
        encoder.encodeSerializableValue(RewardSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): Reward {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Only JSON is supported")
        val element = jsonDecoder.decodeJsonElement()
        if (element is JsonArray) {
            // Empty array returned from Warframe API when no rewards
            return Reward()
        }
        val surrogate = jsonDecoder.json.decodeFromJsonElement(RewardSurrogate.serializer(), element)
        return Reward(surrogate.countedItems, surrogate.items, surrogate.credits)
    }
}
