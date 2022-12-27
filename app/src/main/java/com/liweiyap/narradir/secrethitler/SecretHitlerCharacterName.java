package com.liweiyap.narradir.secrethitler;

/**
 * https://stackoverflow.com/questions/8157755/how-to-convert-enum-value-to-int
 */
public final class SecretHitlerCharacterName
{
    private SecretHitlerCharacterName(){}

    public static final int LIBERAL0 = 0;
    public static final int LIBERAL1 = 1;
    public static final int LIBERAL2 = 2;
    public static final int LIBERAL3 = 3;
    public static final int LIBERAL4 = 4;
    public static final int LIBERAL5 = 5;
    public static final int HITLER = 6;
    public static final int FASCIST0 = 7;
    public static final int FASCIST1 = 8;
    public static final int FASCIST2 = 9;

    public static int getNumberOfCharacters()
    {
        return 10;
    }

    public static int getDefaultNumberOfGoodCharacters() {
        return 3;
    }

    public static int getDefaultNumberOfEvilCharacters() {
        return 2;
    }
}