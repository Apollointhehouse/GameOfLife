package game

@JvmInline
value class Pos(private val pos: Pair<Int, Int>) {
    val x: Int
        get() = pos.first

    val y: Int
        get() = pos.second

    operator fun component1() = x
    operator fun component2() = y
}