package com.example.loginpage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private com.android.volley.RequestQueue mRequestQueue;
    private static Context mContext;
    private VolleySingleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
    }
    public static synchronized VolleySingleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
    public com.android.volley.RequestQueue getRequestQueue(){
        if (mRequestQueue ==null);
        mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
