package ru.firix.service.incoming.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.service.incoming.IncomingMessageStrategy
import ru.firix.service.incoming.NonCommandStrategy

@Component
class IncomingMessageProcessorImpl @Autowired constructor(
        @Qualifier("estimateStrategy") private val estimateStrategy: IncomingMessageStrategy,
        @Qualifier("nonCommandStrategy") private val nonCommandStrategy: NonCommandStrategy
): IncomingMessageProcessor {

    override fun process(sender: AbsSender, message: Message) {
        if (message.text.startsWith("/")) {
            //unknown command
            nonCommandStrategy.process(sender, message)
            return
        }
        //it is estimation from user chat
        estimateStrategy.process(sender, message)
    }
}