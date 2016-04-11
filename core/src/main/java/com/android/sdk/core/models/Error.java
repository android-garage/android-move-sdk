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

package com.android.sdk.core.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.sdk.core.Core;
import com.android.sdk.core.R;

import static com.android.sdk.core.utils.DesignByContract.requireAny;
import static com.android.sdk.core.utils.DesignByContract.requireNonEmptyString;
import static com.android.sdk.core.utils.DesignByContract.requireNonNull;

/**
 * Created by deepu on 4/5/16.
 */
public class Error implements FailureMessage {

    private final String code;
    private final String message;
    private final Exception exception;


    enum Code {
        UNKNOWN(R.string.error_message_unknown),
        SERVICE_RESPONSE_ERROR(R.string.error_message_unknown),
        NETWORK_ERROR(R.string.error_message_network_unavailable);

        private int stringResourceId;

        Code(int stringResourceId) {
            this.stringResourceId = stringResourceId;
        }

        @Override
        public String toString() {
            //DO NOT OVERRIDE: this is used in {@code ErrorCodes}
            return super.toString();

        }
    }

    /**
     * Creates an error message with the given code.
     *
     * @param code      StringCode, Use this when your code is not available in {@code Error#Code}
     * @param message   Error message
     * @param exception Optional exception that can be passed to better debugging.
     * @return {@code Error}
     */
    public static Error CreateErrorMessage(@NonNull String code, @NonNull String message, @Nullable Exception exception) {
        requireNonEmptyString(code);
        requireNonEmptyString(message);
        requireAny(exception);
        return new Error(code, message, exception);
    }

    /**
     * Creates an error message with the given code.
     *
     * @param code      {@code Error#Code}
     * @param exception Optional exception that can be passed to better debugging.
     * @return {@code Error}
     */
    public static Error CreateErrorMessageWithCode(@NonNull Code code, @Nullable Exception exception) {
        requireNonNull(code);
        requireAny(exception);
        return new Error(code.toString(), getString(code.stringResourceId), exception);
    }

    private Error(@NonNull String code, @NonNull String message, @Nullable Exception exception) {
        requireNonEmptyString(code);
        requireNonEmptyString(message);
        requireAny(exception);

        this.code = code;
        this.message = message;
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Nullable
    @Override
    public Exception getException() {
        return exception;
    }

    private static String getString(int resId) {
        return Core.getInstance().getApplicationContext().getResources().getString(resId);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("code: ").append(code).append(" message: ").append(message).append(" exception: ").append(getExceptionMessage());
        return stringBuilder.toString();
    }

    private String getExceptionMessage() {
        if (exception != null) {
            return exception.getMessage();
        }

        return "";
    }
}
