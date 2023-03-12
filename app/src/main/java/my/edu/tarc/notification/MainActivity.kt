package my.edu.tarc.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.os.Build

class MainActivity : AppCompatActivity() {

    //UI Views
    private lateinit var showNotificationBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init UI Views
        showNotificationBtn = findViewById(R.id.showNotification)

        showNotificationBtn.setOnClickListener{
            showNotification()
        }
    }

    companion object {
        const val channelID = "channel1"        //const that will be used
    }

    private fun showNotification() {
        createNotificationChannel()

        //unique ID to show new notification each time we click notification, if you want to replace previous use a constant value as ID
        val date = Date()
        val notificationID = SimpleDateFormat("ddHHmmss", Locale.US).format(date).toInt()

        //handle notification click, start SecondActivity by "TAPPING" notification     //later will change to open the app(MainActivity)
        val mainIntent = Intent(this, SecondActivity::class.java)
        //if you want to pass data in notification and get in required activity
        mainIntent.putExtra("KEY_NAME", "Ali")
        mainIntent.putExtra("KEY_EMAIL", "ali@gmail.com")
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent = PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_notification_foreground)
            .setContentTitle("Notification Title")
            .setContentText("This is the description of the notification, can be of multiple lines")

        //cancel notification when on click & add click intent
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setContentIntent(mainPendingIntent)

        val notificationManager : NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    private fun createNotificationChannel(){
        //In order to show notification on Android Oreo(O, API 26), we have to create notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Channel"
            val description = "Notification Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelID, name, importance)
            notificationChannel.description = description

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
}