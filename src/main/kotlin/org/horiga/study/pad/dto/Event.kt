package org.horiga.study.pad.dto


data class Event(
        val name: String, // Name of PaD guerrilla event.
        val group: String, // Group of user, A,B,C,D,E
        val hours: List<String> // Hour 1, 15:30
) {
    var notified: Boolean = false
}



