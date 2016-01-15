package com.example.frankjunior.taidoido.util;

import android.util.Log;

/**
 * Created by frankjunior on 15/01/16.
 */
public class MyLog {

    public static final String OPEN_BRACKET = "[";
    public static final String CLOSE_BRACKET = "] - ";
    public static final String FILE_EXTENSION = ".java";
    public static final String SEPARATOR = " - ";
    public static final int METHOD_NAME_INDEX = 2;
    private static boolean DEBUG = true;
    private final static String DEFAULT_TAG = "fcbj";

    public static void print(String tag, String message) {
        if (DEBUG) {
            Log.i(tag, OPEN_BRACKET + getClassCaller() + CLOSE_BRACKET + message);
        }
    }

    public static void print(String message) {
        if (DEBUG) {
            Log.i(DEFAULT_TAG, OPEN_BRACKET + getClassCaller() + CLOSE_BRACKET + message);
        }
    }

    public static void printError(String message, Throwable e) {
        if (DEBUG) {
            Log.e(DEFAULT_TAG, OPEN_BRACKET + getClassCaller() + CLOSE_BRACKET + message, e);
        }
    }

    private static String getClassCaller() {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();
        return elements[METHOD_NAME_INDEX].getFileName().
                replace(FILE_EXTENSION, "") + SEPARATOR + elements[METHOD_NAME_INDEX].getMethodName();
    }
}

