package com.eq08.restAPI.model

import jakarta.persistence.*
import org.springframework.validation.annotation.Validated


@Entity
data class CityEntity(
    @Id
    @Column(name = "nomVille")
    @Validated
    var nomVille: String = "",

    @Column(name = "population")
    var population: Int = 0,

    @Column(name = "cityFacilities")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cityName")
    var cityFacilities: MutableSet<SportFacilityEntity> = mutableSetOf<SportFacilityEntity>()
)


