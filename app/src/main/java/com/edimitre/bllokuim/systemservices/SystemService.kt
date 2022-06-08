package com.edimitre.bllokuim.systemservices

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.edimitre.bllokuim.R
import com.edimitre.bllokuim.activity.MainActivity
import com.edimitre.bllokuim.activity.ReminderActivity
import com.edimitre.bllokuim.data.dao.ReminderDao
import com.edimitre.bllokuim.data.db.MyRoomDatabase
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit


class SystemService(private val context: Context) {


    lateinit var reminderDao:ReminderDao

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "PersonalManagerNotificationChannel"
            val descriptionText = "PersonalManagerNotificationChannel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel =
                NotificationChannel("PersonalManagerNotificationChannel", name, importance)
            mChannel.description = descriptionText

            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            // register in system
            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(mChannel)
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarm(alarmTime: Long) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC,
                alarmTime,
                pendingIntent
            )
        }
    }


    fun cancelAllAlarms() {

        val i = Intent(context, MyReminderReceiver::class.java)

        @SuppressLint("UnspecifiedImmutableFlag")
        val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(pi)
    }

    fun notify(title: String?, message: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mainActivity = Intent(context, ReminderActivity::class.java)

            @SuppressLint("UnspecifiedImmutableFlag")
            val pi = PendingIntent.getActivity(
                context,
                1,
                mainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
            )


            val mBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, "PersonalManagerNotificationChannel")

            mBuilder.setSmallIcon(R.drawable.reminder)
            mBuilder.setContentIntent(pi)
            mBuilder.setContentTitle(title)
            mBuilder.setContentText(message)
            mBuilder.priority = NotificationCompat.PRIORITY_HIGH
            mBuilder.setAutoCancel(true)

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            notificationManager!!.notify(1, mBuilder.build())

        }

    }

    private fun permissionGranted(): Boolean {
        val result1 =
            ContextCompat.checkSelfPermission(context, permission.WRITE_EXTERNAL_STORAGE)
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    fun exportDatabase() {

        val sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)



        if (permissionGranted()) {
            val currentDBPath = MyRoomDatabase.getInstance(context).openHelper.writableDatabase.path
            val backupDBPath = "PersonalManagerDatabase.db"
            val currentDB = File(currentDBPath)
            val backupDB = File(sd, backupDBPath)
            if (currentDB.exists()) {
                try {
                    val src = FileInputStream(currentDB).channel
                    val dst = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
//                    src.close()
//                    dst.close()
                    Log.e("Personal Manager", "database backed up successfully ")
                    Toast.makeText(context, "Databaza u ruajt me sukses", Toast.LENGTH_SHORT).show()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun importDatabase() {

        val id = context.applicationContext.getDatabasePath("PersonalManagerDatabase.db")


        if (permissionGranted()) {
            val currentDBPath = "/storage/emulated/0/Documents/PersonalManagerDatabase.db"
            val backupDBPath = ""
            val currentDB = File(currentDBPath)
            val backupDB = File(id, backupDBPath)



            if (currentDB.exists()) {
                try {

                    val src = FileInputStream(currentDB).channel
                    val dst = FileOutputStream(backupDB).channel
                    dst.transferFrom(src, 0, src.size())
//                    src.close()
//                    dst.close()

                    val intent = Intent(context.applicationContext, MainActivity::class.java)
                    context.startActivity(intent)

                    Toast.makeText(context, "Databaza u ngarkua me sukses", Toast.LENGTH_SHORT)
                        .show()

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun restartApp() {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    fun startDbBackupWorker() {
        val workRequest = PeriodicWorkRequest.Builder(
            MyBackGroundWorker::class.java, 4,
            TimeUnit.HOURS
        )
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniquePeriodicWork(
            "dbBackupWorker",
            ExistingPeriodicWorkPolicy.REPLACE, workRequest
        )
        Log.e("BackGroundWorker : ", "dbBackUpScheduled")
    }


}