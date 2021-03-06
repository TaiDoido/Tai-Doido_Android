package com.example.frankjunior.taidoido.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by frankjunior on 24/02/16.
 */
public class Util {

    private static final int INVALID_POSITION = -1;
    private static final String TEMPLATE_HTML = "template.html";
    private static final String CONTENT = "#CONTEUDO#";
    private static final String TITLE = "#TITLE#";
    private static final String IFRAME_OPEN_TAG = "<iframe";
    private static final String IFRAME_CLOSE_TAG = "</iframe>";
    private static final String CLOSE_TAG = "/>";
    private static final String YOUTUBE = "youtube";
    private static final String PATTERN = "(?:embed\\/|v=)([\\w-]+)";
    private static final int BUFFER_SIZE = 1024;

    // Sql comments begin with two consecutive "-" characters
    private static final String REG_COMMENT_EXPRESSION = "--";
    private static final String REG_EXPRESSION = ";";

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

    /**
     * Método que faz a requisição e o donwload do JSON
     *
     * @param url da requisição
     * @return JSON em formato de String
     * @throws IOException
     */
    public static String doGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String formatHtml(Context ctx, String html, String title) {
        String newHtml = html;
        newHtml = findYoutubeTag(newHtml);
        try {
            InputStream inputStream = ctx.getAssets().open(TEMPLATE_HTML);
            String template = streamToString(inputStream);
            newHtml = template.replaceAll(CONTENT, newHtml)
                    .replaceAll(TITLE, title);
            inputStream.close();
        } catch (IOException e) {
            MyLog.printError("error", e);
        }
        return newHtml;
    }

    /**
     * Split in strings the content of sql files.
     *
     * @param context
     * @param fileNames
     * @return
     */
    public static String[] getStatementSql(Context context, final String fileNames) {
        final StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream;
        BufferedReader sqlFile = null;
        try {
            inputStream = context.getAssets().open(fileNames);
            final InputStreamReader reader = new InputStreamReader(inputStream,
                    Charset.defaultCharset());
            sqlFile = new BufferedReader(reader);
            String buffer;
            while ((buffer = sqlFile.readLine()) != null) {
                //Ignore comment in sql
                if (!buffer.startsWith(REG_COMMENT_EXPRESSION)) {
                    stringBuilder.append(buffer);
                }
            }
        } catch (final IOException e) {
            MyLog.printError("Error opening SQL file", e);
        } finally {
            if (sqlFile != null) {
                try {
                    sqlFile.close();
                } catch (final IOException e) {
                    MyLog.printError("Error closing SQL file", e);
                }
            }
        }
        return stringBuilder.toString().split(REG_EXPRESSION);
    }

     /*
      **********************************************
      *   Métodos private
      **********************************************
      */

    private static String streamToString(InputStream is) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }

    /**
     * Função pra procurar por tags do Youtube
     *
     * @param newHtml
     * @return
     */
    private static String findYoutubeTag(String newHtml) {
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
            MyLog.printError("error to find youtube tag", e);
        }
        return newHtml;
    }

}
