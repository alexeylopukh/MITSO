package com.schedule.mitso

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomappbar.BottomAppBar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import androidx.viewpager.widget.PagerTabStrip
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.TextView
import android.widget.Toast



class PagesActivity : AppCompatActivity() {

    private var schedule: DayList = DayList()
    private lateinit var progress: ProgressBar
    private lateinit var tabs: PagerTabStrip
    private lateinit var vp: ViewPager
    private lateinit var sPref: SharedPreferences
    private lateinit var pagerAdapter: SchedulePagerAdapter
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        tabs = findViewById(R.id.tabs)
        val bar = findViewById<BottomAppBar>(R.id.bar)
        setSupportActionBar(bar)
        sPref = getSharedPreferences("urls" ,MODE_PRIVATE)
        Auth.group = sPref.getString("group", "")
        vp = findViewById(R.id.vpPage)
        progress = findViewById(R.id.progress)
        val json = JsonHelper.read(SCHEDULE_FILE)
        if (json != "[]") {
            schedule = gson.fromJson(json, DayList::class.java)
            if (schedule.size != 0)
                tabs.visibility = View.VISIBLE
            pagerAdapter = SchedulePagerAdapter(supportFragmentManager, schedule)
            vp.adapter = pagerAdapter
        }
        goToCurrentDay()
        if (Parser.hasConnection(applicationContext)){
            progress.visibility = View.VISIBLE
            val url = sPref.getString("weekURL", "")
            updAuth()
            getWeeks(url)
        }
    }

    override fun onResume() {
        if (Parser.hasConnection(applicationContext)){
            progress.visibility = View.VISIBLE
            val url = sPref.getString("weekURL", "")
            updAuth()
            getWeeks(url)
        }
        super.onResume()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    private fun  updAuth(){
        doAsync {
            try {
                Parser.auth(Auth.current.login!!, Auth.current.pass!!)

            }catch (e: java.lang.Exception){}
            uiThread {
                if (Auth.current.balance.toFloat() < 0.0){
                    val toast = Toast.makeText(
                        applicationContext,
                        "У вас отрицательный баланс!", Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }
        }
    }

    private fun getWeeks(url: String) {
        doAsync {
            val weekList: ArrayList<String> = ArrayList()
            try {
                val doc: Document = Jsoup.connect(url).get()
                val options = doc.select("option")
                for (option in 0 until options.size) {
                    weekList.add(options[option].text())
                }
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext, e.toString(), Toast.LENGTH_SHORT
                )
                toast.show()
            }
            uiThread {

                val spinWeeks = findViewById<Spinner>(R.id.spin_weeks)
                val weekAdapter: ArrayAdapter<String> = ArrayAdapter(
                    this@PagesActivity, android.R.layout.simple_spinner_dropdown_item, weekList
                )
                spinWeeks.adapter = weekAdapter
                spinWeeks.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        try {
                            (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                        }catch (e: java.lang.Exception){}

                        progress.visibility = View.VISIBLE
                        var weekNumber = 0
                        if (weekList[position] != "Текущая неделя") {
                            val weekChar: String = weekList[position].toCharArray()[0].toString()
                            weekNumber = weekChar.toInt() - 1
                        }
                        val parsURL = sPref.getString("url", "")
                        updRasp(parsURL, weekNumber)
                    }
                }
            }
        }
    }

    private fun updRasp(url: String, week: Int) { //ToDo - reWrite
        val udpSchedule = DayList()
        doAsync {
            val doc: Document = Jsoup.connect(url + week).get()
            val divs = doc.select("div>div[class=rp-ras-data]")
            val days = doc.select("div>div[class=rp-ras-data2]")
            val mixRasp = doc.select("div>div[class=rp-ras-opis]")

            for (div in 0 until divs.size) {
                val date = divs[div].text()
                val day = days[div].text()
                val time = arrayOfNulls<String>(8)
                val obj = arrayOfNulls<String>(8)
                val cl = arrayOfNulls<String>(8)
                val times = mixRasp[div].select("div[class=rp-r-time]") //Массив время
                val objs = mixRasp[div].select("div[class=rp-r-op]") //массив Предметы
                val cls = mixRasp[div].select("div[class=rp-r-aud]")

                for (i in 0 until cls.size) {
                    cl[i] = cls[i].text()
                }

                for (ob in 0 until objs.size) {
                    time[ob] = times[ob].text()
                    obj[ob] = objs[ob].text()
                }
                udpSchedule.add(DayModel(date, day, time, obj, cl))
            }
            uiThread {
                if (udpSchedule != schedule){
                    schedule = udpSchedule
                    if (schedule.size != 0)
                        tabs.visibility = View.VISIBLE
                    else
                        tabs.visibility = View.INVISIBLE
                    pagerAdapter = SchedulePagerAdapter(supportFragmentManager, schedule)
                    vp.adapter = pagerAdapter
                    val json = listToJson(schedule)
                    JsonHelper.write(json, SCHEDULE_FILE)
                    goToCurrentDay()
                }
                progress.visibility = View.GONE
            }
        }

    }

    private fun listToJson(jsonList: ArrayList<DayModel>): String {
        return gson.toJson(jsonList)
    }

    private fun goToCurrentDay(){
        val currentTime = Calendar.getInstance().time
        val format = SimpleDateFormat("d")
        val currentDay = format.format(currentTime)
        var current = -1
        for (i in 0 until schedule.size) {
            val mix = schedule[i].date.toCharArray()
            try {
                val numb: String = mix[0].toString()+mix[1].toString()
                if (currentDay == numb || "$currentDay " == numb) {
                    current = i
                }
            }catch (e: java.lang.Exception){}
        }
        if (current == -1){
            vp.currentItem = 0
        }else vp.currentItem = current
    }

}
