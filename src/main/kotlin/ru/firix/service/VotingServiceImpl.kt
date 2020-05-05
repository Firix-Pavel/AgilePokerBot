package ru.firix.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.telegram.telegrambots.meta.api.objects.User
import ru.firix.exception.LobbyStructureException
import ru.firix.exception.ParticipantNotFoundException
import ru.firix.exception.VoteException
import ru.firix.model.dao.LobbyRepository
import ru.firix.model.dao.ParticipantRepository
import ru.firix.model.dao.VoteEntryRepository
import ru.firix.model.dao.VoteRepository
import ru.firix.model.entity.Participant
import ru.firix.model.entity.Vote
import ru.firix.model.entity.VoteEntry

@Service
@Transactional
open class VotingServiceImpl @Autowired constructor(
        private val participantRepository: ParticipantRepository,
        private val lobbyRepository: LobbyRepository,
        private val voteRepository: VoteRepository,
        private val voteEntryRepository: VoteEntryRepository
): VotingService {
    override fun start(chatId: Long, title: String): List<Participant> {
        val lobby = lobbyRepository.getByChatId(chatId)
        if (lobby == null || lobby.participants.isEmpty()) {
            throw LobbyStructureException("Lobby for chat is empty, add users in lobby.")
        }
        if (voteRepository.getByLobbyAndActive(lobby, true) != null) {
            throw VoteException(
                    "Please finish current voting before start new one. Use /votestatus to get information about it.")
        }

        val vote = Vote(title, true, lobby)
        voteRepository.save(vote)
        lobby.participants.forEach { it.requestedVote = vote }
        return lobby.participants
    }

    override fun forceFinish(chatId: Long): Vote {
        val lobby = lobbyRepository.getByChatId(chatId)
        val voteException = VoteException("Voting is not started. Use /startvote to start voting.")
        if (lobby == null || lobby.participants.isEmpty()) {
            throw voteException
        }
        val vote = voteRepository.getByLobbyAndActive(lobby, true) ?: throw voteException

        vote.active = false
        lobby.participants.forEach { it.requestedVote = null }
        return vote
    }

    override fun requireVote(chatId: Long, user: User): Participant {
        TODO()
    }

    override fun vote(chatId: Long, user: User, estimation: String): Vote? {
        val userId = user.id
        val participant = participantRepository.getByUserId(userId) ?: throw ParticipantNotFoundException(
                "User ${user.firstName} ${user.lastName} (${user.userName}) was not added to system. " +
                        "You have to /start AgilePokerBot."
        )
        val vote = participant.requestedVote ?: throw VoteException("You are not participating in any votes.")
        val lobby = vote.lobby

        var voteEntry = vote.voteEntries.find { it.participant.userId == userId }
        if (voteEntry == null) {
            voteEntry = VoteEntry(estimation, vote, participant)
            voteEntryRepository.save(voteEntry)
            vote.voteEntries.add(voteEntry)
        } else {
            voteEntry.estimation = estimation
        }

        return if(lobby.participants.size == vote.voteEntries.size) {
            vote.active = false
            lobby.participants.forEach { it.requestedVote = null }
            vote
        } else {
            null
        }
    }

    override fun getActiveVote(chatId: Long): Vote {
        val lobby = lobbyRepository.getByChatId(chatId)
        val voteException = VoteException("Voting is not started. Use /startvote to start voting.")
        if (lobby == null || lobby.participants.isEmpty()) {
            throw voteException
        }
        return voteRepository.getByLobbyAndActive(lobby, true) ?: throw voteException
    }
}