package com.arthur.callback.adapter.`in`.api


import com.arthur.callback.adapter.`in`.data.CallbackData
import com.arthur.callback.application.service.CallbackDataReceiver
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CallbackController(private val callbackDataReceiver: CallbackDataReceiver) {
    private val logger = LoggerFactory.getLogger(CallbackController::class.java)

    @PostMapping("/callbacks")
    suspend fun receiveCallback(@RequestBody callbackData: CallbackData): ResponseEntity<String> {
        logger.info("callbacks: {}", callbackData)
        val value = callbackDataReceiver.receiveCallback(callbackData)
        return ResponseEntity.ok(value)
    }
}