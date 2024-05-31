import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder

class TimerService : Service() {

    private val binder = TimerBinder()
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    var isTimerRunning = false
        private set

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun startTimer(duration: Long) {
        timeLeftInMillis = duration
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                // Notificar a la actividad o actualizar la UI
            }

            override fun onFinish() {
                isTimerRunning = false
                // Llamar a showNotification si corresponde
                // Notificar a la actividad que el temporizador ha terminado
            }
        }.start()
        isTimerRunning = true
    }

    fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
    }

    fun resumeTimer() {
        startTimer(timeLeftInMillis)
    }

    fun getTimeLeftInMillis() = timeLeftInMillis
}
