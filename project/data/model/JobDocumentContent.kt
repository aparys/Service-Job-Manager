package com.adamparys.servicejobmanager.data.model

import java.io.File
import java.util.ArrayList
import java.util.HashMap

object JobDocumentContent {
    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<JobDocumentContent.JobDocumentItem> = ArrayList()



    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, JobItem> = HashMap()
    data class JobDocumentItem (
        var id: String? ,
        var jobId: String?,
        var ownerId: String?,
        var fileType: String? ,
        var file: File)
}
