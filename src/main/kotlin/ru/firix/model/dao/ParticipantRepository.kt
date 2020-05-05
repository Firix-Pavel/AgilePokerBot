package ru.firix.model.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.firix.model.entity.Participant

interface ParticipantRepository: JpaRepository<Participant, Long> {
    fun getByUserId(userId: Int): Participant?
    fun getByUserNameIn(userNames: List<String>): List<Participant>
}