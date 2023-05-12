package com.arthur.callback.adapter.out.redis.repository

import com.arthur.callback.adapter.`in`.data.CallbackData
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.reactivestreams.Publisher
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class RedisCallbackDataRepositoryImpl(private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>) :
        RedisCallbackDataRepository {
    private val hashOperations: ReactiveHashOperations<String, String, String> =
            reactiveRedisTemplate.opsForHash<String, String>()

    private val TTL_DURATION = Duration.ofMinutes(10)

    override suspend fun getCallback(key: String): Mono<CallbackData?> {
        val value = hashOperations.get("callbacks", key).awaitFirstOrNull()
        return value?.let { Mono.just(CallbackData(key, it)) } ?: Mono.empty()
    }

    override fun <S : CallbackData?> save(entity: S): Mono<S> {
        val key = entity?.key ?: throw IllegalArgumentException("CallbackData key must not be null")
        val value = entity.value
        return hashOperations.put("callbacks", key, value)
                .then(reactiveRedisTemplate.expire("callbacks", TTL_DURATION))
                .then(Mono.just(entity))
    }

    override fun <S : CallbackData?> saveAll(entities: MutableIterable<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun <S : CallbackData?> saveAll(entityStream: Publisher<S>): Flux<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: String): Mono<CallbackData> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Publisher<String>): Mono<CallbackData> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: String): Mono<Boolean> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: Publisher<String>): Mono<Boolean> {
        TODO("Not yet implemented")
    }

    override fun findAll(): Flux<CallbackData> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<String>): Flux<CallbackData> {
        TODO("Not yet implemented")
    }

    override fun findAllById(idStream: Publisher<String>): Flux<CallbackData> {
        TODO("Not yet implemented")
    }

    override fun count(): Mono<Long> {
        return hashOperations.size("callbacks")
    }

    override fun deleteById(id: String): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Publisher<String>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun delete(entity: CallbackData): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<String>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<CallbackData>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entityStream: Publisher<out CallbackData>): Mono<Void> {
        TODO("Not yet implemented")
    }

    override fun deleteAll(): Mono<Void> {
        TODO("Not yet implemented")
    }
}
