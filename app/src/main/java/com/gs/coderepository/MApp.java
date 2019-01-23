package com.gs.coderepository;

import android.app.Application;

/**
 * Created by 13203 on 2019-01-23.
 */

public class MApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //未捕获异常处理搭配使用
    public MApp() {
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(new UnCeHandler(this));
    }

}
