package com.vk.wmjsams

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.os.Looper
import android.util.Log

@SuppressLint("SpecifyJobSchedulerIdRange")
class MyJobService:JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {

        // do background work here ...
        Log.e("vibhav","started working ")
        // Simulate background work
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d("MyJobService", "Job finished")
            jobFinished(params, false) // Mark job as finished
        }, 5000L) // 5 seconds delay
        jobFinished(params,true)
       return false;
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.e("vibhav","stopped  working ")
        return true;
    }
}