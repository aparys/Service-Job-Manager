package com.adamparys.servicejobmanager

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_job_detail.*


/**
 * An activity representing a single Job detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [JobListActivity].
 */
class JobDetailActivity : AppCompatActivity() {

    private val editJobFragment = EditJobFragment()
    private val jobDetailFragment = JobDetailFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)
        setSupportActionBar(detail_toolbar)

        createEditJobFab(savedInstanceState)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            jobDetailFragment.apply {
                arguments = Bundle().apply {
                    putString(
                        JobDetailFragment.ARG_ITEM_ID,
                        intent.getStringExtra(JobDetailFragment.ARG_ITEM_ID)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.job_detail_container, jobDetailFragment)
                .commit()
        }
    }

    private fun createEditJobFab(savedInstanceState: Bundle?) {
        //opens EditJobFragment from fab
        editJobFab.setOnClickListener { view ->
            Snackbar.make(view, "Edit Job", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                editJobFragment.apply {
                    arguments = Bundle().apply {
                        putString(
                            EditJobFragment.ARG_ITEM_ID,
                            intent.getStringExtra(EditJobFragment.ARG_ITEM_ID)
                        )
                    }
                }
                val fragment = supportFragmentManager.findFragmentById(R.id.job_detail_container)
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.job_detail_container, editJobFragment)
                        .commit()

                    editJobFab.hide()
                    saveJobFab.show()
                    cancelFab.show()
                }
            }
        }
        cancelFab.setOnClickListener {view ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.job_detail_container, jobDetailFragment)
                .commit()
            editJobFab.show()
            saveJobFab.hide()
            cancelFab.hide()
            editJobFragment.cancelChanges(view)
        }
        saveJobFab.setOnClickListener {view ->
            var saveSuccessful = true
            try {
                editJobFragment.saveChanges(view)
            } catch (e: Exception) {

                Toast.makeText(applicationContext, "Error Saving Job", Toast.LENGTH_SHORT).show()
                saveSuccessful = false
            }
            if (saveSuccessful)
                Snackbar.make(view, "Job Saved", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            else
                Snackbar.make(view, "Error saving job", Snackbar.LENGTH_LONG).setAction(
                    "Action",
                    null
                ).show()
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                jobDetailFragment.apply {
                    arguments = Bundle().apply {
                        putString(
                            JobDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(JobDetailFragment.ARG_ITEM_ID)
                        )
                    }
                }

                supportFragmentManager.beginTransaction()
                    .replace(R.id.job_detail_container, jobDetailFragment)
                    .commit()
                editJobFab.show()
                saveJobFab.hide()
                cancelFab.hide()
            }
        }
    }




    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back

            navigateUpTo(Intent(this, JobListActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


}
