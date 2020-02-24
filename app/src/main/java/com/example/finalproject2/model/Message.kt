package com.example.finalproject2.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.finalproject2.ultis.getTimeString
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Message(
    var id: String? = null,
    var text: String? = null,
    var sender: String? = null,
    var receiver: String? = null,
    var time: String? = null
) {
    init {
        time = LocalDateTime.now().getTimeString()
    }
}
