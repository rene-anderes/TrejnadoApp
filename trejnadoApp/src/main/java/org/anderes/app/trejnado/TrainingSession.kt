package org.anderes.app.trejnado

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class TrainingSession {

    var trainingDate: Long = 0
    var units: List<TrainingUnit> = emptyList();

    constructor()
}
