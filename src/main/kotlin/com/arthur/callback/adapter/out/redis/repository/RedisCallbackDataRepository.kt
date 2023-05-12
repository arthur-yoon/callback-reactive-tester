package com.arthur.callback.adapter.out.redis.repository

import com.arthur.callback.adapter.`in`.data.CallbackData
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface RedisCallbackDataRepository : ReactiveCrudRepository<CallbackData, String> {
    suspend fun getCallback(key: String): Mono<CallbackData?>
}
