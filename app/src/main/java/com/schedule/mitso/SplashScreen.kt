package com.schedule.mitso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        JsonHelper.setContext(baseContext)

        Auth.writeConfig(JsonHelper.read(AUTH_FILE))
        val sharedPreferences = getSharedPreferences("urls" ,MODE_PRIVATE)

        val url = sharedPreferences.getString("url", "unknow")
        if (url != "unknow"){
            val intent = Intent(this, PagesActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("weekURL", sharedPreferences.getString("weekURL", "unknow"))
            startActivity(intent)
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        }
    }
}