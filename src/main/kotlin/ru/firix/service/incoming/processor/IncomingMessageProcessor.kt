package ru.firix.service.incoming.processor

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

interface IncomingMessageProcessor {
    fun process(sender: AbsSender, message: Message)
}