package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.exception.PlanningPokerBotException
import ru.firix.service.VotingService
import ru.firix.util.sendMessage

@Component
class ForceFinishCommand @Autowired constructor(
        private val votingService: VotingService
): BotCommand("/forceFinish", "stop voting") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        logger.debug("Try to handle '/forceFinish' from user: $user, and chat: $chat. Arguments: $arguments")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/forcefinish command available only in group chat, use /help.")
            return
        }

        try {
            val vote = votingService.forceFinish(chatId)
            sendMessage(sender, chatId, "Voting finished, results: $vote")
        } catch(ex: PlanningPokerBotException) {
            sendMessage(sender, chatId, ex.message)
        }
    }
}