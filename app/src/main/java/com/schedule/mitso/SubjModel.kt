package com.schedule.mitso

class SubjModel {
    var time: String? = null
    var subject: String? = null
    var classRoom: String? = null

    constructor(time: String?, subject: String?, classRoom: String?) {
        if (subject != "Нет занятий"){
            this.time = time
            this.subject = subject
            this.classRoom = classRoom
        }
    }
}