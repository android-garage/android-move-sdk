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

import com.android.sdk.core.utils.Logger;

import org.json.JSONObject;

import java.util.Map;

import static com.android.sdk.core.utils.DesignByContract.requireAny;
import static com.android.sdk.core.utils.DesignByContract.requireNonEmptyString;
import static com.android.sdk.core.utils.DesignByContract.requireNonNull;

/**
 * Created by deepu on 3/30/16.
 */
public class DataRequest {
    private static final String TAG = DataRequest.class.getSimpleName();

    VolleyRequest volleyRequest;
    private boolean cancelled;
    private DataRequestMethod dataRequestMethod;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> params;
    private JSONObject body;


    public static DataRequest createSimpleDataRequest(SimpleDataRequestMethod method, String path, Map<String, String> headers, Map<String, String> params) {
        requireNonNull(method);
        requireNonEmptyString(path);
        requireAny(headers);
        requireAny(params);

        DataRequest dataRequest = new DataRequest(method, path, headers, params, null);

        return dataRequest;
    }

    public static DataRequest createJsonDataRequest(JsonDataRequestMethod method, String path, Map<String, String> headers, Map<String, String> params, JSONObject body) {
        requireNonNull(method, "method");
        requireNonEmptyString(path, "path");
        requireAny(headers);
        requireAny(params);
        requireNonNull(body, "jsonBody");

        DataRequest dataRequest = new DataRequest(method, path, headers, params, body);

        return dataRequest;
    }

    private DataRequest(DataRequestMethod method, String path, Map<String, String> headers, Map<String, String> params, JSONObject body) {
        requireNonNull(method, "method");
        requireNonEmptyString(path, "path");
        requireAny(headers);
        requireAny(params);
        if (method.hasBody()) {
            requireNonNull(body, "body");
        } else {
            requireAny(body);
        }

        this.dataRequestMethod = method;
        this.path = path;
        this.headers = headers;
        this.params = params;
        this.body = body;
    }


    public void cancel() {
        cancelled = true;
        if (volleyRequest != null) {
            Logger.d(TAG, "cancelling n/w request");
            volleyRequest.cancel();
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public int getMethod() {
        return dataRequestMethod.getMethod();
    }

    public String getPath() {
        return path;
    }

    public byte[] getBody() {
        if (body != null) {
            String value = body.toString();
            return value.getBytes();
        } else {
            Logger.w(TAG, "getBody: body is null");

        }
        return null;
    }

    public boolean hasBody() {
        return dataRequestMethod.hasBody();
    }

}