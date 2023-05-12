package com.eq08.restAPI.services

import com.eq08.restAPI.DAO.CityDAO
import com.eq08.restAPI.DAO.SportFacilityDAO
import com.eq08.restAPI.model.CityEntity
import com.eq08.restAPI.model.SportFacilityEntity
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Profile("!test")
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = arrayOf("enabled"),
    havingValue = "true",
    matchIfMissing = true)

@Component
class InsertData: CommandLineRunner{

    @Autowired
    private lateinit var cityDao: CityDAO

    @Autowired
    private lateinit var sportFacilityDAO: SportFacilityDAO

    override fun run(vararg args: String?) {
        val reader = CSVReader(FileReader("./datasets/input_cities.csv"))

        val lines = reader.readAll()
        lines.removeAt(0) // delete first line
        // create cities entities
        for (line in lines) {
            val name = line[0]
            val population = line[7].toInt()
            val city = CityEntity(nomVille = name, population = population)
            cityDao.save(city)
        }

        // create sport facility entities
        val fileName = "./datasets/input_equipement.csv"
        val myPath: Path = Paths.get(fileName)

        val parser = CSVParserBuilder().withSeparator(';').build()

        Files.newBufferedReader(myPath, StandardCharsets.UTF_8).use { br ->
            CSVReaderBuilder(br).withCSVParser(parser)
                .build().use { reader ->

                    val rows = reader.readAll()
                    rows.removeAt(0) // delete first line

                    for (line in rows) {
                        val name = line[5]
                        val cityName = line[3]
                        val family = line[11]
                        val type = line[10]
                        val id: String = (line[4]).toString()
                        val sportEntity =
                            SportFacilityEntity(id = id, nom = name, cityName = cityName, family = family, type = type)
                        sportFacilityDAO.save(sportEntity)
                    }
                }
        }

    }
}