package com.nowy.baselib.app

import android.app.Application

/**
 *
 * @Package:        com.nowy.baselib.app
 * @ClassName:      BaseApp
 * @Description:    java类作用描述
 * @Author:         Nowy
 * @CreateDate:     2019/5/22 15:46
 * @UpdateUser:     Nowy
 * @UpdateDate:     2019/5/22 15:46
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class BaseApp : Application(){
    val TAG = "BaseApp"

    override fun onCreate() {
        super.onCreate()
    }


    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }



}