package com.lk.bihu;

import android.app.Application;

import com.lk.bihu.utils.ImageDownLoader;

public class BihuApplication extends Application{
    private boolean isNightMode=false;
    private ImageDownLoader loader;
    private static BihuApplication app;
    @Override
    public void onCreate() {
        app=this;
        super.onCreate();
    }
    public synchronized static BihuApplication getApp(){
        return app;
    }
    /**得到imagedownloader的单例*/
//    public ImageDownLoader getImageDownLoaderInstance(){
//        if (loader==null){
//            loader=new ImageDownLoader(this);
//            return loader;
//        }else{
//            return loader;
//        }
//    }
    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
    }
}
