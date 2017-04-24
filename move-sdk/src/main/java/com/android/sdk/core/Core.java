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

package com.android.sdk.core;

import android.content.Context;

import static com.android.sdk.core.utils.DesignByContract.require;
import static com.android.sdk.core.utils.DesignByContract.requireAny;
import static com.android.sdk.core.utils.DesignByContract.requireNonNull;

/**
 * Created by deepu on 4/2/16.
 */
public final class Core {

    private static Context sApplicationContext;
    private static Core sInstance;


    private String baseUrl;

    public static void init(final Context applicationContext, final String baseUrl) {
        requireNonNull(applicationContext);
        requireAny(baseUrl);
        sInstance = new Core(applicationContext, baseUrl);
    }

    private Core(final Context applicationContext, final String baseUrl) {
        sApplicationContext = applicationContext;
        this.baseUrl = baseUrl;
    }

    public static Core getInstance() {
        require(sInstance != null, "Core not initialized, call Core.init(..) before calling this method.");
        return sInstance;
    }

    public Context getApplicationContext() {
        return sApplicationContext;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
