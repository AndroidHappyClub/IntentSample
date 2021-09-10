package com.water.intentsample;

import android.app.Application;

public class MyApplication extends Application {
    private String _name;
    private static MyApplication _instance;

    public static MyApplication getInstance() {
        return _instance;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public void onCreate() {
       super.onCreate();
       _instance = this;
    }
}
