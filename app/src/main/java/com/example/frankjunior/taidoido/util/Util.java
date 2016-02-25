package com.example.frankjunior.taidoido.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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

    /*
     **********************************************
     *   Connection
     **********************************************
     */
    public static HttpURLConnection connect(String urlFile) throws IOException {
        final int SECONDS = 1000;
        URL url = new URL(urlFile);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(10 * SECONDS);
        conexao.setConnectTimeout(15 * SECONDS);
        conexao.setRequestMethod("GET");
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.connect();
        return conexao;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
}
