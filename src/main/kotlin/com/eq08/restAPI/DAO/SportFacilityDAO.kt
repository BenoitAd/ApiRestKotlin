package com.eq08.restAPI.DAO

import com.eq08.restAPI.model.SportFacilityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface SportFacilityDAO : JpaRepository<SportFacilityEntity, String> {
    override fun findById(id: String): Optional<SportFacilityEntity>
}
