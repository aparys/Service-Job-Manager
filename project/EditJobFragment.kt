package com.adamparys.servicejobmanager

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_job_detail.*
import java.util.*
import android.app.TimePickerDialog
import java.text.SimpleDateFormat
import android.widget.ArrayAdapter
import com.adamparys.servicejobmanager.data.model.*
import kotlinx.android.synthetic.main.fragment_edit_job.*
import kotlinx.android.synthetic.main.fragment_edit_job.view.*
import kotlinx.android.synthetic.main.nav_header_account.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EditJobFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EditJobFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class EditJobFragment : Fragment() {

    private var rootView: View? = null
    private var oldItem: JobItem? = null
    private var newItem: JobItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                oldItem = JobContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                newItem = JobItem(oldItem)
                activity?.toolbar_layout?.title = "Job Details"
            }
        }
    }

     private fun addButtonClickListener() {
         rootView?.let { rootView ->
             rootView.selectDateButton.setOnClickListener { v ->
                 val datePick = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                     val cal = Calendar.getInstance()
                     cal.set(Calendar.YEAR, year)
                     cal.set(Calendar.MONTH, month)
                     cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                     val date = JobContent.dateFormat(cal.time)
                     selectDateButton.text = date
                     newItem?.date = date
                 }
                 val cal = Calendar.getInstance()
                 cal.time = JobContent.dateParse(newItem?.date)
                 DatePickerDialog(
                     v.context,
                     datePick,
                     cal.get(Calendar.YEAR),
                     cal.get(Calendar.MONTH),
                     cal.get(Calendar.DAY_OF_MONTH)
                 ).show()
             }

             val latestTimePickerListener =
                 TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
                     val cal = Calendar.getInstance()
                     cal.time = JobContent.timeParse(newItem?.arrivalWindowEnd)
                     cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                     cal.set(Calendar.MINUTE, minutes)
                     newItem?.arrivalWindowEnd = JobContent.hourFormat(cal.time)
                     var pattern = "h:mm a"
                     val simpleDateFormat = SimpleDateFormat(pattern, Locale("en", "US"))
                     latestArrivalButton.text = simpleDateFormat.format(oldItem?.arrivalWindowEnd)
                 }
             val etaTimePickerListener =
                 TimePickerDialog.OnTimeSetListener { view, hourOfDay, minutes ->
                     val cal = Calendar.getInstance()
                     cal.time = JobContent.timeParse(newItem?.eta)
                     cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                     cal.set(Calendar.MINUTE, minutes)
                     newItem?.eta = JobContent.hourFormat(cal.time)
                     var pattern = "h:mm a"
                     val simpleDateFormat = SimpleDateFormat(pattern, Locale("en", "US"))
                     etaButton.text = simpleDateFormat.format(newItem?.eta)
                 }
             val earliestTimePickerListener =
                 TimePickerDialog.OnTimeSetListener { view, hourOfDay, minutes ->
                     val cal = Calendar.getInstance()
                     cal.time = JobContent.timeParse(newItem?.arrivalWindowBegin)
                     cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                     cal.set(Calendar.MINUTE, minutes)
                     newItem?.arrivalWindowBegin = JobContent.hourFormat(cal.time)
                     var pattern = "h:mm a"
                     val simpleDateFormat = SimpleDateFormat(pattern, Locale("en", "US"))
                     earliestArrivalButton.text =
                         simpleDateFormat.format(newItem?.arrivalWindowBegin)
                 }
             val earliestOnClickListener = View.OnClickListener {
                 val cal = Calendar.getInstance()
                 cal.time = JobContent.timeParse(newItem?.arrivalWindowBegin)
                 TimePickerDialog(
                     context,
                     earliestTimePickerListener,
                     cal.get(Calendar.HOUR_OF_DAY),
                     cal.get(Calendar.MINUTE),
                     false
                 ).show()
             }
             val latestOnClickListener = View.OnClickListener {
                 val cal = Calendar.getInstance()
                 cal.time = JobContent.timeParse(newItem?.arrivalWindowEnd)
                 TimePickerDialog(
                     context,
                     latestTimePickerListener,
                     cal.get(Calendar.HOUR_OF_DAY),
                     cal.get(Calendar.MINUTE),
                     false
                 ).show()
             }
             val etaOnClickListener = View.OnClickListener {
                 val cal = Calendar.getInstance()
                 cal.time = JobContent.timeParse(newItem?.eta)
                 TimePickerDialog(
                     context,
                     etaTimePickerListener,
                     cal.get(Calendar.HOUR_OF_DAY),
                     cal.get(Calendar.MINUTE),
                     false
                 ).show()
             }
             rootView.earliestArrivalButton.setOnClickListener(earliestOnClickListener)
             rootView.latestArrivalButton.setOnClickListener(latestOnClickListener)
             rootView.etaButton.setOnClickListener(etaOnClickListener)
         }
     }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         rootView = inflater.inflate(R.layout.fragment_edit_job, container, false)
        var statusArray = (StatusDescription.values).toTypedArray()
        val adapter = ArrayAdapter(rootView!!.context, android.R.layout.simple_spinner_item, statusArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView!!.StatusSpinner.adapter = adapter
        addButtonClickListener()
        populateFields()
        return rootView
    }


    private fun populateFields() {
        rootView?.let { rootView ->
            oldItem?.let {
                rootView.streetEditText.setText(it.streetAddress)
                rootView.cityEditText.setText(it.city)
                rootView.stateEditText.setText(it.state)
                rootView.zipEditText.setText(it.zipCode)
                rootView.earliestArrivalButton.text = it.arrivalWindowBegin
                rootView.latestArrivalButton.text = it.arrivalWindowEnd
                rootView.etaButton.text = it.eta
                rootView.clientCompanyEditText.setText(it.companyName)
                rootView.customerEditText.setText(it.customerName)
                if (it.status != null)
                    rootView.StatusSpinner.setSelection(toStatus(it.status)!!.ordinal)
                else
                    rootView.StatusSpinner.setSelection(0)

                rootView.selectDateButton.text = it.date
                rootView.orderEditText.setText(it.orderNumber)
                rootView.phoneNumberEditText.setText(it.phoneNumber)
            }
        }
    }

    fun cancelChanges(rootView: View) {
        populateFields()
    }

    fun saveChanges(rootView: View) {
        newItem?.apply {
            streetAddress = streetEditText.text.toString()
            city = cityEditText.text.toString()
            state = stateEditText.text.toString()
            zipCode = zipEditText.text.toString()

            arrivalWindowBegin = earliestArrivalButton.text.toString()
            arrivalWindowEnd = latestArrivalButton.text.toString()
            eta = etaButton.text.toString()
            companyName = clientCompanyEditText.text.toString()
             customerName = customerEditText.text.toString()
//            status = rootView.StatusSpinner.textView.toString()

            date = selectDateButton.text.toString()
            orderNumber = orderEditText.text.toString()
            phoneNumber = phoneNumberEditText.text.toString()
        }
        oldItem?.copy(newItem)
        oldItem?.saveInBackground{e ->
            if (e == null){
//                Toast.makeText(context,"Job saved", Toast.LENGTH_LONG)
            }
            else{
//                Toast.makeText(context,"Failed to save changes", Toast.LENGTH_LONG)
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