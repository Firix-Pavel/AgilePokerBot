package ru.firix.bot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.firix.command.*
import ru.firix.service.incoming.processor.IncomingMessageProcessor
import ru.firix.util.sendMessage

@Component
class AgilePokerBot @Autowired constructor(
        private val startCommand: StartCommand,
        private val helpCommand: HelpCommand,
        private val addCommand: AddCommand,
        private val addMeCommand: AddMeCommand,
        private val deleteCommand: DeleteCommand,
        private val statusCommand: StatusCommand,
        private val startVoteCommand: StartVoteCommand,
        private val forceFinishCommand: ForceFinishCommand,
        private val voteCommand: VoteCommand,
        private val voteStatusCommand: VoteStatusCommand,
        private val incomingMessageProcessor: IncomingMessageProcessor
) : TelegramLongPollingCommandBot() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getBotUsername() = BotAuthSettings.username
    override fun getBotToken() = BotAuthSettings.token

    init {
        register(startCommand)
        register(helpCommand)

        register(addCommand)
        register(addMeCommand)
        register(deleteCommand)
        register(statusCommand)

        register(startVoteCommand)
        register(forceFinishCommand)
        register(voteCommand)
        register(voteStatusCommand)
        logger.info("Finish setup bot")
    }

    override fun processNonCommandUpdate(update: Update?) {
        logger.debug("Process non command update $update")
        try {
            val message: Message = update?.message?: return
            handleIncomingMessage(message)
        } catch (e: Exception) {
            logger.error(e.message, e)
        }
    }

    private fun handleIncomingMessage(message: Message) {
        logger.debug("Handle Incoming message $message")
        incomingMessageProcessor.process(this, message)
    }
}
