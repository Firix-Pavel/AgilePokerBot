package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.exception.PlanningPokerBotException
import ru.firix.service.LobbyService
import ru.firix.util.sendMessage

@Component
class AddMeCommand @Autowired constructor(private val lobbyService: LobbyService):
        BotCommand("/addMe", "add participant for voting") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        logger.debug("Try to handle '/addMe' from user: $user, and chat: $chat, arguments: $arguments.")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/addme command available only in group chat, use /help.")
            return
        }

        val isNewUserAdded: Boolean
        try {
            isNewUserAdded = lobbyService.addUser(chatId, user)
        } catch (ex: PlanningPokerBotException) {
            sendMessage(sender, chatId, ex.message)
            return
        }

        sendMessage(sender, chatId, "User ${user.firstName} ${user.lastName} (${user.userName}) " +
                if(isNewUserAdded) "is added into lobby." else "already in lobby.")
        if (isNewUserAdded) {
            sendMessage(sender, user.id, "You were added in \"${chat.title}\" lobby")
        }
    }
}