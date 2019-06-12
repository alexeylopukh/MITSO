package com.schedule.mitso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds




class ScheduleFragment : Fragment(){

    var subjList: ArrayList<SubjModel>? = ArrayList()

    companion object {
        fun newInstance(day: DayModel): ScheduleFragment {
            val fragmentFirst = ScheduleFragment()
            val args = Bundle()
            args.putString("date", day.date)
            args.putString("day", day.day)
            args.putStringArray("time", day.time)
            args.putStringArray("subject", day.subject)
            args.putStringArray("classroom", day.classroom)
            fragmentFirst.arguments = args
            return fragmentFirst
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val time = arguments!!.getStringArray("time")
        val subject = arguments!!.getStringArray("subject")
        val classroom = arguments!!.getStringArray("classroom")
        for (i in 0 until time!!.size){
            val currentSubj = SubjModel(time[i], subject[i], classroom[i])
            if (currentSubj.time != null){subjList?.add(currentSubj)}
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)
        val rw = view.findViewById<RecyclerView>(R.id.rw)

        MobileAds.initialize(this.context, "ca-app-pub-4272857293285960~2112230356");

       val mAdView = view.findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)



        if (subjList.isNullOrEmpty()){
            val emptyView = view.findViewById<TextView>(R.id.empty_view)
            emptyView.visibility = View.VISIBLE
        }
        else{
            val adapter = RWadapter(subjList)
            rw.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            rw.adapter = adapter
        }
        return view
    }
}