package com.schedule.mitso




class DayList: ArrayList<DayModel>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (!super.equals(other)) return false
        return true
    }

}