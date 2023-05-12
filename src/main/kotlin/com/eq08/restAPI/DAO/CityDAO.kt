package com.eq08.restAPI.DAO

import com.eq08.restAPI.model.CityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface CityDAO : JpaRepository<CityEntity, String>