package org.anderes.app.trejnado

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat

@IgnoreExtraProperties
class TrainingProgram {

    var key: String? = null
    var name: String? = null
    var createDate: Long = 0
    var machines: List<TrainingMachine> = emptyList()
    var sessions: List<TrainingSession> = emptyList();

    constructor()
    constructor(name : String, createDate : Long, machines : List<TrainingMachine>) {
        this.name = name;
        this.createDate = createDate
        this.machines = machines
    }

    @Exclude
    fun getCreateDateAsFormattedString(): String {
        val format = SimpleDateFormat("dd.MM.yyy")
        return format.format(createDate)
    }

    @Exclude
    fun findSessionById(sessionId: String): TrainingSession? {
        return sessions.find { session -> session.id.equals(sessionId) }
    }

    @Exclude
    fun addSession(newSession: TrainingSession) {
        sessions = sessions.plus(newSession)
    }
}