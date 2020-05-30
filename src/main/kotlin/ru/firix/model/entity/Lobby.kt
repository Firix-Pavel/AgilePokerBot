package ru.firix.model.entity

import javax.persistence.*

/**
 * Set of users in chat to vote.
 */
@Entity
@Table(name = "lobby")
open class Lobby(
        @Column(name = "chat_id")
        var chatId: Long,
        @ManyToMany
        @JoinTable(
                name = "participant_lobby_ref",
                joinColumns = [JoinColumn(name = "lobby_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "participant_id", referencedColumnName = "id")]
        )
        var participants: MutableList<Participant> = mutableListOf(),
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "lobby")
        var votes: MutableList<Vote> = mutableListOf()
) {
    @Id
    @GeneratedValue
    private var id: Long? = null

    override fun toString() = """
            |Lobby with chat ID: $chatId
            |Participants:
            |""".trimMargin() + participants.joinToString("\n", "*") { it.toString() }
}
