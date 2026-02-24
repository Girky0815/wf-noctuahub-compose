package jp.girky.wf_noctuahub

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform