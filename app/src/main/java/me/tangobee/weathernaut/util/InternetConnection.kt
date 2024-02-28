package me.tangobee.weathernaut.util

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class InternetConnection {

    companion object {
        suspend fun isNetworkAvailable(context: Context): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    // Check if the device is connected to a network
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    connectivityManager.activeNetwork ?: return@withContext false

                    // Check if the device has a working internet connection
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53) // Google's DNS server
                    socket.connect(socketAddress, 1500)
                    socket.close()

                    true
                } catch (e: IOException) {
                    false
                }
            }
        }
    }
}