package com.eq08.restAPI.controller

import com.eq08.restAPI.DAO.CityDAO
import com.eq08.restAPI.model.CityEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import java.util.*
import kotlin.jvm.optionals.toSet

@RestController
@RequestMapping("/cities")
class CityController (var cityDAO: CityDAO){

    // GET all cities in the database
    @GetMapping
    fun getCities(): List<CityEntity> {
        return cityDAO.findAll()
    }

    // GET a particular city in the database
    @GetMapping("/{city}")
    fun getCity(@PathVariable city: String): Optional<CityEntity> {
        return cityDAO.findById(city)
    }

    // GET all sport facility from a particular sport in France.
    // example : /sport/Terrain de boules
    @GetMapping("/sport/{sport}")
    fun getCitiesBySport(@PathVariable sport: String): Any? {
            return cityDAO.findAll().map { it -> it.cityFacilities.filter { it.type == sport }
            }
    }

    // GET all sport facility from a particular sport in a particular city.
    // example : /sport/Terrain de boules/Nantes
    @GetMapping("/sport/{sport}/{city}")
    fun getSportByCity(@PathVariable sport: String, @PathVariable city: String): Any? {
        return cityDAO.findById(city).map { it -> it.cityFacilities.filter { it.type == sport }
        }
    }


    // UPDATE population of a given city from the database
    @PatchMapping("/update")
    fun updateCity(@RequestBody city : CityEntity): ResponseEntity<CityEntity> {
        cityDAO.findById(city.nomVille).get().population = city.population
        cityDAO.findById(city.nomVille).get().cityFacilities = city.cityFacilities
        return ResponseEntity.ok(cityDAO.findById(city.nomVille).get())
    }

    // DELETE a given city from the database
    @DeleteMapping("/delete/{id}")
    fun deleteCity(@PathVariable id: String) {
        val cityToDelete = cityDAO.findById(id).orElseThrow { java.lang.Exception() }
        cityDAO.delete(cityToDelete)
    }

    // Create a city
    @PostMapping("/create")
    fun createCity(@RequestBody city : CityEntity): ResponseEntity<CityEntity> {
        val cityCreated = cityDAO.save(city)
        return ResponseEntity.ok(cityCreated)
    }
}