package jp.girky.wf_noctuahub.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorldStateResponse(
    @SerialName("WorldSeed") val worldSeed: String? = null,
    @SerialName("Version") val version: Int? = null,
    @SerialName("Time") val time: Long? = null,
    @SerialName("Events") val events: List<WsEvent>? = emptyList(),
    @SerialName("Goals") val goals: List<WsGoal>? = emptyList(),
    @SerialName("Alerts") val alerts: List<WsAlert>? = emptyList(),
    @SerialName("Sorties") val sorties: List<WsSortie>? = emptyList(),
    @SerialName("SyndicateMissions") val syndicateMissions: List<WsSyndicateMission>? = emptyList(),
    @SerialName("ActiveMissions") val activeMissions: List<WsActiveMission>? = emptyList(),
    @SerialName("NodeOverrides") val nodeOverrides: List<WsNodeOverride>? = emptyList(),
    @SerialName("VoidStorms") val voidStorms: List<WsVoidStorm>? = emptyList(),
    @SerialName("Invasions") val invasions: List<WsInvasion>? = emptyList(),
    @SerialName("VoidTraders") val voidTraders: List<WsVoidTrader>? = emptyList(),
    @SerialName("PrimeVaultTraders") val primeVaultTraders: List<WsPrimeVaultTrader>? = emptyList(),
    @SerialName("DailyDeals") val dailyDeals: List<WsDailyDeal>? = emptyList(),
    @SerialName("EndlessXpChoices") val endlessXpChoices: List<WsEndlessXpChoice>? = emptyList(),
    @SerialName("ProjectPct") val projectPct: List<Double>? = emptyList(),
    @SerialName("LiteSorties") val liteSorties: List<WsSortie>? = emptyList(),
    @SerialName("SeasonInfo") val seasonInfo: WsSeasonInfo? = null,
    @SerialName("KnownCalendarSeasons") val calendarSeasons: List<WsCalendarSeason>? = emptyList(),
    @SerialName("Descents") val descents: List<WsDescent>? = emptyList(),
    @SerialName("Conquests") val conquests: List<WsConquest>? = emptyList()
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
data class WsGoal(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Tag") val tag: String? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("Desc") val desc: String? = null,
    @SerialName("ToolTip") val toolTip: String? = null,
    @SerialName("Count") val count: Int? = 0,
    @SerialName("Goal") val goal: Int? = 0,
    @SerialName("HealthPct") val healthPct: Double? = 1.0,
    @SerialName("Reward") val reward: WsReward? = null
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
    @SerialName("missionReward") val missionReward: WsReward? = null
)

@Serializable
data class WsSortie(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Reward") val reward: String? = null,
    @SerialName("Boss") val boss: String? = null,
    @SerialName("Faction") val faction: String? = null,
    @SerialName("Variants") val variants: List<WsSortieVariant>? = emptyList(),
    @SerialName("Missions") val missions: List<WsSortieVariant>? = emptyList()
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
    @SerialName("Tag") val tag: String? = null,
    @SerialName("Nodes") val nodes: List<String>? = emptyList()
)

@Serializable
data class WsActiveMission(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("MissionType") val missionType: String? = null,
    @SerialName("Modifier") val modifier: String? = null,
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
    @SerialName("AttackerReward") val attackerReward: WsReward? = null,
    @SerialName("DefenderReward") val defenderReward: WsReward? = null
)

@Serializable
data class WsVoidTrader(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Character") val character: String? = null,
    @SerialName("Node") val node: String? = null,
    @SerialName("Manifest") val manifest: List<WsVoidTraderItem>? = emptyList()
)

@Serializable
data class WsVoidTraderItem(
    @SerialName("ItemType") val itemType: String? = null,
    @SerialName("PrimePrice") val primePrice: Int? = null,
    @SerialName("RegularPrice") val regularPrice: Int? = null
)

@Serializable
data class WsPrimeVaultTrader(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Manifest") val manifest: List<WsVoidTraderItem>? = emptyList()
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

@Serializable
data class WsEndlessXpChoice(
    @SerialName("Category") val circuitCategory: String? = null, // "EXC_NORMAL", "EXC_HARD"
    @SerialName("Choices") val choices: List<String>? = emptyList()
)

@Serializable
data class WsSeasonInfo(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Season") val season: Int? = null,
    @SerialName("ActiveChallenges") val activeChallenges: List<WsNightwaveChallenge>? = emptyList()
)

@Serializable
data class WsNightwaveChallenge(
    @SerialName("_id") val id: MongoId? = null,
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Challenge") val challenge: String? = null,
    @SerialName("Daily") val daily: Boolean? = false
)

@Serializable
data class WsCalendarSeason(
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Season") val season: String? = null, // "CST_SPRING" etc
    @SerialName("Days") val days: List<WsCalendarDay>? = emptyList()
)

@Serializable
data class WsCalendarDay(
    @SerialName("day") val day: Int? = null,
    @SerialName("events") val events: List<WsCalendarEvent>? = emptyList()
)

@Serializable
data class WsCalendarEvent(
    @SerialName("type") val type: String? = null, // "CET_CHALLENGE", "CET_REWARD", "CET_UPGRADE"
    @SerialName("challenge") val challenge: String? = null,
    @SerialName("reward") val reward: String? = null,
    @SerialName("upgrade") val upgrade: String? = null
)

@Serializable
data class WsDescent(
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("RandSeed") val randSeed: Long? = null,
    @SerialName("Challenges") val challenges: List<WsDescentChallenge>? = emptyList()
)

@Serializable
data class WsDescentChallenge(
    @SerialName("Index") val index: Int? = null,
    @SerialName("Type") val type: String? = null,
    @SerialName("Challenge") val challenge: String? = null,
    @SerialName("Level") val level: String? = null,
    @SerialName("Specs") val specs: List<String>? = emptyList(),
    @SerialName("Auras") val auras: List<String>? = emptyList()
)

@Serializable
data class WsConquest(
    @SerialName("Activation") val activation: MongoDate? = null,
    @SerialName("Expiry") val expiry: MongoDate? = null,
    @SerialName("Type") val type: String? = null,
    @SerialName("Missions") val missions: List<WsConquestMission>? = emptyList(),
    @SerialName("Variables") val variables: List<String>? = emptyList(),
    @SerialName("RandomSeed") val randomSeed: Long? = null
)

@Serializable
data class WsConquestMission(
    @SerialName("faction") val faction: String? = null,
    @SerialName("missionType") val missionType: String? = null,
    @SerialName("difficulties") val difficulties: List<WsConquestDifficulty>? = emptyList()
)

@Serializable
data class WsConquestDifficulty(
    @SerialName("type") val type: String? = null,
    @SerialName("deviation") val deviation: String? = null,
    @SerialName("risks") val risks: List<String>? = emptyList()
)

