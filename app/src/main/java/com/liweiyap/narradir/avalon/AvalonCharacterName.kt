package com.liweiyap.narradir.avalon

object AvalonCharacterName {
    const val MERLIN: Int = 0
    const val PERCIVAL: Int = 1
    const val LOYAL0: Int = 2
    const val LOYAL1: Int = 3
    const val LOYAL2: Int = 4
    const val LOYAL3: Int = 5
    const val LOYAL4: Int = 6
    const val LOYAL5: Int = 7
    const val ASSASSIN: Int = 8
    const val MORGANA: Int = 9
    const val MORDRED: Int = 10
    const val OBERON: Int = 11
    const val MINION0: Int = 12
    const val MINION1: Int = 13
    const val MINION2: Int = 14
    const val MINION3: Int = 15

    @JvmStatic
    fun getNumberOfCharacters(): Int {
        return 16
    }

    @JvmStatic
    fun getDefaultNumberOfGoodCharacters(): Int {
        return 3
    }

    @JvmStatic
    fun getDefaultNumberOfEvilCharacters(): Int {
        return 2
    }
}