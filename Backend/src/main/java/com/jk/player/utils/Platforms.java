package com.jk.player.utils;

public enum Platforms {
    //0 for local, 1 for netease, 2 for bili, 3 for youtube
    LOCAL(0), BILI(1), NETEASE(2), YTB(3);

    private final int numVal;

    Platforms(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
