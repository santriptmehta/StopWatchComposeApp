import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class StopWatch {

    var formattedTime by mutableStateOf("00:00:000")
    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isActive = false

    private var timeMillis = 0L
    private var lastTimeTamp = 0L

    fun start(){
        if(isActive) return

        coroutineScope.launch {
            lastTimeTamp = System.currentTimeMillis()
            this@StopWatch.isActive = true
            while (this@StopWatch.isActive){
                delay(10L)
                timeMillis += System.currentTimeMillis() - lastTimeTamp
                lastTimeTamp = System.currentTimeMillis()
                formattedTime = formatTime(timeMillis)
            }
        }
    }

    fun pause(){
        isActive = false
    }
    fun reset(){
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeMillis = 0L
        lastTimeTamp = 0L
        formattedTime = "00:00:000"
        isActive = false

    }
    private fun formatTime(timeMillie:Long):String{
        val localDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timeMillie),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofPattern(
            "mm:ss:SSS",
            Locale.getDefault()
        )
        return localDateTime.format(formatter)
    }
}