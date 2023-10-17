package me.tangobee.weathernaut.ui.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.ui.base.MainActivity
import me.tangobee.weathernaut.util.AppConstants

class WeatherMusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentMusicUrl: String? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer()

        val notification = createNotification()
        startForeground(AppConstants.NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.extras?.getString("music_url")
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        if(url != null && url != currentMusicUrl) {
            currentMusicUrl = url
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.isLooping = true
            mediaPlayer.setOnPreparedListener {
                it.start()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotification() : Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = AppConstants.CHANNEL_ID
            val channel = NotificationChannel(channelId, "Weather Music Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, AppConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_music)
            .setContentTitle("Now Playing")
            .setContentText("Weather Music")
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        return notificationBuilder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}