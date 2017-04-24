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

package com.android.sdk.core.operations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.sdk.core.utils.Logger;

import com.android.sdk.core.Core;
import com.android.sdk.core.models.DataListener;
import com.android.sdk.core.models.DataRequest;
import com.android.sdk.core.models.DataTransaction;
import com.android.sdk.core.models.DataTransceiver;
import com.android.sdk.core.models.FailureMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.sdk.core.utils.DesignByContract.ensure;
import static com.android.sdk.core.utils.DesignByContract.ensureNonEmptyString;
import static com.android.sdk.core.utils.DesignByContract.ensureNonNull;
import static com.android.sdk.core.utils.DesignByContract.requireNonNull;
import static com.android.sdk.core.utils.DesignByContract.requireShouldOverRide;

/**
 * Base operation for making a network call and getting the parsed response model object.
 * Subclass this operation and override necessary methods to provide your service details.
 * Created by deepu on 3/30/16.
 */
public abstract class ServiceOperation<T> implements Operation {

    private static final String TAG = ServiceOperation.class.getSimpleName();

    private final Class resultType;
    private DataRequest dataRequest;
    private boolean cancelled;


    /**
     * Create your data request using the passed parameters
     *
     * @param path    : Service URL
     * @param headers : add any additional values you may want to pass, you can also override {@code #updateHeaders()} to provide additional headers
     * @param params  : add any additional values you may want to pass, you can also override {@code #updateParams()} to provide additional headers
     * @return DataRequest
     */
    protected abstract DataRequest getDataRequest(@NonNull String path, @NonNull Map<String, String> headers, @NonNull Map<String, String> params);


    /**
     * Return your service end point. This will get appended to the base url provided through {@code Core#init} or by overriding {@code #getBaseUrl()}
     *
     * @return NON-NULL String
     */
    protected abstract String getEndPoint();

    /**
     * Service operation constructor
     *
     * @param clazz, Response object class type, Pass {@code Void#class} if a response object is not expected for an operation
     * @param clazz
     */
    protected ServiceOperation(@NonNull Class<T> clazz) {
        requireNonNull(clazz);
        resultType = clazz;
    }


    @Override
    public void execute(final OperationListener operationListener) {
        Logger.d(TAG, "entering execute operation");

        if (dataRequest != null) {
            Logger.w(TAG, "execute: WILL NOT EXECUTE, This operation is currently in execution");
            return;
        }
        cancelled = false;


        String path = generateURl();
        Logger.d(TAG, "path: " + path);

        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        dataRequest = getDataRequest(path, headers, params);
        ensureNonNull(dataRequest, "dataRequest");

        DataTransceiver.getsInstance().queueJsonRequest(dataRequest, new DataListener() {
            @Override
            public void onSuccess(DataTransaction dataTransaction) {
                dataRequest = null;
                if (cancelled) {
                    logCancelled("onSuccess");
                    return;
                }
                Logger.d(TAG, "onSuccess: " + dataTransaction.getDataResponse().getJson());
                requireNonNull(dataTransaction);

                T result = null;
                if (!isVoidResult()) {
                    requireNonNull(dataTransaction.getDataResponse().getJson(), "jsonResponse");
                    result = getResultFromJson(dataTransaction.getDataResponse().getJson());
                } else {
                    Logger.d(TAG, "Empty result, since the operation was NOT expecting a resultType");
                }

                completeWithResult(result, operationListener);

            }

            @Override
            public void onFailure(DataTransaction dataTransaction) {
                dataRequest = null;
                if (cancelled) {
                    logCancelled("onFailure");
                    return;
                }
                Logger.d(TAG, "onFailure: " + dataTransaction.getDataResponse().getFailureMessage());
                requireNonNull(dataTransaction);
                completeWithMessage(dataTransaction.getDataResponse().getFailureMessage(), operationListener);

            }
        });

        Logger.d(TAG, "exiting execute operation");
    }

    protected void updateHeaders(Map<String, String> headers) {
        //Override
    }

    protected void updateParams(Map<String, String> headers) {
        //Override
    }

    protected void completeWithResult(@Nullable T result, @NonNull OperationListener<T> listener) {
        listener.onSuccess(result);
    }

    protected void completeWithMessage(FailureMessage failureMessage, OperationListener listener) {
        listener.onFailure(failureMessage);
    }

    private T getResultFromJson(JSONObject jsonObject) {
        requireNonNull(jsonObject, "responseJson");
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();

        Object obj = gson.fromJson(jsonObject.toString(), resultType);
        ensureNonNull(obj);
        ensure(resultType.isAssignableFrom(obj.getClass()), "Expected type: " + resultType + " actual type: " + obj);

        return (T) obj;
    }


    /**
     * Call this method when you want to cancel an operation in execution.
     * <p/>
     * Note that, an operation will only be cancelled if it is in execution.
     *
     * @return true if the operation was cancelled, false otherwise.
     */
    @Override
    public boolean cancel() {
        if (!cancelled
                && dataRequest != null) {
            cancelled = true;
            dataRequest.cancel();
            Logger.d(TAG, "operation cancelled: " + this);
            return true;
        } else {
            if (dataRequest == null) {
                Logger.d(TAG, "cancel :operation not in execution");
            } else {
                Logger.d(TAG, "cancel :operation already cancelled");
            }
            return false;
        }
    }

    private void logCancelled(@Nullable final String optionalPrefixMessage) {
        StringBuilder sb = new StringBuilder(optionalPrefixMessage != null ? optionalPrefixMessage : "");
        sb.append(": Ignoring operation[ ").append(this).append("], since the operation is already cancelled.");

        Logger.d(TAG, sb.toString());
    }

    /**
     * @return true: if null result object is expected for the operation, false otherwise
     */
    private boolean isVoidResult() {
        return Void.class.isAssignableFrom(resultType);

    }


    private String generateURl() {
        ensureNonEmptyString(getBaseUrl(), "baseUrl");
        ensureNonEmptyString(getEndPoint(), "Url endPoint");

        StringBuilder stringBuilder = new StringBuilder(getBaseUrl()).append(getEndPoint());
        return stringBuilder.toString();
    }

    /**
     * Override when necessary, it is recommended not to override this method but provide your base URL in @{@code com.android.sdk.core.Core#init}
     *
     * @return
     */
    protected String getBaseUrl() {
        String url = Core.getInstance().getBaseUrl();
        if (TextUtils.isEmpty(url)) {
            requireShouldOverRide("Looks like a BaseURL is not defined in Core, either provide a base URL in core or override getBaseUrl() in your operation");
        }
        return url;
    }
}
