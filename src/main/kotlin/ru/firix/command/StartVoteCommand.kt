package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.exception.AgilePokerBotException
import ru.firix.service.VotingService
import ru.firix.util.sendMessage

@Component
class StartVoteCommand @Autowired constructor(
        private val votingService: VotingService
): BotCommand("/startVote", "start voting") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        logger.debug("Try to handle '/startVote' from user: $user, and chat: $chat. Arguments: $arguments")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/startvote command available only in group chat, use /help.")
            return
        }

        val votingTitle = arguments.joinToString(" ")
        try {
            val participants = votingService.start(chatId, votingTitle)
            sendMessage(sender, chatId, "Start voting $votingTitle. Wait user estimates or use /forcefinish, /votestatus")
            participants.forEach { sendMessage(sender, it.userId,
                    "You are added to voting in chat \"${chat.title}\". Please estimate task: $votingTitle") }
        } catch (ex: AgilePokerBotException) {
            sendMessage(sender, chatId, ex.message)
        }

    }
}