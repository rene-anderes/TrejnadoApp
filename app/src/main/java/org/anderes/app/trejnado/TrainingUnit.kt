package org.anderes.app.trejnado

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class TrainingUnit {

    var machine: TrainingMachine? = null
    var weight: Int = 0
    var duration: Int = 0

    constructor()
}
