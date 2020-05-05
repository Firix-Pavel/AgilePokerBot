package ru.firix.command

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import ru.firix.service.LobbyService
import ru.firix.service.ParticipantService
import ru.firix.util.sendMessage

@Component
class StartCommand @Autowired constructor(private val participantService: ParticipantService) :
        BotCommand("/start", "start using bot") {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        logger.debug("Try to handle '/start' from user: $user, and chat: $chat.")
        val chatId = chat.id
        if(!chat.isUserChat) {
            sendMessage(sender, chatId, "/start command is available only in user chat, use /help.")
            return
        }

        val isCreatedNewParticipant = participantService.saveUser(user)
        sendMessage(sender, chatId, "Hello, ${user.firstName}, " +
                if (isCreatedNewParticipant) "you were added to agile poker." else "we've updated info about you.")
    }
}