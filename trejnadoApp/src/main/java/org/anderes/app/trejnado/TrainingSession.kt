package org.anderes.app.trejnado

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class TrainingSession {

    var trainingDate: Long = 0
    var units: List<TrainingUnit> = emptyList()
    var id: String = UUID.randomUUID().toString()
    var done: Boolean = false

    constructor()
}
