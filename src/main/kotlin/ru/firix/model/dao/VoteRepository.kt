package ru.firix.model.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.firix.model.entity.Lobby
import ru.firix.model.entity.Vote

interface VoteRepository: JpaRepository<Vote, Long> {
    fun getByLobbyAndActive(lobby: Lobby, active: Boolean): Vote?
}