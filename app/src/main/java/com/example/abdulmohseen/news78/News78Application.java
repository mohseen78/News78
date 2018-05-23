package com.example.abdulmohseen.news78;

import android.app.Application;

/*
** Used for getting the application instance
**/
public class News78Application extends Application {
    private static News78Application news78ApplicationInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        news78ApplicationInstance = this;
    }
    public static News78Application getNews78ApplicationInstance(){
        return news78ApplicationInstance;
    }
}
