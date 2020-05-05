package ru.firix.model.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.firix.model.entity.Lobby

interface LobbyRepository: JpaRepository<Lobby, Long> {
    fun getByChatId(chatId: Long): Lobby?
}