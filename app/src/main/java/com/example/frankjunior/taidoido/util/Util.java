package com.example.frankjunior.taidoido.util;

import java.util.ArrayList;

/**
 * Created by frankjunior on 24/02/16.
 */
public class Util {

    public static final int INVALID_POSITION = -1;

    private Util() {
    }

    /***
     * Get last position from arraylist
     *
     * @param list arraylist
     * @return the last position
     */
    public static int getLastPositionFromList(ArrayList list) {
        int lastPosition;

        if (list != null && list.size() > 0) {
            lastPosition = list.size() - 1;
        } else {
            lastPosition = INVALID_POSITION;
        }
        return lastPosition;
    }
}
