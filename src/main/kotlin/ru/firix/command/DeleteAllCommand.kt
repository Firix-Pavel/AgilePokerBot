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
class DeleteAllCommand @Autowired constructor(private val lobbyService: LobbyService):
        BotCommand("/deleteall", "delete participants from lobby") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        logger.debug("Try to handle '/delete' from user: $user, and chat: $chat. Arguments: $arguments")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/deleteall command available only in group chat, use /help.")
            return
        }
        
        try {
            lobbyService.deleteAllUsers(chatId)
        } catch (ex: PlanningPokerBotException) {
            sendMessage(sender, chatId, ex.message)
            return
        }
        sendMessage(sender, chatId, "All users were deleted from \"${chat.title}\" lobby.")
    }
}