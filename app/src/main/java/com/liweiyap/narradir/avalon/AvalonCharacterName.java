package com.liweiyap.narradir.avalon;

/**
 * https://stackoverflow.com/questions/8157755/how-to-convert-enum-value-to-int
 */
public final class AvalonCharacterName
{
    private AvalonCharacterName(){}

    public static final int MERLIN = 0;
    public static final int PERCIVAL = 1;
    public static final int LOYAL0 = 2;
    public static final int LOYAL1 = 3;
    public static final int LOYAL2 = 4;
    public static final int LOYAL3 = 5;
    public static final int LOYAL4 = 6;
    public static final int LOYAL5 = 7;
    public static final int ASSASSIN = 8;
    public static final int MORGANA = 9;
    public static final int MORDRED = 10;
    public static final int OBERON = 11;
    public static final int MINION0 = 12;
    public static final int MINION1 = 13;
    public static final int MINION2 = 14;
    public static final int MINION3 = 15;

    public static int getNumberOfCharacters()
    {
        return 16;
    }
}