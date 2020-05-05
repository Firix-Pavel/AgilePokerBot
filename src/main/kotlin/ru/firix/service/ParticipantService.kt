package ru.firix.service

import org.telegram.telegrambots.meta.api.objects.User

interface ParticipantService {
    /**
     * Add new user into system or update existed.
     * @return true if persist new user, false if update existed
     */
    fun saveUser(user: User): Boolean
}