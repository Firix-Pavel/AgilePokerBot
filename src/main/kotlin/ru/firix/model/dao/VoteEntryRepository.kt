package ru.firix.model.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.firix.model.entity.VoteEntry

interface VoteEntryRepository: JpaRepository<VoteEntry, Long>