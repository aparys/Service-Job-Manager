package com.adamparys.servicejobmanager

import android.app.Application
import android.util.Log
import com.adamparys.servicejobmanager.data.model.JobItem
import com.parse.*

class ParseStarterApp : Application() {


    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(JobItem::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parse_app_id))
                .clientKey(getString(R.string.parse_client_key))
                .server(getString(R.string.parse_server_url))
                .enableLocalDataStore()
                .build()
        )
        ParseInstallation.getCurrentInstallation().saveInBackground { ex ->
            if (ex == null) {
                Log.i("Parse Result", "Put installation Successful!")
            } else {
                Log.i("Parse Result", "Put installation Failed $ex")
                ex.printStackTrace()
            }
        }
        ParseUser.enableAutomaticUser()
        val defaultACL = ParseACL()
        defaultACL.publicReadAccess = true
        defaultACL.publicWriteAccess = true
        ParseACL.setDefaultACL(defaultACL, true)
    }
}