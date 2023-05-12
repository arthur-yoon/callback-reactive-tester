package com.arthur.callback.adapter.out.redis

import com.arthur.callback.adapter.`in`.data.CallbackData
import com.arthur.callback.adapter.out.redis.repository.RedisCallbackDataRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component

@Component
class RedisAdapter(
        private val redisCallbackDataRepository: RedisCallbackDataRepository
) {
    suspend fun getCallbackData(key: String): CallbackData? {
        return redisCallbackDataRepository.getCallback(key).awaitSingleOrNull()
    }
}