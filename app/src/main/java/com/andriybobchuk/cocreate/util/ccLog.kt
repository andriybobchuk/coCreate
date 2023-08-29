package com.andriybobchuk.cocreate.util

import android.util.Log
import com.andriybobchuk.cocreate.BuildConfig

object ccLog {

    // Set this flag to true for enabling logging in debug builds
    private val isDebugBuild = BuildConfig.DEBUG

//    private fun getTag(): String {
//        val stackTrace = Thread.currentThread().stackTrace
//        for (element in stackTrace) {
//            if (element.className != ccLog::class.java.name) {
//                return "[${element.className.substringAfterLast('.')}.${element.methodName}]"
//            }
//        }
//        return "[ccLog]" // Fallback tag
//    }
//
//    fun v(message: String?) {
//        if(message.isNullOrEmpty()) {
//            Log.v(getTag(), "Empty Message")
//            return
//        }
//        Log.v(getTag(), message)
//    }

    fun d(className: String?, message: String?) {
        if (isDebugBuild) {
            if(message.isNullOrEmpty() || className.isNullOrEmpty()) {
                Log.d(className, "Empty Message or Tag")
                return
            }
            Log.d(className, message)
        }
    }

//    fun i(message: String?) {
//        if(message.isNullOrEmpty()) {
//            Log.i(getTag(), "Empty Message")
//            return
//        }
//        Log.i(getTag(), message)
//    }
//
//    fun w(message: String?) {
//        if(message.isNullOrEmpty()) {
//            Log.w(getTag(), "Empty Message")
//            return
//        }
//        Log.w(getTag(), message)
//
//    }

    fun e(className: String?, message: String?) {
        if(message.isNullOrEmpty()  || className.isNullOrEmpty()) {
            Log.e(className, "Empty Message")
            return
        }
        Log.e(className, message)
    }
}
