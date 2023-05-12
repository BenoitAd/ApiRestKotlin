package com.eq08.restAPI.model

import com.eq08.restAPI.controller.SportFacilityController
import jakarta.persistence.*

@Entity
data class SportFacilityEntity(
    @Id
    @Column(name = "idFacility")
    var id: String = "",

    @Lob
    @Column(name = "nom")
    var nom: String = "",

    @Column(name = "cityName")
    var cityName: String = "",

    @Column(name = "family")
    var family: String = "",

    @Column(name = "type")
    var type: String = ""
) {
}
