package com.example.pbbdraft.javascriptinterface

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import com.example.pbbdraft.room.PBBDB


class KoordinateWebInterface(val mode:String, val appContext: Context){

    val db by lazy { PBBDB(appContext) }

    @JavascriptInterface
    fun getMapMode():String{
        return mode
    }
    @JavascriptInterface
    fun returnLatLngBlokValue(lat:String, lng:String, blok:String){
        Handler(Looper.getMainLooper()).post {
            db.PBBDao().updateLatLng(lat.toFloat(), lng.toFloat(), blok.toInt())
        }
        (appContext as Activity).finish()
    }
}