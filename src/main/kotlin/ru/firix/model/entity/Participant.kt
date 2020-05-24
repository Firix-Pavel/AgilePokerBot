package ru.firix.model.entity

import javax.persistence.*

/**
 * User in chat.
 */
@Entity
@Table(name = "participant")
open class Participant(
        @Column(name = "user_id")
        var userId: Int,
        @Column(name = "username")
        var userName: String,
        @Column(name = "first_name")
        var firstName: String,
        @Column(name = "last_name")
        var lastName: String,
        @ManyToOne
        @JoinColumn(name = "vote_id")
        var requestedVote: Vote? = null,
        @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
        var lobbies: MutableList<Lobby> = mutableListOf()
) {
    @Id
    @GeneratedValue
    private var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Participant
        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId
    }

    override fun toString() = """
            |Participant with username: $userName, userId: $userId
            |Requested vote: $requestedVote
            """.trimMargin()
}
