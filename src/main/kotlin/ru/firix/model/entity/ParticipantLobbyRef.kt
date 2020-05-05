package ru.firix.model.entity

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Table

@Embeddable
@Table(name = "participant_lobby_ref")
open class ParticipantLobbyRef(
        @Column(name = "lobby_id")
        var lobbyId: Long,
        @Column(name = "participant_id")
        var participantId: Long
)