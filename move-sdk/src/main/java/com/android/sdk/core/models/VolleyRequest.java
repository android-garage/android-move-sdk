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
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

import static com.android.sdk.core.utils.DesignByContract.ensureNonNull;

/**
 * Created by deepu on 4/5/16.
 */
public abstract class VolleyRequest<T> extends Request<T> {

    private static final String TAG = VolleyRequest.class.getSimpleName();
    protected DataTransaction dataTransaction;
    protected DataListener dataListener;

    public VolleyRequest(final DataTransaction dataTransaction, final DataListener dataListener) {
        super(dataTransaction.getDataRequest().getMethod(), dataTransaction.getDataRequest().getPath(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "onErrorResponse: " + error);
                Error localError;
                if (error.getCause() instanceof NetworkError) {
                    localError = Error.CreateErrorMessageWithCode(Error.Code.NETWORK_ERROR, error);
                } else if (error instanceof ParseError) {
                    localError = Error.CreateErrorMessageWithCode(Error.Code.SERVICE_RESPONSE_ERROR, error);
                } else {
                    localError = Error.CreateErrorMessageWithCode(Error.Code.UNKNOWN, error);
                }
                ensureNonNull(localError);
                dataTransaction.getDataResponse().setFailureMessage(localError);
                dataListener.onFailure(dataTransaction);
            }
        });
        this.dataTransaction = dataTransaction;
        this.dataListener = dataListener;
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        if (dataTransaction.getDataRequest().hasBody()) {
            return dataTransaction.getDataRequest().getBody();
        }
        return super.getBody();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return dataTransaction.getDataRequest().getParams();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return dataTransaction.getDataRequest().getHeaders();
    }
}
