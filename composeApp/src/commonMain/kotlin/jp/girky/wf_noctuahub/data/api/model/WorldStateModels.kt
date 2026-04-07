package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldStateResponse(
    @SerialName("WorldSeed") val worldSeed: String? = null,
    @SerialName("Version") val version: Int? = null,
    @SerialName("Time") val time: Long? = null,
    @SerialName("Events") val events: List<WsEvent>? = emptyList(),
    @SerialName("Alerts") val alerts: List<WsAlert>? = emptyList(),
    @SerialName("Sorties") val sorties: List<WsSortie>? = emptyList(),
    @SerialName("SyndicateMissions") val syndicateMissions: List<WsSyndicateMission>? = emptyList(),
    @SerialName("ActiveMissions") val activeMissions: List<WsActiveMission>? = emptyList(),
    @SerialName("NodeOverrides") val nodeOverrides: List<WsNodeOverride>? = emptyList(),
    @SerialName("VoidStorms") val voidStorms: List<WsVoidStorm>? = emptyList(),
    @SerialName("Invasions") val invasions: List<WsInvasion>? = emptyList(),
    @SerialName("VoidTraders") val voidTraders: List<WsVoidTrader>? = emptyList(),
    @SerialName("DailyDeals") val dailyDeals: List<WsDailyDeal>? = emptyList(),
    // For Earth/Cetus clock, we might rely on specific keys or calculation based on epoch time if not directly provided
    @SerialName("ProjectPct") val projectPct: List<Double>? = emptyList() // Fomorian/Razorback progress
)

@Serializable
data class WsEvent(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Messages") val messages: List<WsMessage>? = emptyList(),
    @SerialName("Prop") val prop: String? = null,
    @SerialName("Icon") val icon: String? = null,
    @SerialName("ImageUrl") val imageUrl: String? = null,
    @SerialName("Date") val date: MongoDate? = null,
    @SerialName("EventStartDate") val eventStartDate: MongoDate? = null,
    @SerialName("EventEndDate") val eventEndDate: MongoDate? = null
)

@Serializable
data class WsMessage(
    @SerialName("LanguageCode") val languageCode: String? = null,
    @SerialName("Message") val message: String? = null
)

@Serializable
data class WsAlert(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("MissionInfo") val missionInfo: WsMissionInfo? = null
)

@Serializable
data class WsMissionInfo(
    @SerialName("missionType") val missionType: String? = null,
    @SerialName("faction") val faction: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("levelOverride") val levelOverride: String? = null,
    @SerialName("enemySpec") val enemySpec: String? = null,
    @SerialName("minEnemyLevel") val minEnemyLevel: Int = 0,
    @SerialName("maxEnemyLevel") val maxEnemyLevel: Int = 0,
    @SerialName("difficulty") val difficulty: Double = 0.0,
    @SerialName("seed") val seed: Int = 0,
    @SerialName("missionReward") val missionReward: Reward? = null
)

@Serializable
data class WsSortie(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Reward") val reward: String? = null, // e.g., "/Lotus/Types/Game/SortieRewards/SortieRewardTable"
    @SerialName("Boss") val boss: String? = null,
    @SerialName("Variants") val variants: List<WsSortieVariant>? = emptyList()
)

@Serializable
data class WsSortieVariant(
    @SerialName("missionType") val missionType: String? = null,
    @SerialName("modifierType") val modifierType: String? = null,
    @SerialName("node") val node: String? = null,
    @SerialName("tileset") val tileset: String? = null
)

@Serializable
data class WsSyndicateMission(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Tag") val tag: String? = null, // e.g., "CetusSyndicate", "SolarisSyndicate", "EntratiSyndicate", "ZarimanSyndicate"
    @SerialName("Nodes") val nodes: List<String>? = emptyList()
)

@Serializable
data class WsActiveMission(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("MissionType") val missionType: String? = null, // might not exist
    @SerialName("Modifier") val modifier: String? = null, // "VoidT1", "VoidT2" etc
    @SerialName("Region") val region: Int? = null,
    @SerialName("Seed") val seed: Int? = null,
    @SerialName("Hard") val hard: Boolean? = false
)

@Serializable
data class WsNodeOverride(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("Hide") val hide: Boolean? = null
)

@Serializable
data class WsVoidStorm(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("ActiveMissionTier") val activeMissionTier: String? = null
)

@Serializable
data class WsInvasion(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Faction") val faction: String? = null,
    @SerialName("DefenderFaction") val defenderFaction: String? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("Count") val count: Int? = null,
    @SerialName("Goal") val goal: Int? = null,
    @SerialName("LocTag") val locTag: String? = null,
    @SerialName("Completed") val completed: Boolean? = null,
    @SerialName("AttackerReward") val attackerReward: Reward? = null,
    @SerialName("DefenderReward") val defenderReward: Reward? = null
)

@Serializable
data class WsVoidTrader(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Character") val character: String? = null,
    @SerialName("Node") val node: String? = null
)

@Serializable
data class WsDailyDeal(
    @SerialName("StoreItem") val storeItem: String? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Discount") val discount: Int? = null,
    @SerialName("OriginalPrice") val originalPrice: Int? = null,
    @SerialName("SalePrice") val salePrice: Int? = null,
    @SerialName("AmountTotal") val amountTotal: Int? = null,
    @SerialName("AmountSold") val amountSold: Int? = null
)
