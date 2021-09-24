package com.example.client;

import com.example.client.R;

public class IconMaker {
    private boolean clickedConfirme;
    private boolean clickedDecline;
    private int iconConfirme;
    private int iconDecline;

    public IconMaker(boolean clickedConfirme, boolean clickedDecline) {
        if (clickedConfirme)
            iconConfirme= R.drawable.iconfinder_tic;
        else iconConfirme= R.drawable.iconfinder_tick_success_done_valid_5402431;

        if (clickedDecline)
            iconDecline= R.drawable.iconfinder_freebie;
        else iconDecline= R.drawable.iconfinder_freebies1_glyph_2920349;
    }

    public int getIconConfirme() {
        return iconConfirme;
    }

    public int getIconDecline() {
        return iconDecline;
    }
}
