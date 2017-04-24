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

import com.android.sdk.core.Core;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static com.android.sdk.core.utils.DesignByContract.ensureNonNull;
import static com.android.sdk.core.utils.DesignByContract.requireNonNull;

/**
 * Takes care of HTTP request and response.
 * <p/>
 * Created by deepu on 3/30/16.
 */
public final class DataTransceiver {
    private static final String TAG = DataTransceiver.class.getSimpleName();
    private static final DataTransceiver sInstance = new DataTransceiver();
    private static final RequestQueue queue = Volley.newRequestQueue(Core.getInstance().getApplicationContext());

    private DataTransceiver() {

    }

    public static DataTransceiver getsInstance() {
        return sInstance;
    }


    /**
     * Queues any service request that returns a Json response.
     *
     * @param dataRequest
     * @param dataListener
     */
    public void queueJsonRequest(DataRequest dataRequest, final DataListener dataListener) {
        requireNonNull(dataRequest);
        requireNonNull(dataListener);

        if (!dataRequest.isCancelled()) {
            DataTransaction transaction = new DataTransaction(dataRequest);

            VolleyRequest request = new JsonRequest(transaction, new DataListener() {
                @Override
                public void onSuccess(DataTransaction dataTransaction) {
                    Logger.d(TAG, "onSuccess");
                    dataListener.onSuccess(dataTransaction);
                }

                @Override
                public void onFailure(DataTransaction dataTransaction) {
                    ensureNonNull(dataTransaction.getDataResponse().getFailureMessage());
                    dataListener.onFailure(dataTransaction);
                }
            });

            dataRequest.volleyRequest = request;
            synchronized (sInstance) {
                Logger.d(TAG, "queueing Request");
                queue.add(request);
            }
        } else {
            Logger.d(TAG, "queueJsonRequest: Will not queue DataRequest: " + dataRequest + ", request already cancelled");
        }
    }
}

