package ru.firix.model.entity

import javax.persistence.*

@Entity
@Table(name = "vote")
open class Vote(
        @Column(name = "title")
        var title: String,
        @Column(name = "active")
        var active: Boolean,
        @ManyToOne
        @JoinColumn(name = "lobby_id")
        var lobby: Lobby,
        @OneToMany(fetch = FetchType.EAGER, mappedBy = "vote")
        var voteEntries: MutableList<VoteEntry> = mutableListOf()
) {
    @Id
    @GeneratedValue
    private var id: Long? = null

    override fun toString() = """
            |Title: "$title"
            |Active: $active
            |Vote entries:
            |""".trimMargin() + voteEntries.joinToString("\n") { "* $it" }
}