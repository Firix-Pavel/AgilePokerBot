package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.firix.exception.AgilePokerBotException
import ru.firix.exception.ParticipantNotFoundException
import ru.firix.model.entity.Participant
import ru.firix.service.LobbyService
import ru.firix.util.sendMessage

@Component
class AddCommand @Autowired constructor(private val lobbyService: LobbyService):
        BotCommand("/add", "add participants for voting") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<String>) {
        logger.debug("Try to handle '/add' from user: $user, and chat: $chat. Arguments: $arguments")
        val chatId = chat.id
        if (!chat.isGroupChat) {
            sendMessage(sender, chatId, "/add command available only in group chat, use /help.")
            return
        }

        val newAddedParticipants: List<Participant>
        try {
            newAddedParticipants = lobbyService.addUsers(chatId, arguments.toList())
            sendMessage(sender, chatId, "Next new users were added in lobby "
                    + newAddedParticipants.map { "${it.firstName} ${it.lastName} (${it.userName})" })
        } catch (ex: AgilePokerBotException) {
            sendMessage(sender, chatId, ex.message)
            return
        }

        newAddedParticipants.forEach {
            sendMessage(sender, it.userId, "You were added in ${chat.title} lobby.")
        }
    }
}