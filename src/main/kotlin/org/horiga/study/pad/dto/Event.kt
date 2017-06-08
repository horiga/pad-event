package org.horiga.study.pad.dto

data class Event(
        val message: String,
        val events: List<EventItem>)

data class EventItem(
        val name: String, // Name of PaD guerrilla event.
        val group: String, // Group of user, A,B,C,D,E
        val hours: List<String> // Hour 1, 15:30
) {
    fun readableMessage(): String {
        return "グループ $group => イベント「$name」の開始時間は、$hours"
    }
}



