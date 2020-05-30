package ru.firix.command

import org.springframework.stereotype.Component
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender
import ru.firix.util.sendMessage

@Component
class HelpCommand: BotCommand("/help", "description of bot") {
    override fun execute(sender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        sendMessage(sender, chat.id, """
            |Hello, I am Planning Poker Bot and i help you estimate your tasks.
            |About planning poker you can read on https://en.wikipedia.org/wiki/Planning_poker.
            | 
            |Each user before using bot have to /start bot in private user chat.
            |Also you have to add bot into group chat to start votes.
            | 
            |Group chat commands:
            |/add <username> [<username>...] - add users into group lobby by usernames.
            |/addme - add yourself into group lobby.
            |/deleteall - delete all users from lobby.
            |/status - get info about lobby and all participants.
            |/startvote [<vote_title>] - start voting. You can not change structure of lobby before vote stops. After starting each participant receives message in private chat where he/she have to answer with estimation.
            |/votestatus - get info about current voting.
            |/forcefinish - stop current voting before all users give their estimates.
            | 
            |User chat commands:
            |/start - add user into Planning Poker Bot system.
            | 
            |You can find project on https://github.com/Firix-Pavel/PlanningPokerBot.
            """.trimMargin()
        )
    }
}