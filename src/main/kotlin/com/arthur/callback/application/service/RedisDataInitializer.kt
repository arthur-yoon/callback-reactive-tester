package com.arthur.callback.application.service

import com.arthur.callback.adapter.`in`.data.CallbackData
import com.arthur.callback.adapter.out.redis.repository.RedisCallbackDataRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class RedisDataInitializer(
        private val redisCallbackDataRepository: RedisCallbackDataRepository
) : ApplicationListener<ApplicationReadyEvent> {

    private val logger = LoggerFactory.getLogger(RedisDataInitializer::class.java)


    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        val csvData = readCsvData()

        val callbackDataList = csvData.map { (transactionId, state) ->
            CallbackData(transactionId, state)
        }

        Flux.fromIterable(callbackDataList)
                .flatMap { redisCallbackDataRepository.save(it) }
                .then(redisCallbackDataRepository.count())
                .doOnSuccess { count -> logger.info("Successfully saved {} callback data entries to Redis.", count) }
                .subscribe()
    }

    private fun readCsvData(): List<Pair<String, String>> {
        val csvLines = javaClass.classLoader.getResourceAsStream("transaction.csv")?.bufferedReader().use { it?.readLines() }

        return csvLines?.drop(1)?.mapNotNull { line ->
            val parts = line.split(",")
            if (parts.size >= 2) {
                val transactionId = parts[0]
                val state = parts[1]
                transactionId to state
            } else {
                null
            }
        } ?: emptyList()
    }
}