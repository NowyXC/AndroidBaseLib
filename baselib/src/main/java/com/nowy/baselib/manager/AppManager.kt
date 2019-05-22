package com.nowy.baselib.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.SystemClock
import java.util.*

/**
 *
 * @Package:        com.nowy.baselib.manager
 * @ClassName:      AppManager
 * @Description:    应用程序Activity管理类：用于Activity管理和应用程序退出
 *                   主要用于生命周期的管理
 * @Author:         Nowy
 * @CreateDate:     2019/5/22 15:54
 * @UpdateUser:     Nowy
 * @UpdateDate:     2019/5/22 15:54
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class AppManager private constructor(){

    fun size():Int{
        return sActivityStack?.size?:0
    }


    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity){
        if (sActivityStack == null) {
            sActivityStack = Collections.synchronizedList(LinkedList())
        }
        sActivityStack!!.add(activity)
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun getCurrentActivity() : Activity?{
        var activity : Activity? = null

        val stackSize = sActivityStack?.size?:0
        if(stackSize > 0){
            activity = sActivityStack!![stackSize - 1]
        }
        return activity
    }



    /**
     * 将activity移出activityStack
     */
    fun removeActivity(activity: Activity){
        val isRemove = sActivityStack!!.remove(activity)

    }

    /**
     * 关闭指定界面
     */
    fun finishActivity(activity: Activity?){
        if(activity != null){
            removeActivity(activity)
            activity.finish()
        }
    }



    /**
     * 关闭栈内所有指定className的activity
     */
    fun finishActivity(clazz: Class<out Activity>){
        sActivityStack!!
            .filter { it.javaClass == clazz }
            .forEach { finishActivity(it) }
    }



    /**
     * 清空堆栈中指定activity之上的activity
     */
    fun clearTopActivity(clazz: Class<out Activity>){
        var needFinish = false
        if(sActivityStack != null){
            needFinish = sActivityStack!!
                .any {
                    it.javaClass == clazz
                }
        }
        if(needFinish){
            for (i in sActivityStack!!.indices.reversed()) {
                val activity = sActivityStack!![i]
                if (activity.javaClass != clazz) {
                    finishActivity(activity)
                } else {
                    finishActivity(activity)
                    return
                }
            }
        }

    }


    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (i in sActivityStack!!.indices.reversed()) {
            val activity = sActivityStack!![i]
            finishActivity(activity)
        }
    }

    /**
     * 关闭指定类名之外的activity
     */
    fun finishOtherActivity(clazz: Class<out Activity>){
        if(sActivityStack != null){
            sActivityStack!!
                .filter { it.javaClass != clazz }
                .forEach { finishActivity(it) }
        }
    }

    /**
     * 关闭指定activity之外的activity
     */
    fun finishOtherActivity(activity: Activity){
        finishOtherActivity(activity.javaClass)
    }


    /**
     * 退出应用程序
     */
    fun appExit() {
        try {
            finishAllActivity()
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    companion object {
        private var sActivityStack : MutableList<Activity>? = Collections.synchronizedList(LinkedList())
        private val INSTANCE = AppManager()


        fun multiClickExitEvent(hits: LongArray): Boolean {
            // arraycopy 拷贝数组
            /*  参数解读如下：
         *  src the source array to copy the content.   拷贝哪个数组
         *  srcPos the starting index of the content in src.  从原数组中的哪里开始拷贝
         *  dst the destination array to copy the data into.  拷贝到哪个数组
         *  dstPos the starting index for the copied content in dst.  从目标数组的那个位置开始去写
         *  length the number of elements to be copied.  拷贝的长度
         */
            System.arraycopy(hits, 1, hits, 0, hits.size - 1)
            //获取离开机的时间
            hits[hits.size - 1] = SystemClock.uptimeMillis()
            //单击时间的间隔，以500毫秒为临界值
            if (hits[0] >= SystemClock.uptimeMillis() - 500) {
                //            mHits = null;
                //            mHits = new long[n];
                AppManager.INSTANCE.appExit()

                return true
            }

            return false
        }


    }
}