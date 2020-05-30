package ru.firix.service.incoming

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.exception.PlanningPokerBotException
import ru.firix.service.VotingService
import ru.firix.util.sendMessage

/**
 * Get estimate from user.
 */
@Component("estimationStrategy")
class EstimationStrategy @Autowired constructor(
    private val votingService: VotingService
): IncomingMessageStrategy {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun process(sender: AbsSender, message: Message) {
        val user = message.from
        val text = message.text
        val userChatId = message.chatId
        logger.debug("Try to handle estimation from user: $user, and chat: ${message.chat.title}. Text: $text")
        if (!message.isUserMessage) {
            sendMessage(sender, userChatId, "Estimation is available only in user chat.")
            return
        }

        try {
            val vote = votingService.vote(userChatId, user, text)
            sendMessage(sender, userChatId, "Your estimation was received.")
            if (vote != null) {
                sendMessage(sender, vote.lobby.chatId, vote.toString())
            }
        } catch (ex: PlanningPokerBotException) {
            sendMessage(sender, userChatId, ex.message)
        }
    }
}