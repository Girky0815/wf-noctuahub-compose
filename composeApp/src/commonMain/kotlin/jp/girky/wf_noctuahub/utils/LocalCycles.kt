package jp.girky.wf_noctuahub.utils

import kotlinx.datetime.Instant

data class CycleState(
    val id: String,
    val activation: Instant,
    val expiry: Instant,
    val isDay: Boolean,
    val state: String,
    val shortString: String
)

object LocalCycles {
    // 既知の Epoch (React版の実装を使用。単位はミリ秒)
    private const val CETUS_EPOCH = 1767686674000L
    private const val CETUS_CYCLE_MS = 150 * 60 * 1000L // 150 minutes
    
    private const val VALLIS_EPOCH = 1767688801000L
    private const val VALLIS_CYCLE_MS = 1600 * 1000L // 26m 40s
    
    private const val CAMBION_EPOCH = CETUS_EPOCH
    private const val CAMBION_CYCLE_MS = 150 * 60 * 1000L
    
    private const val EARTH_EPOCH = 1516200600000L
    private const val EARTH_CYCLE_MS = 8 * 60 * 60 * 1000L // 8 hours

    /**
     * Cetus: Day 100m -> Night 50m
     */
    fun getCetusCycle(nowMs: Long): CycleState {
        val elapsed = nowMs - CETUS_EPOCH
        val cycleTime = elapsed % CETUS_CYCLE_MS
        val dayDuration = 100 * 60 * 1000L
        
        val isDay = cycleTime < dayDuration
        val timeLeft = if (isDay) (dayDuration - cycleTime) else (CETUS_CYCLE_MS - cycleTime)
        
        val expiryTime = nowMs + timeLeft
        val activationTime = expiryTime - if (isDay) dayDuration else (CETUS_CYCLE_MS - dayDuration)
        
        return CycleState(
            id = "cetus-local-$expiryTime",
            activation = Instant.fromEpochMilliseconds(activationTime),
            expiry = Instant.fromEpochMilliseconds(expiryTime),
            isDay = isDay,
            state = if (isDay) "day" else "night",
            shortString = if (isDay) "Day" else "Night"
        )
    }

    /**
     * Orb Vallis: Warm 6m 40s -> Cold 20m
     */
    fun getVallisCycle(nowMs: Long): CycleState {
        val elapsed = nowMs - VALLIS_EPOCH
        val cycleTime = elapsed % VALLIS_CYCLE_MS
        val warmDuration = 400 * 1000L
        
        val isWarm = cycleTime < warmDuration
        val timeLeft = if (isWarm) (warmDuration - cycleTime) else (VALLIS_CYCLE_MS - cycleTime)
        
        val expiryTime = nowMs + timeLeft
        val activationTime = expiryTime - if (isWarm) warmDuration else (VALLIS_CYCLE_MS - warmDuration)
        
        return CycleState(
            id = "vallis-local-$expiryTime",
            activation = Instant.fromEpochMilliseconds(activationTime),
            expiry = Instant.fromEpochMilliseconds(expiryTime),
            isDay = false,
            state = if (isWarm) "warm" else "cold",
            shortString = if (isWarm) "Warm" else "Cold"
        )
    }

    /**
     * Cambion Drift: Fass 100m -> Vome 50m
     */
    fun getCambionCycle(nowMs: Long): CycleState {
        val elapsed = nowMs - CAMBION_EPOCH
        val cycleTime = elapsed % CAMBION_CYCLE_MS
        val fassDuration = 100 * 60 * 1000L
        
        val isFass = cycleTime < fassDuration
        val timeLeft = if (isFass) (fassDuration - cycleTime) else (CAMBION_CYCLE_MS - cycleTime)
        
        val expiryTime = nowMs + timeLeft
        val activationTime = expiryTime - if (isFass) fassDuration else (CAMBION_CYCLE_MS - fassDuration)
        
        return CycleState(
            id = "cambion-local-$expiryTime",
            activation = Instant.fromEpochMilliseconds(activationTime),
            expiry = Instant.fromEpochMilliseconds(expiryTime),
            isDay = isFass,
            state = if (isFass) "fass" else "vome",
            shortString = if (isFass) "Fass" else "Vome"
        )
    }

    /**
     * Earth: Day 4h -> Night 4h
     */
    fun getEarthCycle(nowMs: Long): CycleState {
        val elapsed = nowMs - EARTH_EPOCH
        val cycleTime = elapsed % EARTH_CYCLE_MS
        val dayDuration = 4 * 60 * 60 * 1000L
        
        val isDay = cycleTime < dayDuration
        val timeLeft = if (isDay) (dayDuration - cycleTime) else (EARTH_CYCLE_MS - cycleTime)
        
        val expiryTime = nowMs + timeLeft
        val activationTime = expiryTime - dayDuration
        
        return CycleState(
            id = "earth-local-$expiryTime",
            activation = Instant.fromEpochMilliseconds(activationTime),
            expiry = Instant.fromEpochMilliseconds(expiryTime),
            isDay = isDay,
            state = if (isDay) "day" else "night",
            shortString = if (isDay) "Day" else "Night"
        )
    }
}
