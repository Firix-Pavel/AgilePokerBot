package ru.firix.service.incoming

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.util.sendMessage

/**
 * Get unknown command.
 */
@Component("nonCommandStrategy")
class NonCommandStrategy: IncomingMessageStrategy {
    override fun process(sender: AbsSender, message: Message) {
        sendMessage(sender, message.chatId, "Unknown command: ${message.text}.")
    }
}