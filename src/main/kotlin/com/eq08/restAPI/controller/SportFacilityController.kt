package com.eq08.restAPI.controller

import com.eq08.restAPI.DAO.SportFacilityDAO
import com.eq08.restAPI.model.SportFacilityEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/sportFacilities")
class SportFacilityController (var sportFacilityDAO: SportFacilityDAO){
    // GET all sport facilty in the database
    @GetMapping
    fun getSportFacilities(): List<SportFacilityEntity> {
        return sportFacilityDAO.findAll()
    }

    // Récupérer une installation sportives selon son id
    @GetMapping("/{id}")
    fun getSportFacilities(@PathVariable id: String): Optional<SportFacilityEntity> {
        return sportFacilityDAO.findById(id)
    }

    // Récupérer les différents types d'installations sportives
    @GetMapping("/equipementType")
    fun getEquipementType(): MutableList<String>? {
        val equipementTypeList: MutableList<String> = mutableListOf<String>()

        sportFacilityDAO.findAll().forEach{
            if(!equipementTypeList.contains(it.type))
                equipementTypeList.add(it.type)
        }

        return equipementTypeList
    }

    // Récupérer les différentes familles d'installations sportives
    @GetMapping("/equipementFamily")
    fun getEquipementFamily(): MutableList<String>? {
        val equipementFamilyList: MutableList<String> = mutableListOf<String>()

        sportFacilityDAO.findAll().forEach{
            if(!equipementFamilyList.contains(it.family))
                equipementFamilyList.add(it.family)
        }

        return equipementFamilyList
    }

    // CREATE
    @PostMapping(value = ["/create"], consumes = ["application/xml", "application/json"])
    fun createSportFacility(@RequestBody sportFacility: SportFacilityEntity): SportFacilityEntity {
        return sportFacilityDAO.save(sportFacility)
    }

    // UPDATE
    // Update le nom d'un equipement de sport
    @PatchMapping("/name/{id}/{newName}")
    fun updateSportFacilityName(@PathVariable id: String, @PathVariable newName: String) : SportFacilityEntity {
        val sportFacilityToUpdate = sportFacilityDAO.findById(id).orElseThrow{ java.lang.Exception() }

        sportFacilityToUpdate.nom = newName

        return sportFacilityDAO.save(sportFacilityToUpdate)
    }

    // DELETE
    // Supprime un equipement de sport
    @DeleteMapping("/delete/{id}")
    fun deleteSportFacility(@PathVariable id: String) {
        sportFacilityDAO.deleteById(id)
    }
}