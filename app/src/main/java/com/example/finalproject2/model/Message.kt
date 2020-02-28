package com.example.finalproject2.model

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

open class Message(
    var id: String? = null,
    var text: String? = null,
    var sender: String? = null,
    var receiver: String? = null,
    var time: String? = null
) {
    init {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        time = LocalDateTime.now()
            .format(formatter)
    }
}
