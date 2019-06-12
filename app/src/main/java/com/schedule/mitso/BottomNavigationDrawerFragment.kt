package com.schedule.mitso

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        val name = view.findViewById<TextView>(R.id.name)
        val balance = view.findViewById<TextView>(R.id.balance)
        val lastUdp = view.findViewById<TextView>(R.id.last_upd)
        val choiceGroup = view.findViewById<TextView>(R.id.choice_group)
        if (Auth.group != ""){
            choiceGroup.visibility = View.VISIBLE
            choiceGroup.text = Auth.group
        }

        if (Auth.isAuth()){
            name.text = Auth.current.name
            balance.text = "Баланс: "+Auth.current.balance
            lastUdp.text = Auth.current.lastUdp
        } else {
            name.text = "Пользователь"
            balance.text = "не авторизован"
            lastUdp.text = ""
        }

        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_change -> {
                    JsonHelper.write("[]", SCHEDULE_FILE)
                    val intent = Intent()
                    intent.setClass(view.context, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_log_out -> {
                    JsonHelper.write("[]", AUTH_FILE)
                    val intent = Intent()
                    intent.setClass(view.context, LoginActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_to_web -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mitso.by"))
                    startActivity(browserIntent)
                }
                R.id.nav_bag_report -> {
                    val toast = Toast.makeText(view.context,"Скоро!",Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            true
        }
        return view
    }
}