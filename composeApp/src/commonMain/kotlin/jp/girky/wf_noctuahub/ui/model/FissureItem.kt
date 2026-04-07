package jp.girky.wf_noctuahub.ui.model

data class FissureItem(
    val id: String,
    val node: String,
    val missionType: String,
    val tier: String,
    val expiry: Long, // unix time in ms
    val isHard: Boolean,
    val isStorm: Boolean,
    val enemyFaction: String = "",
    val minLevel: Int = 0,
    val maxLevel: Int = 0,
    val tierNum: Int = 0 // for sorting: Lith=1, Meso=2... etc
)
