package com.android.sdk.core.operations;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;

import com.android.sdk.core.Core;
import com.android.sdk.core.models.DataRequest;
import com.android.sdk.core.models.FailureMessage;
import com.android.sdk.core.models.Question;
import com.android.sdk.core.models.SimpleDataRequestMethod;
import com.android.sdk.core.utils.Logger;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.android.sdk.core.utils.Logger.LogLevel.*;

/**
 * Created by deepu on 4/9/16.
 */
public class ServiceOperationTests extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Core.init(getContext(), "http://private-8d0f8-androidmoveframeworkapi.apiary-mock.com");
        Logger.overrideLogLevel(DEBUG);
    }

    public void testServiceOperation() {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Operation<Question> operation = new TestServiceOperation();
        operation.execute(new OperationListener<Question>() {
            @Override
            public void onSuccess(Question result) {
                assertNotNull(result);
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(FailureMessage failureMessage) {
                fail();
            }
        });

        try {
            countDownLatch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            fail();
        }

    }

    class TestServiceOperation extends ServiceOperation<Question> {

        protected TestServiceOperation() {
            super(Question.class);
        }

        @Override
        protected DataRequest getDataRequest(@NonNull String path, @NonNull Map<String, String> headers, @NonNull Map<String, String> params) {
            return DataRequest.createSimpleDataRequest(SimpleDataRequestMethod.Get(), path, headers, params);
        }

        @Override
        protected String getEndPoint() {
            return "/questions";
        }
    }

}
