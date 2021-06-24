package com.liweiyap.narradir.util;

import com.liweiyap.narradir.R;

public final class PlayerNumberDictionary
{
    private PlayerNumberDictionary(){}

    public static int getSelectorButtonIdFromPlayerNumber(final int playerNumber)
    {
        switch (playerNumber)
        {
            case 5:
                return R.id.p5Button;
            case 6:
                return R.id.p6Button;
            case 7:
                return R.id.p7Button;
            case 8:
                return R.id.p8Button;
            case 9:
                return R.id.p9Button;
            case 10:
                return R.id.p10Button;
        }

        return 0;
    }
}