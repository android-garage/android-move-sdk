/*
 * MIT License
 *
 * Copyright (c) 2016
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.android.sdk.core.utils;


import android.util.Log;

import static com.android.sdk.core.utils.DesignByContract.requireNonNull;

/**
 * Wrapper class for android's {@link Log}
 * <p/>
 * By default the log level is set to {@link com.android.sdk.core.utils.Logger.LogLevel#DEBUG}, if you want to override the log level call {@link Logger#overrideLogLevel(LogLevel)}
 * To turnOff logging, call {@link Logger#overrideLogLevel(LogLevel)} with {@link com.android.sdk.core.utils.Logger.LogLevel#OFF}
 * <p/>
 * <p/>
 * Created by deepu on 4/10/16.
 */
public final class Logger {

    public enum LogLevel {
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARN(Log.WARN),
        ERROR(Log.ERROR),
        OFF(0);

        private int level;

        LogLevel(int level) {

            this.level = level;

        }
    }

    private static LogLevel currentLogLevel;

    private Logger() {
        currentLogLevel = LogLevel.ERROR;
    }

    public static void overrideLogLevel(LogLevel level) {
        requireNonNull(level);
        currentLogLevel = level;
    }

    /**
     * Send a {@link android.util.Log#VERBOSE} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        if (shouldLog(LogLevel.VERBOSE)) {
            return Log.v(tag, msg);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#VERBOSE} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        if (shouldLog(LogLevel.VERBOSE)) {
            return Log.v(tag, msg, tr);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        if (shouldLog(LogLevel.DEBUG)) {
            return Log.d(tag, msg);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#DEBUG} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        if (shouldLog(LogLevel.DEBUG)) {
            return Log.d(tag, msg, tr);
        }
        return -1;
    }

    /**
     * Send an {@link android.util.Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        if (shouldLog(LogLevel.INFO)) {
            return Log.i(tag, msg);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#INFO} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        if (shouldLog(LogLevel.INFO)) {
            return Log.i(tag, msg, tr);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        if (shouldLog(LogLevel.WARN)) {
            return Log.w(tag, msg);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        if (shouldLog(LogLevel.WARN)) {
            return Log.w(tag, msg, tr);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param tr  An exception to log
     */
    public static int w(String tag, Throwable tr) {
        if (shouldLog(LogLevel.WARN)) {
            return Log.w(tag, tr);
        }
        return -1;
    }

    /**
     * Send an {@link android.util.Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        if (shouldLog(LogLevel.ERROR)) {
            return Log.e(tag, msg);
        }
        return -1;
    }

    /**
     * Send a {@link android.util.Log#ERROR} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        if (shouldLog(LogLevel.ERROR)) {
            return Log.w(tag, msg, tr);
        }
        return -1;
    }

    private static boolean shouldLog(LogLevel level) {
        return (level.ordinal() >= currentLogLevel.ordinal());
    }


}
