package com.adamparys.servicejobmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.adamparys.servicejobmanager.data.model.JobContent
import com.adamparys.servicejobmanager.data.model.JobItem
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.android.synthetic.main.activity_job_list.*
import kotlinx.android.synthetic.main.job_list.*
import kotlinx.android.synthetic.main.job_list_content.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [JobDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class JobListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (job_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }


        setupRecyclerView(job_list)
        loadData()
    }

    private fun loadData() {

        val query = ParseQuery<JobItem>("JobItem")
        query.findInBackground { jobList, e ->
            if (e == null) {
                for (job in jobList) {
                    JobContent.addItem(job)
                }
                    job_list.adapter?.notifyDataSetChanged()
            } else { // handle Parse Exception here

            }
        }
    }
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, JobContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: JobListActivity,
        private val values: List<JobItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as JobItem
                if (twoPane) {
                    val fragment = JobDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(JobDetailFragment.ARG_ITEM_ID, item.objectId)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.job_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, JobDetailActivity::class.java).apply {
                        putExtra(JobDetailFragment.ARG_ITEM_ID, item.objectId)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.job_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]

            holder.itemNum.text = position.toString()
            holder.idView.text = item.date + " " + item.arrivalWindowBegin
            holder.contentView.text = item.city

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val itemNum: TextView = view.number_text
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }


/*
    I'm working with RecyclerView and both the remove and the update work well.
    1) REMOVE: There are 4 steps to remove an item from a RecyclerView
    list.remove(position);
    recycler.removeViewAt(position);
    mAdapter.notifyItemRemoved(position);
    mAdapter.notifyItemRangeChanged(position, list.size());
    These line of codes work for me.
    2) UPDATE THE DATA: The only things I had to do is
    mAdapter.notifyDataSetChanged();
    You had to do all of this in the Actvity/Fragment code not in the RecyclerView Adapter code.

 */
}
