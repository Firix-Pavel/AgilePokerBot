package ru.firix.bot

import java.util.*


/**
 * We can't use Spring properties, because TelegramLongPollingCommandBot calls getBotToken() and getBotUsername()
 * in constructor before Spring proxy objects and beans are fully initialized.
 */
object BotAuthSettings {
    val username: String
    val token: String

    init {
        var username: String? = null
        var token: String? = null
        BotAuthSettings.javaClass.getResourceAsStream("/bot_auth.properties").use { input ->
            val prop = Properties()
            prop.load(input)
            username = prop.getProperty("username")
            token = prop.getProperty("token")
        }

        if (username == null || token == null) {
            throw IllegalStateException("Bot username: $username, token: $token")
        }
        this.username = username as String
        this.token = token as String
    }
}