package com.arthur.callback.application.service

import com.arthur.callback.adapter.`in`.data.CallbackData
import com.arthur.callback.adapter.out.redis.RedisAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class CallbackDataReceiver(
        private val redisAdapter: RedisAdapter
) {
    suspend fun receiveCallback(callbackData: CallbackData): String? {
        return withContext(Dispatchers.Default) {
            redisAdapter.getCallbackData(callbackData.key)
        }?.value
    }
}