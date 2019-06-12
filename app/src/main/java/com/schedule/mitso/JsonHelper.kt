package com.schedule.mitso

import android.annotation.SuppressLint
import android.content.Context
import java.io.BufferedReader
import java.io.File

class JsonHelper{
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        fun setContext(context: Context){
            this.context = context
        }

        fun write(json: String, filename: String): Boolean{
            return try {
            val path = context!!.filesDir
            val file = File(path, filename)
                file.createNewFile()
                file.writeText(json)
                true
            }catch (e: Exception){
                false
            }
        }

        fun read(filename: String): String{
            return try{
            val path = context!!.filesDir
            val file = File(path, filename)
                val bufferedReader: BufferedReader = file.bufferedReader()
                val json = bufferedReader.use { it.readText() }
                return json
            }catch (e: Exception){
                "[]"
            }
        }
    }
}