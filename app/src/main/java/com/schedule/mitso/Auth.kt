package com.schedule.mitso

import com.google.gson.Gson
import java.lang.Exception

class Auth(
    var login: String?,
    var pass: String?,
    var name: String?,
    var balance: Float = 0.0f,
    var mainDebt: Float?,
    var percent: Float?,
    var lastUdp: String?
) {
    companion object {
        var group: String = ""
        val gson = Gson()
        var current = Auth(null, null, null, 0.0f, null, null, null)

        fun readConfig(): String{
            var result = "[]"
            try {
                result = gson.toJson(current)
            }catch (e: Exception){

            }
            return result
        }
        
        fun writeConfig(json: String){
            if (json != "[]") {
                current = gson.fromJson(json, Auth::class.java)
            }
        }

        fun isAuth(): Boolean{
            return !(current.login == null)
        }
    }
}

