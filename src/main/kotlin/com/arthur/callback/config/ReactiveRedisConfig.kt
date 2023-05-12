package com.arthur.callback.config

import com.arthur.callback.adapter.out.redis.repository.RedisCallbackDataRepository
import com.arthur.callback.adapter.out.redis.repository.RedisCallbackDataRepositoryImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class ReactiveRedisConfig(
        @Value("\${spring.data.redis.host}") private val redisHost: String,
        @Value("\${spring.data.redis.port}") private val redisPort: Int
) {

    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
        val redisConfiguration = RedisStandaloneConfiguration(redisHost, redisPort)
        return LettuceConnectionFactory(redisConfiguration)
    }

    @Bean
    fun reactiveRedisTemplate(reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, String> {
        val serializationContext = RedisSerializationContext.newSerializationContext<String, String>()
                .key(StringRedisSerializer())
                .value(StringRedisSerializer())
                .hashKey(StringRedisSerializer())
                .hashValue(StringRedisSerializer())
                .build()

        return ReactiveRedisTemplate(reactiveRedisConnectionFactory, serializationContext)
    }

    @Bean
    fun redisCallbackDataRepository(reactiveRedisTemplate: ReactiveRedisTemplate<String, String>): RedisCallbackDataRepository {
        return RedisCallbackDataRepositoryImpl(reactiveRedisTemplate)
    }
}
