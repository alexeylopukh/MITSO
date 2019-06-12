package com.schedule.mitso

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RWadapter(private val subjList: ArrayList<SubjModel>?) : RecyclerView.Adapter<RWadapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RWadapter.ViewHolder {
        var view: View? = null
        try {
            view = LayoutInflater.from(p0.context).inflate(R.layout.rw_item, p0, false)
        }catch (e: Exception){}

        return ViewHolder(view!!)
    }
    override fun getItemCount(): Int {
        return subjList?.size ?: -1
    }
    override fun onBindViewHolder(p0: RWadapter.ViewHolder, p1: Int) {
        p0.timeSh.text = Converter.time(subjList!![p1].time!!)
        p0.subjectSh.text = subjList[p1].subject
        p0.classSh.text = subjList[p1].classRoom!!
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val timeSh = itemView.findViewById<TextView>(R.id.timeShow)
        val subjectSh = itemView.findViewById<TextView>(R.id.subjectShow)
        val classSh = itemView.findViewById<TextView>(R.id.classShow)
    }
}