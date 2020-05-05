package ru.firix.service.incoming.processor

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.service.incoming.IncomingMessageStrategy

@Component
class IncomingMessageProcessorImpl @Autowired constructor(
        @Qualifier("estimateStrategy") private val estimateStrategy: IncomingMessageStrategy
): IncomingMessageProcessor {

    override fun process(sender: AbsSender, message: Message) {
        // for now we have only one strategy to process non-command messages
        estimateStrategy.process(sender, message)
    }
}