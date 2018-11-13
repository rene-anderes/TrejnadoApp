package org.anderes.app.trejnado

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class TrainingUnit {

    lateinit var machine: TrainingMachine
    var weight: String = Constants.NO_DATA
    var duration: String = Constants.NO_DATA

    constructor()
}
