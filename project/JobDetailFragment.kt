package com.adamparys.servicejobmanager

import android.Manifest
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.adamparys.servicejobmanager.data.model.JobContent
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.job_detail.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat.checkSelfPermission
import com.adamparys.servicejobmanager.data.model.JobItem
import com.adamparys.servicejobmanager.data.model.JobStatus
import com.adamparys.servicejobmanager.data.model.StatusDescription


/**
 * A fragment representing a single Job detail screen.
 * This fragment is either contained in a [JobListActivity]
 * in two-pane mode (on tablets) or a [JobDetailActivity]
 * on handsets.
 */
class JobDetailFragment : Fragment() {

    /**
     * The job content this fragment is presenting.
     */
    private var item: JobItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = JobContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]

                activity?.toolbar_layout?.title = "Job Details"
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.job_detail, container, false)
        setButtonListeners(rootView)


        // Show the dummy content as text in a TextView.
        item?.let {
            /*
            //formats the date
            var pattern = "MMM d"
            val simpleDateFormat = SimpleDateFormat(pattern, Locale("en", "US"))
            var date = simpleDateFormat.format(item?.arrivalWindowBegin)
            //formats the time
            pattern = "h:mm a"
            simpleDateFormat.applyPattern(pattern)
            val windowOfArrival = simpleDateFormat.format(item?.arrivalWindowBegin) +
                    " - " + simpleDateFormat.format(item?.arrivalWindowEnd)
            //formats the address
            val address = item?.streetAddress + ", \n" +
                    item?.city + " " +
                    item?.state + ", " +
                    item?.zipCode

             */

            rootView.windowOfArrivalTextView.text = "Schedualed date: \n" + it.date
            rootView.editEtaTextView.text = item?.arrivalWindowBegin + "-" + it.arrivalWindowEnd
            var addressString: String = ""
            if (!item?.streetAddress.isNullOrBlank())
                addressString+= item?.streetAddress + "\n"
            if (!item?.city.isNullOrBlank())
                addressString+= ", " + item?.city
            if (!item?.state.isNullOrBlank())
                addressString+= ", " + item?.state
            if (!item?.zipCode.isNullOrBlank())
                addressString+= ", " + item?.zipCode

            rootView.addressTextView.text = addressString
            rootView.ClientCompanyextView.text = it.companyName
            rootView.customerTextView.text = it.customerName
            rootView.statusTextView.text = it.status
            rootView.editEtaTextView.text =
                "Estimated time of arrival: \n" + it.eta
            rootView.orderNumberTextView.text = "Order number: \n" + it.orderNumber

            rootView.timeEndedTextView.text = "Job Started on:\n" + it.timeEnded
        }
        return rootView
    }

    private fun setButtonListeners(rootView: View) {
        setStartJobBtnListener(rootView)
        setJobDoneBtnListener(rootView)
        setCallCustomerBtnListener(rootView)
        setGetDirectionsBtnListener(rootView)
    }

    private fun setGetDirectionsBtnListener(rootView: View) {
        rootView.showInMapBtn.setOnClickListener { v ->
            if(item != null) {
                //builds the address string for google maps
                var addressString: String = "geo:0,0?q="
                if (!item?.streetAddress.isNullOrBlank())
                    addressString+= item?.streetAddress
                if (!item?.city.isNullOrBlank())
                    addressString+= ", " + item?.city
                if (!item?.state.isNullOrBlank())
                    addressString+= ", " + item?.state
                if (!item?.zipCode.isNullOrBlank())
                    addressString+= ", " + item?.zipCode

                val gmmIntentUri = Uri.parse(addressString)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun setCallCustomerBtnListener(rootView: View) {
        rootView.callCustomerBtn.setOnClickListener { v ->
            // Here, thisActivity is the current activity
            if (checkSelfPermission(v.context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_DENIED
            ) {
                // Permission is not granted
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
            } else {
                // Permission has already been granted
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${item?.phoneNumber}")
                startActivity(callIntent)
            }
        }
    }

    private fun setJobDoneBtnListener(rootView: View) {
        rootView.jobDoneButn.setOnClickListener { v ->
            if (item?.timeEnded != null) {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(v.context)
                dialogBuilder.setMessage("Job was already ended. You can change the end time in the edit screen")
                val alert = dialogBuilder.create()
                alert.setTitle("Job already ended")
                alert.show()
                var player: MediaPlayer = MediaPlayer.create(v.context, R.raw.computer_error)
                player.start()

            } else if(item?.timeStarted == null) {
                val dialogBuilder = AlertDialog.Builder(v.context)
                dialogBuilder.setMessage("Please Start the job first before you can end it")
                val alert = dialogBuilder.create()
                alert.setTitle("Job not started yet")
                alert.show()
                var player: MediaPlayer = MediaPlayer.create(v.context, R.raw.computer_error)
                player.start()
            }else{
                item?.timeEnded = JobContent.hourFormat(Calendar.getInstance().time)
                Toast.makeText(v.context, "started", Toast.LENGTH_LONG).show()
                val date = item?.timeEnded
                rootView.timeEndedTextView.text = date
                item?.status = StatusDescription[JobStatus.Canceled]
                rootView.statusTextView?.text =
                    StatusDescription[JobStatus.Completed]
            }
        }
    }
    private fun setStartJobBtnListener(rootView: View) {
        rootView.startJobBtn.setOnClickListener { v ->
            if (item?.timeStarted != null) {
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(v.context)
                dialogBuilder.setMessage("Job was already started. You can change the start time in the edit screen")
                val alert = dialogBuilder.create()
                alert.setTitle("Job already started")
                alert.show()
                var player: MediaPlayer = MediaPlayer.create(v.context,R.raw.computer_error)
                player.start()

            } else {
                item?.timeStarted = JobContent.hourFormat(Calendar.getInstance().time)
                Toast.makeText(v.context, "started", Toast.LENGTH_LONG).show()
                rootView.timeStartedTextView.text = item?.timeStarted
                item?.status = StatusDescription[JobStatus.InProgress]
                rootView.statusTextView?.text = StatusDescription[JobStatus.InProgress]
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
