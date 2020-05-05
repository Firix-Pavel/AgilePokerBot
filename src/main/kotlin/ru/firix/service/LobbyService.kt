package ru.firix.service

import org.telegram.telegrambots.meta.api.objects.User
import ru.firix.model.entity.Participant

interface LobbyService {
    /**
     * Add list of participants in lobby.
     * @return list of new participants in this lobby.
     * @throws ru.firix.exception.ParticipantNotFoundException if at least one of participants is not added into system.
     */
    fun addUsers(chatId: Long, userNames: List<String>): List<Participant>

    /**
     * Add existed user in lobby.
     * @return true if user is added in lobby, false if user already in lobby.
     * @throws ru.firix.exception.ParticipantNotFoundException if user is not added into system.
     */
    fun addUser(chatId: Long, user: User): Boolean

    /**
     * Delete all users from lobby.
     */
    fun deleteAllUsers(chatId: Long)

    /**
     * Info about lobby.
     */
    fun getStatus(chatId: Long): String
}