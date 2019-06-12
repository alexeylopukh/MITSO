package com.schedule.mitso

class DayModel(
    var date: String,
    var day: String,
    var time: Array<String?>,
    var subject: Array<String?>,
    classroom: Array<String?>
) {
    var classroom: Array<String?>

    init {
        val newClassroom: Array<String?> = arrayOfNulls(subject.size)
        var last = 0
        for (i in 0 until subject.size) { //ToDo reWrite. Add "if" when create object
            if (!subject[i].equals("Нет занятий")) {
                newClassroom[i] = classroom[last]
                last++
            }
        }
        this.classroom = newClassroom
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as DayModel

        if (date != other.date) return false
        if (day != other.day) return false
        if (!time.contentEquals(other.time)) return false
        if (!subject.contentEquals(other.subject)) return false
        if (!classroom.contentEquals(other.classroom)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + day.hashCode()
        result = 31 * result + time.contentHashCode()
        result = 31 * result + subject.contentHashCode()
        result = 31 * result + classroom.contentHashCode()
        return result
    }


}