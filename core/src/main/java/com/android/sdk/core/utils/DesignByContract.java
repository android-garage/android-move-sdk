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

import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Read about DBC: https://www.eiffel.com/values/design-by-contract/introduction/
 * <p/>
 * Created by deepu on 4/2/16.
 */
public final class DesignByContract {


    //REQUIRE
    public static void requireNonEmptyCollection(Collection<Object> collection, String... paramName) {
        requireNonNull(collection, paramName);
        StringBuilder message = new StringBuilder("non empty collection").append("(").append(getOptionalMessage(paramName)).append(")");
        require(collection.size() > 0, message.toString());
    }

    public static void requireNonEmptyMap(Map<Object, Object> map, String... paramName) {
        requireNonNull(map, paramName);
        StringBuilder message = new StringBuilder("non empty map.").append("(").append(getOptionalMessage(paramName)).append(")");
        require(map.size() > 0, message.toString());
    }

    public static void requireNonNull(Object obj, String... paramName) {
        StringBuilder message = new StringBuilder("non null object").append("(").append(getOptionalMessage(paramName)).append(")");
        require((obj != null), message.toString());
    }

    public static void requireNonEmptyString(String str, String... paramName) {
        requireNonNull(str, paramName);
        StringBuilder message = new StringBuilder("non empty string").append("(").append(getOptionalMessage(paramName)).append(")");
        require(str.length() > 0, (TextUtils.isEmpty(getOptionalMessage()) ? getOptionalMessage() : "non empty string"));
    }


    public static void requireAny(Object object) {
        //Indicates that user can pass a null object
    }

    public static void requireShouldOverRide(String... optionalMessage) {
        StringBuilder message = new StringBuilder(" sub class should override:  ").append(getOptionalMessage());
        require(false, message.toString());
    }

    public static void requireShouldNeverReachHere() {
        StringBuilder message = new StringBuilder("should never reach here");
        require(false, message.toString());
    }

    public static void require(boolean require, String message) {
        if (!require) {
            throw new DesignByContractRequireException("Design By Contract Exception. !!!REQUIRE!!! ->>" + message, null);
        } else {
            //Do nothing.
        }
    }


    //ENSURE
    public static void ensureNonEmptyCollection(Collection<Object> collection, String... paramName) {
        requireNonNull(collection);
        StringBuilder message = new StringBuilder("non empty collection").append("(").append(getOptionalMessage(paramName)).append(")");
        require(collection.size() > 0, message.toString());
    }

    public static void ensureNonEmptyMap(Map<Object, Object> map, String... paramName) {
        requireNonNull(map);
        StringBuilder message = new StringBuilder("non empty map.").append("(").append(getOptionalMessage(paramName)).append(")");
        require(map.size() > 0, message.toString());
    }

    public static void ensureNonNull(Object obj, String... paramName) {
        StringBuilder message = new StringBuilder("non null object").append("(").append(getOptionalMessage(paramName)).append(")");
        require((obj != null), message.toString());
    }

    public static void ensureNonEmptyString(String str, String... paramName) {
        requireNonNull(str);
        StringBuilder message = new StringBuilder("non empty string").append("(").append(getOptionalMessage(paramName)).append(")");
        require(str.length() > 0, message.toString());
    }


    public static void ensureAny(Object object) {
        //Indicates that user can pass a null object
    }

    public static void ensure(boolean require, String message) {
        if (!require) {
            throw new DesignByContractEnsureException("Design By Contract Exception. !!!ENSURE!!! ->>" + message + " failed.", null);
        } else {
            //Do nothing.
        }
    }

    private static String getOptionalMessage(String... params) {
        if (params != null
                && params.length > 0
                && !TextUtils.isEmpty(params[0])) {
            return params[0];
        }

        return "";
    }


    public static class DesignByContractRequireException extends RuntimeException {

        DesignByContractRequireException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DesignByContractEnsureException extends RuntimeException {

        DesignByContractEnsureException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
