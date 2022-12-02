package com.adamparys.servicejobmanager.data.model

import java.util.*

data class WorkerSchedual(
    val workerId: String? = "",
    val currentJobId : String? = "",
    val previousJobId : String? = "",
    val nextJobId : String? = "",
    val id : String?,
    val timeStarted : Date,
    val timeEnded : Date
)