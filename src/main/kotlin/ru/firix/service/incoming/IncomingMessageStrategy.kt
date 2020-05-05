package ru.firix.service.incoming

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender

interface IncomingMessageStrategy {
    fun process(sender: AbsSender, message: Message)
}