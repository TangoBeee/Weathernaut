package me.tangobee.weathernaut.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import me.tangobee.weathernaut.R

class WeatherMusicService : Service() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null
    }

    private val musicUrl = "https://weathernaut-backend.onrender.com/api/music"
    private val SERVICE_ID = 1
    private val CHANNEL_ID = "MUSIC_SERVICE_CHANNEL"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        // Check if the service was called with the "STOP_MUSIC" action to stop the music
        if (intent?.action == "STOP_MUSIC") {
            stopSelf() // Stop the service and the music
            return START_NOT_STICKY
        }

        // Intent for stopping the music through notification action
        val stopMusicIntent = Intent(this, WeatherMusicService::class.java).apply {
            action = "STOP_MUSIC"
        }
        val stopMusicPendingIntent = PendingIntent.getService(
            this, 0, stopMusicIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Foreground notification with action to stop music
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Playing Weather Music")
            .setContentText("Enjoy the music and your day's weather!")
            .setSmallIcon(R.drawable.icon_music) // Replace with your own icon
            .addAction(R.drawable.baseline_stop_24, "Stop Music", stopMusicPendingIntent) // Action to stop music
            .build()

        // Start the service in the foreground with the notification
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(SERVICE_ID, notification)
        } else {
            startForeground(SERVICE_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        }

        // Initialize or restart the media player
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(musicUrl)
                prepareAsync() // Async preparation for streaming
                setOnPreparedListener {
                    it.start() // Start playing music when ready
                }
                setOnCompletionListener {
                    start() // Loop the music when it ends
                }
                setOnErrorListener { _, _, _ ->
                    stopSelf() // Stop service on error
                    false
                }
            }
        } else if (!mediaPlayer!!.isPlaying) {
            mediaPlayer!!.start() // Resume music if it's paused
        }

        return START_STICKY
    }

    // Create notification channel for Android 8.0 and above
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // We don't bind this service to an activity
    }
}
