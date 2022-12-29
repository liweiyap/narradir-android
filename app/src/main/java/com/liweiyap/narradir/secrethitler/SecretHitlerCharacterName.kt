package com.liweiyap.narradir.secrethitler

object SecretHitlerCharacterName {
    const val LIBERAL0: Int = 0
    const val LIBERAL1: Int = 1
    const val LIBERAL2: Int = 2
    const val LIBERAL3: Int = 3
    const val LIBERAL4: Int = 4
    const val LIBERAL5: Int = 5
    const val HITLER: Int = 6
    const val FASCIST0: Int = 7
    const val FASCIST1: Int = 8
    const val FASCIST2: Int = 9

    @JvmStatic
    fun getNumberOfCharacters(): Int {
        return 10
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