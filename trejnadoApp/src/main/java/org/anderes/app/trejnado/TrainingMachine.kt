package org.anderes.app.trejnado

import com.google.firebase.database.IgnoreExtraProperties
import kotlin.Int.Companion.MIN_VALUE

@IgnoreExtraProperties
class TrainingMachine : Comparable<Int> {

    var name: String? = null
    var sequenceNo: Int = MIN_VALUE
    var settings: Map<String, String> = emptyMap()

    constructor()
    constructor(name : String, sequenceNo : Int, settings : Map<String, String>) {
        this.name = name
        this.sequenceNo = sequenceNo
        this.settings = settings
    }

    override fun compareTo(other: Int): Int {
        return sequenceNo.compareTo(other)
    }
}