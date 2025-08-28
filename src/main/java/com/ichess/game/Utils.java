//==============================================================================
//            Copyright (c) 2009-2014 ichess.co.il
//
//This document contains confidential information which is protected by
//copyright and is proprietary to ichess.co.il. No part
//of this document may be used, copied, disclosed, or conveyed to another
//party without prior written consent of ichess.co.il.
//==============================================================================

package com.ichess.game;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Utility methods for the chess game package
 * Extracted from com.ichess.util.Utils for independence
 */
public class Utils {

    private static Random _generator = null;

    // Log level constants
    public static final Level DEBUG = Level.FINE;
    public static final Level INFO = Level.INFO;
    public static final Level WARNING = Level.WARNING;
    public static final Level ERROR = Level.SEVERE;

    // Time constants
    public static final long MS_IN_SECOND = 1000L;
    public static final long MS_IN_MINUTE = MS_IN_SECOND * 60L;

    // Text encoding constants
    public static final String LRO = "\u202D";
    public static final String PDF = "\u202C";

    // Cache the clock as a static final variable
    private static final java.time.Clock CLOCK = java.time.Clock.systemUTC();

    /**
     * Assertion method
     */
    public static void Assert(boolean cond) {
        Assert(cond, "Assertion : condition failed");
    }

    /**
     * Assertion method with message
     */
    public static void Assert(boolean cond, String message) {
        assert cond : "Assertion : " + message + " at : \n" + getStackTrace(Thread.currentThread().getStackTrace());
    }

    /**
     * Assert null check
     */
    public static void AssertNull(Object obj) {
        Assert(obj != null, "Assertion : null");
    }

    /**
     * Assert null check with message
     */
    public static void AssertNull(Object obj, String message) {
        Assert(obj != null, message);
    }

    /**
     * Check if a number is between two values
     */
    public static boolean isBetween(int n, int x1, int x2) {
        if (x1 <= x2) {
            return ((x1 <= n) && (n <= x2));
        } else {
            return ((x2 <= n) && (n <= x1));
        }
    }

    /**
     * Check if a string is null or empty
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Check if a point is in the path between two other points
     */
    public static boolean isInPath(int x1, int y1, int x2, int y2, int x3, int y3) {
        if ((x1 == x2) && (y1 == y2)) {
            return true;
        }
        if ((x1 == x3) && (y1 == y3)) {
            return true;
        }

        // if x2=x3, then check if y1 is in the middle
        if (x2 == x3) {
            return ((x1 == x2) && (((y2 > y1) && (y1 > y3)) || ((y2 < y1) && (y1 < y3))));
        }

        // if y2=y3, then check if x1 is in the middle
        if (y2 == y3) {
            return ((y1 == y2) && (((x2 > x1) && (x1 > x3)) || ((x2 < x1) && (x1 < x3))));
        }

        // both are different, then it must be diagonal check
        int deltaX = x3 - x2;
        int deltaY = y3 - y2;

        if (Math.abs(deltaX) != Math.abs(deltaY)) {
            return false;
        }

        deltaX = deltaX / Math.abs(deltaX);
        deltaY = deltaY / Math.abs(deltaY);

        int curX = x2;
        int curY = y2;

        while ((curX != x3) && (curY != y3)) {
            if ((x1 == curX) && (y1 == curY)) {
                return true;
            }
            curX += deltaX;
            curY += deltaY;
        }
        return false;
    }

    /**
     * Join a list with default separator
     */
    public static <T> String join(List<T> list) {
        return join(list, ";");
    }

    /**
     * Join a list with a separator
     */
    public static <T> String join(List<T> list, String separator) {
        String result = "";
        if (list == null) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            T elem = list.get(i);
            if (elem != null) {
                result += elem.toString().replaceAll(separator, "");
            }
            if (i != (list.size() - 1)) {
                result += separator;
            }
        }
        return result;
    }

    /**
     * Join an array with a separator
     */
    public static String join(Object[] arr, String separator) {
        String result = "";
        if (arr == null) {
            return result;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                result += arr[i].toString().replaceAll(separator, "");
            }
            if (i != (arr.length - 1)) {
                result += separator;
            }
        }
        return result;
    }

    /**
     * Get first element from a list
     */
    public static <T> Object getFirstInList(List<T> list) {
        return getFirstInList(list, false);
    }

    /**
     * Get first element from a list with option to remove null primitives
     */
    public static <T> Object getFirstInList(List<T> list, boolean removeNullPrimitives) {
        T result = (list == null || list.size() == 0) ? null : list.get(0);
        if ((result != null) && (removeNullPrimitives)) {
            // Note: removeNullPrimitives functionality is simplified for game package
            // The original implementation used reflection which we don't need
        }
        return result;
    }

    /**
     * Parse boolean from string, return false if invalid
     */
    public static boolean parseBoolean(String str) {
        if (isEmptyString(str)) {
            return false;
        }
        return Boolean.parseBoolean(str);
    }

    /**
     * Parse integer from string, return 0 if invalid
     */
    public static int parseInt(String str) {
        if (isEmptyString(str)) {
            return 0;
        }
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            // ignore. this is what this function is about
        }
        return value;
    }

    /**
     * Parse float from string, return 0 if invalid
     */
    public static float parseFloat(String str) {
        if (isEmptyString(str)) {
            return 0;
        }
        float value = 0;
        try {
            value = Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            // ignore. this is what this function is about
        }
        return value;
    }

    /**
     * Parse double from string, return 0 if invalid
     */
    public static double parseDouble(String str) {
        if (isEmptyString(str)) {
            return 0;
        }
        double value = 0;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            // ignore. this is what this function is about
        }
        return value;
    }

    /**
     * Parse long from string, return 0 if invalid
     */
    public static long parseLong(String str) {
        if (isEmptyString(str)) {
            return 0;
        }
        long value = 0;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException ex) {
            // ignore. this is what this function is about
        }
        return value;
    }

    /**
     * Generate random number in range [0, n-1]
     */
    public static int random(int n) {
        if (_generator == null) {
            _generator = new Random(nowInMs());
        }
        return ((Math.abs(_generator.nextInt())) % n);
    }

    /**
     * Encode string in RLE format
     */
    public static String encodeInRLE(String str) {
        return "\u202B" + str + PDF; // RLE + str + PDF
    }

    /**
     * Returns current time as Date using the cached clock.
     */
    public static Date now() {
        return Date.from(CLOCK.instant());
    }

    /**
     * Returns current time in milliseconds extracted from the same
     * Date object returned by now().
     */
    public static long nowInMs() {
        return now().getTime();
    }

    /**
     * Get current calendar
     */
    public static Calendar nowCalendar() {
        return Calendar.getInstance();
    }

    /**
     * Get stack trace as string
     */
    public static String getStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("\t").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
