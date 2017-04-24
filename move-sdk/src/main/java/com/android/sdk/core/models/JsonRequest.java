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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by deepu on 4/4/16.
 */
public class JsonRequest extends VolleyRequest<JSONObject> {
    private static final String TAG = JsonRequest.class.getSimpleName();

    public JsonRequest(DataTransaction dataTransaction, DataListener dataListener) {
        super(dataTransaction, dataListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data);
            Logger.d(TAG, "parseNetworkResponse: " + json);
            HttpHeaderParser.parseCharset(response.headers);
            return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            Logger.w(TAG, "Failed: parseNetworkResponse: " + e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        dataTransaction.getDataResponse().setJson(response);
        dataListener.onSuccess(dataTransaction);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("User-agent", System.getProperty("http.agent"));
        return headers;
    }
}
