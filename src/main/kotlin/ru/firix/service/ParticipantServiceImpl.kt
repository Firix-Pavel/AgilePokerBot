package ru.firix.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.objects.User
import ru.firix.model.dao.ParticipantRepository
import ru.firix.model.entity.Participant

@Service
@Transactional
open class ParticipantServiceImpl @Autowired constructor(
        private val participantRepository: ParticipantRepository
): ParticipantService {

    override fun saveUser(user: User): Boolean {
        val participant = participantRepository.getByUserId(user.id)
        return if (participant == null) {
            participantRepository.save(Participant(user.id, user.userName, user.firstName, user.lastName))
            true
        } else {
            participant.userName = user.userName
            participant.firstName = user.firstName
            participant.lastName = user.lastName
            false
        }
    }
}