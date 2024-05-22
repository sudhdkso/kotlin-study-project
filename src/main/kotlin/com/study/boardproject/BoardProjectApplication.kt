package com.study.boardproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BoardProjectApplication

fun main(args: Array<String>) {
	runApplication<BoardProjectApplication>(*args)
}
