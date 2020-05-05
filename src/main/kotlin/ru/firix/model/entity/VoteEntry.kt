package ru.firix.model.entity

import javax.persistence.*

@Entity
@Table(name = "vote_entry")
open class VoteEntry(
    @Column(name = "choice")
    var estimation: String,
    @ManyToOne
    @JoinColumn(name = "vote_id")
    var vote: Vote,
    @ManyToOne
    @JoinColumn(name = "participant_id")
    var participant: Participant
) {
    @Id
    @GeneratedValue
    private var id: Long? = null

    override fun toString() = """
            Vote entry for user ${participant.firstName} ${participant.lastName} (${participant.userName})
            Estimation: ${if(vote.active) "hidden" else estimation}
        """.trimIndent()
}