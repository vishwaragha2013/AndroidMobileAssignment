

package com.android.mobileassignment.utils.network;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
 * This class is a singleton class which returns an instance of a Request queue and Image Loader
 */
public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRequestQueue;


    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * Generate Lru Cache max size and return.
     *
     * @return
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    /**
     * Get volley request queue instance.
     *
     * @return RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

}

