package ru.firix.bot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi

@SpringBootApplication(scanBasePackages = ["ru.firix"])
@EnableJpaRepositories(basePackages = ["ru.firix"])
@EntityScan(basePackages = ["ru.firix.model.entity"])
open class AgilePokerBotInit @Autowired constructor(
        private val applicationContext: ApplicationContext,
        private val env: Environment
) : CommandLineRunner {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            ApiContextInitializer.init()
            runApplication<AgilePokerBotInit>(*args)
        }
    }

    override fun run(vararg args: String?) {
        val agilePokerBot = applicationContext.getBean("agilePokerBot") as AgilePokerBot
        TelegramBotsApi().registerBot(agilePokerBot)
    }
}
