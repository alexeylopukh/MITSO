package com.schedule.mitso

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var selectFaculty: String = ""
    private var selectForm: String = ""
    private var selectCours: Int = -1
    private var selectGroup: String = ""
    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val linerLayout = findViewById<LinearLayout>(R.id.linerSpinner)
        if (!Parser.hasConnection(applicationContext)){
            Snackbar.make(linerLayout, "Проверьте интернет-соединение", Snackbar.LENGTH_LONG).show()
        }
        progress = findViewById<ProgressBar>(R.id.progressLine)
        progress.visibility = View.VISIBLE
        val facultyList = arrayOf("МЭОиМ", "Магистратура", "Юридический")
        val spinFaculty = findViewById<Spinner>(R.id.spin_faculty)
        val scheduleAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, R.layout.spinner_item, facultyList
        )
        spinFaculty.adapter = scheduleAdapter
        spinFaculty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectFaculty = facultyList[position]
                upgradeForm()
            }
        }

        val btnGo = findViewById<Button>(R.id.btn_go)
        btnGo.setOnClickListener {
            if (selectGroup != "") {
                val sharedPreferences = getSharedPreferences("urls" ,MODE_PRIVATE)
                val url = "https://www.mitso.by/schedule/${Converter.wordToLink(selectForm)}/" +
                        "${Converter.wordToLink(selectFaculty)}/$selectCours%20kurs/" +
                        "${Converter.wordToLink(selectGroup)}/"
                val weekURL = "https://mitso.by/schedule_update?type=date&kaf=Glavnaya+kafedra&form=" +
                        "${Converter.wordToLink(selectForm)}&fak=${Converter.wordToLink(selectFaculty)}" +
                        "&kurse=$selectCours+kurs&group_class="+Converter.wordToLink(selectGroup)
                                val editor = sharedPreferences.edit()
                editor.putString("url", url)
                editor.putString("weekURL", weekURL)
                editor.putString("group", selectGroup)
                editor.apply()
                lateinit var a: Auth
                val intent = if (Auth.current.name.isNullOrEmpty()){
                    Intent(this, LoginActivity::class.java)
                }else{
                    Intent(this, PagesActivity::class.java)
                }
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun upgradeForm() {
        doAsync {
            val facultyList: ArrayList<String> = ArrayList()
            try {
                val url = "https://www.mitso.by/schedule_update?type=form&kaf=Glavnaya+kafedra&fak=${Converter.wordToLink(selectFaculty)}"
                val doc: Document = Jsoup.connect(url).get()
                val options = doc.select("option")
                for (option in 0 until options.size) {
                    facultyList.add(options[option].text())
                }
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext, e.toString(), Toast.LENGTH_SHORT
                )
                toast.show()
            }
            uiThread {
                val spinForm = findViewById<Spinner>(R.id.spin_form)
                val formAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@MainActivity, R.layout.spinner_item, facultyList
                )
                spinForm.adapter = formAdapter
                spinForm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        progress.visibility = View.VISIBLE
                        selectForm = facultyList[position]
                        upgradeCours()
                    }
                }
            }
        }
    }

    fun upgradeCours() {
        doAsync {
            val coursList: ArrayList<String> = ArrayList()
            try {
                val url = "https://www.mitso.by/schedule_update?type=kurse&kaf=Glavnaya+kafedra&form=" +
                        "${Converter.wordToLink(selectForm)}&fak=${Converter.wordToLink(selectFaculty)}"
                val doc: Document = Jsoup.connect(url).get()
                val options = doc.select("option")
                for (option in 0 until options.size) {
                    coursList.add(options[option].text())
                }
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext, e.toString(), Toast.LENGTH_SHORT
                )
                toast.show()
            }
            uiThread {
                val spinCours = findViewById<Spinner>(R.id.spin_cours)
                val coursAdapter: ArrayAdapter<String> = ArrayAdapter(
                    this@MainActivity, R.layout.spinner_item, coursList)
                spinCours.adapter = coursAdapter
                spinCours.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        progress.visibility = View.VISIBLE
                        val selectStringCours = coursList[position].toCharArray()[0].toString()
                        selectCours = selectStringCours.toInt()
                        upgradeGroups()
                    }
                }
            }
        }
    }

    fun upgradeGroups() {
        doAsync {
            val groupList: ArrayList<String> = ArrayList()
            try {
                val url = "https://www.mitso.by/schedule_update?type=group_class&kaf=Glavnaya+kafedra&form=" +
                        "${Converter.wordToLink(selectForm)}&fak=${Converter.wordToLink(selectFaculty)}" +
                        "&kurse=$selectCours+kurs"
                val doc: Document = Jsoup.connect(url).get()
                val options = doc.select("option")
                groupList.clear()
                for (option in 0 until options.size) {
                    groupList.add(options[option].text())
                }
            } catch (e: Exception) {
                val toast = Toast.makeText(
                    applicationContext, e.toString(), Toast.LENGTH_SHORT
                )
                toast.show()
            }
            uiThread {
                progress.visibility = View.INVISIBLE
                val spinGroup = findViewById<Spinner>(R.id.spin_group)
                val groupAdapter: ArrayAdapter<String> = ArrayAdapter(
                    this@MainActivity, R.layout.spinner_item, groupList
                )
                spinGroup.adapter = groupAdapter
                spinGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectGroup = ""
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        //progress.visibility = View.VISIBLE
                        if (groupList.size != 0){
                        selectGroup = groupList[position]
                        }
                    }
                }
            }
        }
    }
}
