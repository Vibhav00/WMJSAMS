package com.vk.wmjsams


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.vk.wmjsams.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var amb: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        amb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(amb.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        setWorkManager()     -> work manager
//        scheduleJob()        -> job scheduler
//        setDailyAlarm(this)  -> alarm manager

    }

    private fun scheduleJob() {
        val componentName = ComponentName(this,MyJobService::class.java)
//        val jobInfo = JobInfo.Builder(1,componentName)
//            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//            .setPersisted(true) // Job persists across reboots
////            .setPeriodic(2 * 1000L) // /Minimum 15 minutes interval
//            .build()
//
        val jobInfo = JobInfo.Builder(1, componentName)
            .setMinimumLatency(2000L) // Wait at least 2s
            .setOverrideDeadline(10000L) // Must finish within 10s
            .build()

        val jobScheduler = this.getSystemService(JobScheduler::class.java)
        jobScheduler?.schedule(jobInfo)
    }

    // function to set the work manager and observe it
    private fun setWorkManager() {
        // creating the constraints for for work request
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()

        // creating the instance of work request
        val wr = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()
        // enqueuing the work request
        WorkManager.getInstance(this).enqueue(wr)
        // observing the work request by id 
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(wr.id).observe(this) {
            if (it != null) {
                when (it.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.e("workManagerTest", "enqueued")
                    }

                    WorkInfo.State.RUNNING -> {
                        Log.e("workManagerTest", "running")
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        Log.e("workManagerTest", "succeed")
                        Log.e("workManagerTest", "the value = ${it.outputData.getString("vibhav")}")
                    }

                    WorkInfo.State.FAILED -> {
                        Log.e("workManagerTest", "failed")
                    }

                    WorkInfo.State.BLOCKED -> {
                        Log.e("workManagerTest", "blocked")

                    }

                    WorkInfo.State.CANCELLED -> {
                        Log.e("workManagerTest", "cancelled")
                    }
                }
            }
        }
    }}


private fun setDailyAlarm(context: Context) {
    // instance of alarm manager
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    // creating instance to alarm receiver
    val intent = Intent(context, AlarmReceiver::class.java)

    // creating pending intent
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 10)
        set(Calendar.MINUTE, 15)
        set(Calendar.SECOND, 0)
    }

    // If it's already past 10 AM today, schedule for tomorrow
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    alarmManager.setWindow(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        2 * 60 * 1000, // Flexibility window (adjust as needed)
        pendingIntent)

}
