package com.study.boardproject.notification.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*


@Repository
class EmitterRepository {
    private val log = LoggerFactory.getLogger(EmitterRepository::class.java)
    private val emitterMap: MutableMap<String, SseEmitter> = mutableMapOf()

    fun save(userId:Long, sseEmitter: SseEmitter) : SseEmitter {
        emitterMap[getKey(userId)] = sseEmitter
        log.info("Saved SseEmitter for {}", userId)
        return sseEmitter
    }

    fun get(userId:Long) : Optional<SseEmitter>{
        log.info("Got SseEmitter for {}",userId)
        return Optional.ofNullable(emitterMap[getKey(userId)])
    }

    fun delete(userId: Long) {
        emitterMap.remove(getKey(userId))
        log.info("Deleted SseEmitter for {}", userId)
    }

    fun getKey(userId:Long) : String = "Emitter:ID:$userId"
}