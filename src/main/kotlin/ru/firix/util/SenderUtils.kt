package ru.firix.util

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

private val logger = LoggerFactory.getLogger(object{}::class.java)

fun sendMessage(sender: AbsSender, chatId: Number, message: String?) {
    try {
        logger.debug("Send message. ChatId: $chatId, message: $message")
        sender.execute(SendMessage(chatId.toLong(), message))
    } catch (ex: TelegramApiException) {
        logger.error("Cannot send message. Chat id: $chatId, message: $message", ex)
    }
}