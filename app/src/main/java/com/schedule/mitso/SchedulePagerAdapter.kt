package com.schedule.mitso

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SchedulePagerAdapter(fragmentManager: FragmentManager, private var days: ArrayList<DayModel>):
    FragmentStatePagerAdapter(fragmentManager){

    override fun getItem(p0: Int): Fragment {
        return ScheduleFragment.newInstance(days[p0])
    }

    override fun getCount(): Int {
        return days.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return try{
            "${days[position].day}, ${days[position].date}"
        }catch (e: IndexOutOfBoundsException){
            null
        }
    }

    override fun getItemPosition(item: Any): Int {
        return POSITION_NONE
    }
}