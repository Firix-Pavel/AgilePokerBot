package ru.firix.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.objects.User
import ru.firix.exception.ParticipantNotFoundException
import ru.firix.exception.VoteException
import ru.firix.model.dao.LobbyRepository
import ru.firix.model.dao.ParticipantRepository
import ru.firix.model.dao.VoteRepository
import ru.firix.model.entity.Lobby
import ru.firix.model.entity.Participant

@Service
@Transactional
open class LobbyServiceImpl @Autowired constructor(
        private val participantRepository: ParticipantRepository,
        private val lobbyRepository: LobbyRepository,
        private val voteRepository: VoteRepository
): LobbyService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun addUsers(chatId: Long, userNames: List<String>): List<Participant> {
        val participants = participantRepository.getByUserNameIn(userNames)
        if (participants.size != userNames.size) {
            val notFoundUserNames = userNames.filter { userName ->
                !participants.map { it.userName }.contains(userName)
            }
            throw ParticipantNotFoundException("Users with userNames $notFoundUserNames were not added to system." +
                    " They have to /start AgilePokerBot in private chat.")
        }

        var lobby = lobbyRepository.getByChatId(chatId)
        if (lobby == null) {
            lobby = Lobby(chatId)
            lobbyRepository.save(lobby)
        }
        if (voteRepository.getByLobbyAndActive(lobby, true) != null) {
            throw VoteException(
                    "Please finish current voting before add users. Use /votestatus to get information about it.")
        }


        val newParticipants = participants.filter { !lobby.participants.contains(it) }
        lobby.participants.addAll(newParticipants)
        return newParticipants
    }

    override fun addUser(chatId: Long, user: User): Boolean {
        val participant = participantRepository.getByUserId(user.id) ?: throw ParticipantNotFoundException(
                "User ${user.firstName} ${user.lastName} (${user.userName}) was not added to system. " +
                        "User has to /start AgilePokerBot in private chat.")

        var lobby = lobbyRepository.getByChatId(chatId)
        if (lobby == null) {
            lobby = Lobby(chatId)
            lobbyRepository.save(lobby)
        }
        if (voteRepository.getByLobbyAndActive(lobby, true) != null) {
            throw VoteException(
                    "Please finish current voting before add user. Use /votestatus to get information about it.")
        }

        return if (lobby.participants.find { it.userId == user.id } == null) {
            lobby.participants.add(participant)
            true
        } else {
            false
        }
    }

    override fun deleteAllUsers(chatId: Long) {
        val lobby = lobbyRepository.getByChatId(chatId) ?: return
        if (voteRepository.getByLobbyAndActive(lobby, true) != null) {
            throw VoteException(
                    "Please finish current voting before delete users. Use /votestatus to get information about it.")
        }
        lobby.participants = mutableListOf()
    }

    override fun getStatus(chatId: Long): String {
        return lobbyRepository.getByChatId(chatId)?.toString() ?: "Lobby is not created."
    }
}
