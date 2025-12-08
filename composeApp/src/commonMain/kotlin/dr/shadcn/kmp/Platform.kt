package dr.shadcn.kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform