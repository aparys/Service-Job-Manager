package com.adamparys.servicejobmanager.data.model

import android.location.Address
import android.util.Log
import com.adamparys.servicejobmanager.data.model.JobContent.addressFormat
import com.parse.GetCallback
import com.parse.ParseClassName
import com.parse.ParseObject
import com.parse.ParseQuery
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */



val StatusDescription = mapOf(
    JobStatus.NotStarted to "Not Started",
    JobStatus.Canceled to "Canceled",
    JobStatus.Completed to "Completed",
    JobStatus.FurtherActionRequired to "Further action required",
    JobStatus.InProgress to "In progress")
enum class JobStatus {
    NotStarted,
    Canceled,
    Completed,
    FurtherActionRequired,
    InProgress
}
object JobContent {

    /**
     * An array of sample (dummy) jobs.
     */
    val ITEMS: MutableList<JobItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, JobItem> = HashMap()

    private const val COUNT = 5
    fun hourFormat(date: Date?): String {
        return SimpleDateFormat("hh:mm a", Locale("en", "US")).format(date)
    }

    fun dateFormat(date: Date?): String {
        return SimpleDateFormat("MMM d, yyyy", Locale("en", "US")).format(date)
    }

    fun dateParse(date: String?): Date {
        return SimpleDateFormat("MMM d, yyyy", Locale("en", "US")).parse(date)
    }

    fun timeParse(time: String?): Date? {
        if (time == null)
            return null
        else
            return SimpleDateFormat("hh:mm a", Locale("en", "US")).parse(time)
    }

    fun dateTimeParse(date: String?, time: String?): Date {
        return SimpleDateFormat("HH:mm a | MMM d", Locale("en", "US")).parse(date + " " + time)
    }

    fun dateTimeFormat(date: Date?): String {
        return SimpleDateFormat("MMM d, yyyy", Locale("en", "US")).format(date)
    }

    fun addressFormat(street: String?, city: String?, state: String?, zip: String?): Address {
        val address = Address(Locale.US)
        address.setAddressLine(0, street)
        address.subAdminArea = city
        address.adminArea = state
        address.postalCode = zip
        return address
    }
/*
    init {
        // Add some sample items.
        for (b in 1..COUNT) {
            addItem(createDummyItem(b))
            var i = b - 1
            var job = ParseObject("JobItem")
            job.put("customerName", ITEMS[i].customerName!!)
            job.put("companyName", ITEMS[i].companyName!!)
            job.put("streetAdress", ITEMS[i].address!!.getAddressLine(0))
            job.put("city", ITEMS[i].address!!.subAdminArea)
            job.put("state", ITEMS[i].address!!.adminArea)
            job.put("zipCode", ITEMS[i].address!!.postalCode)
            job.put("phoneNumber", ITEMS[i].phoneNumber!!)
            job.put("jobType", ITEMS[i].jobType!!)
            job.put("info", ITEMS[i].info!!)
            job.put("orderNumber", ITEMS[i].orderNumber!!)
            job.put("status", StatusDescription[ITEMS[i].status]!!)
            job.put("arrivalWindowBegin", hourFormat(ITEMS[i].arrivalWindowBegin!!))
            job.put("arrivalWindowEnd", hourFormat(ITEMS[i].arrivalWindowEnd!!))
            job.put("eta", hourFormat(ITEMS[i].eta!!))
            job.put("date", dateFormat(ITEMS[i].arrivalWindowBegin!!))
            job.saveInBackground { e ->
                if (e == null) {
                    Log.i("save job", "succesfull")
                } else {
                    Log.i("save job", "failed")
                }
            }
        }
    }

 */

    internal fun addItem(item: JobItem) {
        ITEM_MAP[item.objectId] = item
        ITEMS.clear()
        ITEMS.addAll(ITEM_MAP.values)
    }
    /*
    internal fun addItem(job: JobItem) {
        ITEMS.add(job)
        ITEM_MAP[job.id] = job
        val item = JobItem(
            job.getString("customerName"),
            job.getString("companyName"),
            addressFormat(
                job.getString("streetAdress"),
                job.getString("city"),
                job.getString("state"),
                job.getString("zipCode")
            ),
            job.getString("phoneNumber"),
            job.getString("jobType"),
            job.getString("info"),
            job.getString("orderNumber"),
            job.objectId,
            toStatus(job.getString("status")),
            dateTimeParse(job.getString("date"), job.getString("arrivalWindowBegin")),
            timeParse(job.getString("arrivalWindowEnd")),
            timeParse(job.getString("eta")),
            timeParse(job.getString("timeStarted")),
            timeParse(job.getString("timeEnded"))
        )

     */
    }

    fun toStatus(string: String?): JobStatus? {
        for (i in StatusDescription.keys) {
            if (StatusDescription[i] == string)
                return i
        }
        return null
    }
/*
    private fun createDummyItem(position: Int): JobItem {
        //Generate a date for Jan. 9, 2013, 10:11:12 AM
        val cal = Calendar.getInstance()
        cal.set(
            2013,
            Calendar.JANUARY,
            9,
            10,
            0,
            0
        ) //Year, month, day of month, hours, minutes and seconds
        val arrivalWindowBegin = cal.time
        cal.add(Calendar.HOUR, 3)
        val arrivalWindowEnd = cal.time

        val address = addressFormat("214-05 15th ave", "Bayside", "NY", "11360")
        return JobItem(
            "Adam", "AP Fitness", address, "718-600-2965",
            "service", "", "1234", position.toString(), JobStatus.NotStarted,
            arrivalWindowBegin, arrivalWindowEnd, arrivalWindowBegin, null, null
        )
    }


 */


/*sample querries
    fun save(job: JobItem?) {
        val query = ParseQuery.getQuery(JobItem::class.java)
        query.findInBackground { objects, e ->
            if (e == null) {
                for (armor in objects) {
                    Log.d("DEBUG", armor.city)
                }
            } else {
                Log.d("score", "Error: " + e!!.message)
            }
        }
        // Retrieve the object by id
        query.getInBackground(job?.id, { job, e->
                if (e == null) { // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to your Parse Server. playerName hasn't changed.
                    job.put("score", 1338)
                    job.put("cheatMode", true)
                    job.saveInBackground()
                }
            }
        })
    }

 */

    /**
     * A dummy item representing a piece of content.
     */


    @ParseClassName("JobItem")
    class JobItem() : ParseObject() {
        var customerName: String?
            get() = getString("customerName")
            set(value) {
                if (value != null) put("customerName", value)
                else put("customerName", "")
            }
        var streetAddress: String?
            get() = getString("streetAddress")
            set(value) {
                if (value != null) put("streetAddress", value)
                else put("streetAddress", "")
            }
        var city: String?
            get() = getString("city")
            set(value) {
                if (value != null) put("city", value)
                else put("city", "")
            }
        var state: String?
            get() = getString("state")
            set(value) {
                if (value != null) put("state", value)
                else put("state", "")
            }
        var zipCode: String?
            get() = getString("zipCode")
            set(value) {
                if (value != null) put("zipCode", value)
                else put("zipCode", "")
            }
        var companyName: String?
            get() = getString("companyName")
            set(value) {
                if (value != null) put("companyName", value)
                else put("companyName", "")
            }
        var phoneNumber: String?
            get() = getString("phoneNumber")
            set(value) {
                if (value != null) put("phoneNumber", value)
                else put("phoneNumber", "")
            }
        var jobType: String?
            get() = getString("jobType")
            set(value) {
                if (value != null) put("jobType", value)
                else put("jobType", "")
            }
        var info: String?
            get() = getString("info")
            set(value) {
                if (value != null) put("info", value)
                else put("info", "")
            }
        var orderNumber: String?
            get() = getString("orderNumber")
            set(value) {
                if (value != null) put("orderNumber", value)
                else put("orderNumber", "")
            }
        var status: String?
            get() = getString("status")
            set(value) {
                if (value != null) put("status", value)
                else put("status", "")
            }
        var arrivalWindowBegin: String?
            get() = getString("arrivalWindowBegin")
            set(value) {
                if (value != null) put("arrivalWindowBegin", value)
                else put("arrivalWindowBegin", "")
            }
        var arrivalWindowEnd: String?
            get() = getString("arrivalWindowEnd")
            set(value) {
                if (value != null) put("arrivalWindowEnd", value)
                else put("arrivalWindowEnd", "")
            }
        var eta: String?
            get() = getString("eta")
            set(value) {
                if (value != null) put("eta", value)
                else put("eta", "")
            }
        var timeStarted: String?
            get() = getString("timeStarted")
            set(value) {
                if (value != null) put("timeStarted", value)
                else put("timeStarted", "")
            }
        var timeEnded: String?
            get() = getString("timeEnded")
            set(value) {
                if (value != null) put("timeEnded", value)
                else put("timeEnded", "")
            }
        var date: String?
            get() = getString("date")
            set(value) {
                if (value != null) put("date", value)
                else put("date", "")
            }

        fun copy(copy: JobItem?) {
            customerName = copy?.customerName
            companyName = copy?.companyName
            streetAddress = copy?.streetAddress
            city = copy?.city
            state = copy?.state
            zipCode = copy?.zipCode
            phoneNumber = copy?.phoneNumber
            jobType = copy?.jobType
            info = copy?.info
            orderNumber = copy?.orderNumber
            status = copy?.status
            arrivalWindowBegin = copy?.arrivalWindowBegin
            arrivalWindowEnd = copy?.arrivalWindowEnd
            eta = copy?.eta
            timeStarted = copy?.timeStarted
            timeEnded = copy?.timeEnded
            date = copy?.date
        }

        constructor(c: JobItem?) : this() {
            copy(c)
        }
    }