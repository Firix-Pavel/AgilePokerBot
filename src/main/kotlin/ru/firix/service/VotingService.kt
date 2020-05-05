package ru.firix.service

import org.telegram.telegrambots.meta.api.objects.User
import ru.firix.exception.VoteException
import ru.firix.model.entity.Participant
import ru.firix.model.entity.Vote
import ru.firix.model.entity.VoteEntry

interface VotingService {
    fun start(chatId: Long, title: String): List<Participant>
    fun forceFinish(chatId: Long): Vote
    fun requireVote(chatId: Long, user: User): Participant
    /**
     * Receive estimation from user.
     * @return vote if voting is ended, null otherwise.
     * @throws VoteException if
     */
    fun vote(chatId: Long, user: User, estimation: String): Vote?
    fun getActiveVote(chatId: Long): Vote
}