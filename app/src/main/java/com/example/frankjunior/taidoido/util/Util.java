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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by frankjunior on 24/02/16.
 */
public class Util {

    public static final int INVALID_POSITION = -1;
    private static final String TEMPLATE_HTML = "template.xml";
    private static final String CONTENT = "#CONTEUDO#";
    private static final String IFRAME_OPEN_TAG = "<iframe";
    private static final String IFRAME_CLOSE_TAG = "</iframe>";
    private static final String CLOSE_TAG = "/>";
    private static final String YOUTUBE = "youtube";
    private static final String PATTERN = "(?:embed\\/|v=)([\\w-]+)";
    private static final int BUFFER_SIZE = 1024;

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

    public static String streamToString(InputStream is) throws IOException {

        byte[] bytes = new byte[BUFFER_SIZE];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }


    public static String formatHtml(Context ctx, String html) {
        String newHtml = html;
        try {
            // searching for youtube iframe tags
            boolean findAgain;
            do {
                findAgain = false;
                // finding first <iframe> tag
                int iframeStartIndex = newHtml.indexOf(IFRAME_OPEN_TAG);

                // if found...
                if (iframeStartIndex > -1) {

                    // find the close tag </iframe>
                    String iframeCloseTag = IFRAME_CLOSE_TAG;
                    int iframeCloseTagLength = iframeCloseTag.length();
                    int iframeEndIndex = newHtml.indexOf(iframeCloseTag, iframeStartIndex);
                    if (iframeEndIndex == -1) {
                        iframeEndIndex = newHtml.indexOf(CLOSE_TAG, iframeStartIndex);
                        iframeCloseTagLength = 0;
                    }

                    // get iframe complete tag
                    String iframeTag = newHtml.substring(iframeStartIndex, iframeEndIndex + iframeCloseTagLength);

                    // if the iframe tag has youtube...
                    if (iframeTag.contains(YOUTUBE)) {

                        // finding video id
                        try {
                            Pattern compiledPattern = Pattern.compile(PATTERN);
                            Matcher matcher = compiledPattern.matcher(iframeTag);

                            if (matcher.find()) {
                                String videoId = matcher.group(1);

                                String videoLink =
                                        "<a href='http://www.youtube.com/watch?v=" + videoId + "'>" +
                                                "<img src='http://img.youtube.com/vi/" + videoId + "/0.jpg'>" +
                                                "</a>";

                                newHtml = newHtml.replace(iframeTag, videoLink);
                                findAgain = true;
                            }
                        } catch (Exception e) {
                            MyLog.printError("error", e);
                        }
                    }
                }
            } while (findAgain);
        } catch (Exception e) {
            MyLog.printError("error", e);
        }
        try {
            String template = streamToString(ctx.getAssets().open(TEMPLATE_HTML));
            newHtml = template.replaceAll(CONTENT, newHtml);
        } catch (IOException e) {
            MyLog.printError("error", e);
        }
        return newHtml;
    }

    /**
     * Método auxiliar para converter os bytes recem baixados de um InputStream para uma String
     *
     * @param is - INputStream recem baixado
     * @return - String com o resultado
     * @throws IOException
     */
    public static String bytesToString(InputStream is) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int bytesLidos;
        while ((bytesLidos = is.read(buffer)) != -1) {
            bufferzao.write(buffer, 0, bytesLidos);
        }
        return new String(bufferzao.toByteArray(), "UTF-8");
    }
}
