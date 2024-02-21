package com.hetica.AutismoCordoba

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder
import android.provider.Settings
import android.util.Log

class ConcentrationModeService : Service() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var handler: Handler
    private lateinit var dndModeChecker: Runnable

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("settings_preferences", Context.MODE_PRIVATE)
        handler = Handler()
        dndModeChecker = Runnable {
            checkDNDMode()
            handler.postDelayed(dndModeChecker,  500)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.post(dndModeChecker)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(dndModeChecker)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun checkDNDMode() {
        val concentrationModeActivated = getConcentrationModeState()
        Log.d("Estado de concentracion", getConcentrationModeState().toString())
        val appInForeground = isAppInForeground(this)
        if(concentrationModeActivated){
            Log.d(TAG, "Modo No molestar activado por HETICA")
            if(!appInForeground){
                Log.d(TAG, "Hetica en segundo plano, desactivando el modo No molestar")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                saveConcentrationModeState(false)
            }
            if(isAppClosed()){
                Log.d(TAG, "Hetica cerrada, desactivando el modo No molestar")
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                saveConcentrationModeState(false)
            }

        } else if(Settings.Global.getInt(contentResolver, "zen_mode")!=0)  { //el modo de No molestar está activo pero no por HETICA
            Log.d(TAG, "Modo No molestar activado por otra app") //No hacemos nada

        }
    }

    private fun getConcentrationModeState(): Boolean {
        return sharedPreferences.getBoolean("concentration_mode", false)
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = context.packageName

        val appProcesses = activityManager.runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == packageName
            ) {
                return true //true si la app esta en PRIMER PLANO
            }
        }

        return false
    }
    private fun saveConcentrationModeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("concentration_mode", state)
        editor.apply()
    }
    private fun isAppClosed(): Boolean {
//por implementar
        return false
    }


    companion object {
        private const val TAG = "DNDModeService"
    }
}