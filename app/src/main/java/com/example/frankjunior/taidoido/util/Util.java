package com.example.frankjunior.taidoido.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Método auxiliar para converter os bytes recem baixados de um InputStream para uma String
     *
     * @param is - INputStream recem baixado
     * @return - String com o resultado
     * @throws IOException
     */
    public static String bytesToString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }
}
