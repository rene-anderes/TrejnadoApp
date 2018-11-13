package org.anderes.app.trejnado

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.*

@IgnoreExtraProperties
class TrainingSession {

    var trainingDate: Long = 0
    var units: List<TrainingUnit> = emptyList()
    var id: String = UUID.randomUUID().toString()
    var done: Boolean = false

    constructor()

    @Exclude
    fun getTrainingDateAsFormattedString(): String {
        val format = SimpleDateFormat("dd.MM.yyy")
        return format.format(trainingDate)
    }

    @Exclude
    fun addUnit(unit: TrainingUnit) {
        units = units.plus(unit)
    }
}
