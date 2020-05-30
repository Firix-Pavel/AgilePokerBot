package ru.firix.service.incoming.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.service.incoming.IncomingMessageStrategy

@Component
class IncomingMessageProcessorImpl @Autowired constructor(
        @Qualifier("estimationStrategy") private val estimationStrategy: IncomingMessageStrategy,
        @Qualifier("nonCommandStrategy") private val nonCommandStrategy: IncomingMessageStrategy
): IncomingMessageProcessor {

    override fun process(sender: AbsSender, message: Message) {
        if (message.text.startsWith("/")) {
            //unknown command
            nonCommandStrategy.process(sender, message)
            return
        }
        //it is estimation from user chat
        estimationStrategy.process(sender, message)
    }
}