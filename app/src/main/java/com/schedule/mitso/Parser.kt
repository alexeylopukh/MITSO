package com.schedule.mitso

import android.content.Context
import android.net.ConnectivityManager
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class Parser{
    companion object {

        fun auth(login: String, pass: String): Boolean{
            try{
                val response = Jsoup.connect("https://student.mitso.by/login_stud.php")
                    .method(Connection.Method.POST)
                    .data("login", login)
                    .data("password", pass)
                    .execute()
                val doc = response.parse()
                val tableTdList = doc.select("table td")
                val balance = tableTdList[1].text().toFloat()
                val mainDebt = tableTdList[3].text().toFloat()
                val percent = tableTdList[5].text().toFloat()
                val name = doc.select("div[class=topmenu]").text()
                val currentTime = Calendar.getInstance().time
                val format = SimpleDateFormat("'Последнее обновление' dd.MM.yyyy")
                val currentDay = format.format(currentTime)
                Auth.current = Auth(login, pass, name, balance, mainDebt, percent, currentDay)
                JsonHelper.write(Auth.readConfig(), AUTH_FILE)
                return true
            }catch (e: Exception){
                return false
            }
        }

        fun hasConnection(context: Context): Boolean {
            val cm = context.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            val wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (wifiNetwork != null && wifiNetwork.isConnected) {
                return true
            }

            val mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            if (mobileNetwork != null && mobileNetwork.isConnected) {
                return true
            }

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected

        }
    }
}