package com.android.sdk.core.storage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.sdk.core.utils.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by deepu on 7/3/16.
 */

public final class StorageUtil {
    private static final String TAG = "StorageUtil";

    public static void writeToInternalStorage(Context context, String fileName, String content) {

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            Logger.e(TAG, "writeToInternalStorage: " + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.e(TAG, "writeToInternalStorage: " + e.getMessage());
                }
            }
        }

    }

    @Nullable
    public static String readFileFromInternalStorage(Context context, String fileName) {

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            int c;
            StringBuilder outputStrBuilder = new StringBuilder();
            while ((c = fis.read()) != -1) {
                outputStrBuilder.append((char) c);
            }
            return outputStrBuilder.toString();
        } catch (FileNotFoundException e) {
            Logger.e(TAG, "readFileFromInternalStorage: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.e(TAG, "readFileFromInternalStorage: " + e.getMessage());
                }
            }
        }
        return null;
    }
}
