package com.eq08.restAPI


import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status



@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class RestApiApplicationTests {
	@Autowired
	private lateinit var mockMvc: MockMvc
	@Autowired lateinit var mapper: ObjectMapper


	@Test
	@Order(1)
	fun testGetCities(){
		// Get all the citval s = status();
		mockMvc.perform(get("/cities")).andExpect(status().isOk)
	}

	@Test
	@Order(2)
	fun testGetaCity(){
		// Get a given city by the name
		mockMvc.perform(get("/cities/Nantes")).andExpect(status().isOk).andExpect(MockMvcResultMatchers.content().contentType(
			MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.nomVille").value("Nantes")).andExpect(jsonPath("$.population").value(633690))

	}

	@Test
	@Order(3)
	fun testGetaFakeCity(){
		// throw an exception when the city name doesn't exists in the data base
		assertThrows<Exception> { mockMvc.perform(get("/cities/cetteVilleEstFake")) }
	}


	@Test
	@Order(4)
	fun testGetAllFrenchSportFacilityFromAGivenSport() {
		// return all sport facilities from a given sport type
		val response = mockMvc.perform(get("/cities/sport/Court de tennis"))
			.andExpect(status().isOk)
			.andReturn().response.contentAsString

		val json = ObjectMapper().readTree(response)
		for (element in json) {
			val jsonString = element.toString()
			if (jsonString.contains("type") && !jsonString.contains("Court de tennis")) {
				assert(false)
			}
		}
		assert(true)
	}

	@Test
	@Order(5)
	fun testGetAllFrenchSportFacilityFromAFakeGivenSport() {
		// throw exception when the sport type doesn't exist
		assertThrows<Exception> { mockMvc.perform(get("/cities/sport/fakeSport")) }
	}

	@Test
	@Order(6)
	fun testGetAllSportFacilityFromGivenSportAndAGivenCity() {
		// get alle sport facility from a given type in a given city name
		val response = mockMvc.perform(get("/cities/sport/Terrain de boules/Nantes"))
			.andExpect(status().isOk)
			.andReturn().response.contentAsString

		val json = ObjectMapper().readTree(response)
		for (element in json) {
			val jsonString = element.toString()
			if (jsonString.contains("type") && !jsonString.contains("Terrain de boules")) {
				assert(false)
			}
			if (jsonString.contains("cityName") && !jsonString.contains("Nantes")) {
				assert(false)
			}
		}
		assert(true)
	}

	@Test
	@Order(7)
	fun testGetAllSportFacilityFromGivenFakeSportAndAGivenCity() {
		// throw exception when the sport type doesn't exist
		assertThrows<Exception> { mockMvc.perform(get("/cities/sport/fakeSport/Nantes")) }
	}

	@Test
	@Order(8)
	fun testGetAllSportFacilityFromGivenSportAndAGivenFakeCity() {
		// throw exception when the city name doesn't exist
		assertThrows<Exception> { mockMvc.perform(get("/cities/sport/Terrain de boules/fakeCity")) }
	}

	@Test
	@Order(9)
	fun testUpdateCity() {
		// update a city already existing in the data base
		val cityJson = """
        {
            "nomVille": "Paris",
            "cityFacilities": [{
			"id": "I441090061",                
			"nom": "College Francois d'Amboise",                
			"cityName": "Nantes",                
			"family": "Salle multisports",                
			"type": "Salle multisports (gymnase)"            
			}],
            "population": 1111
        }
    	"""
		val response = mockMvc.perform(
			patch("/cities/update")
			.contentType(MediaType.APPLICATION_JSON)
			.content(cityJson))
			.andExpect(status().isOk)
			.andReturn().response.contentAsString
		assertEquals(response,cityJson,true)
	}

	@Test
	@Order(10)
	fun testUpdateFakeCity() {
		// throw exception when the city name doesn't exist
		val cityJson = """
        {
            "nomVille": "fakeCity",
            "cityFacilities": [],
            "population": 1111
        }
    	"""
		assertThrows<Exception> { mockMvc.perform(patch("/cities/update")
			.contentType(MediaType.APPLICATION_JSON)
			.content(cityJson)) }
	}

	@Test
	@Order(11)
	fun testDeleteCity(){
		// delete a given city by the name
		mockMvc.perform(delete("/cities/delete/Nantes")).andExpect(status().isOk)
	}

	@Test
	@Order(12)
	fun testDeleteFakeCity(){
		// throw exception when the city name doesn't exist
		assertThrows<Exception> { mockMvc.perform(delete("/cities/delete/fakeCity")) }
	}


	@Test
	@Order(12)
	fun testCreateCity(){
		// create a city
		val cityJson = """
        {
		  "nomVille": "Test",
		  "cityFacilities": [],
		  "population": 1000000
        }
    	"""
		val response = mockMvc.perform(
			post("/cities/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cityJson))
			.andExpect(status().isOk)
			.andReturn().response.contentAsString
		assertEquals(response,cityJson,true)
	}

	@Test
	@Order(13)
	fun testGetAllSportFacilities(){
		// Get all the sport facility entities
		mockMvc.perform(get("/sportFacilities")).andExpect(status().isOk)
	}

	@Test
	@Order(14)
	fun testGetASportifFacility(){
		/* Get a given Sport facility :
		{
  			"id": "I010040002",
  			"nom": "Ambar'Rock",
			 "cityName": "Ambérieu-en-Bugey",
			 "family": "Salle ou terrain spécialisé",
			 "type": "Salle de danse"
		}
		 */
		mockMvc.perform(get("/sportFacilities/I010040002")).andExpect(status().isOk).andExpect(MockMvcResultMatchers.content().contentType(
			MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.cityName").value("Ambérieu-en-Bugey"))
			.andExpect(jsonPath("$.nom").value("Ambar'Rock"))
			.andExpect(jsonPath("$.family").value("Salle ou terrain spécialisé"))
			.andExpect(jsonPath("$.type").value("Salle de danse"))
	}

	@Test
	@Order(15)
	fun testGetaFakeSportFacility(){
		// throw an exception when the city doesn't exist
		assertThrows<Exception> { mockMvc.perform(get("/sportFacilities/fakeID")) }
	}

	@Test
	@Order(16)
	fun testGetAllSportFacilitiesType(){
		// Get all the type of sport facilities
		mockMvc.perform(get("/sportFacilities/equipementType")).andExpect(status().isOk)
	}

	@Test
	@Order(17)
	fun testGetAllSportFacilitiesFamily(){
		// Get all the type of sport facilities
		mockMvc.perform(get("/sportFacilities/equipementFamily")).andExpect(status().isOk)
	}

	@Test
	@Order(18)
	fun testCreateSportFacility(){
		// create a new sport facility
		val sportJson = """
		{
		  "id": "idRandom154",
		  "nom": "College Francois d'Amboise",
		  "cityName": "Nantes",
		  "family": "Salle multisports",
		  "type": "Salle multisports (gymnase)"
		}
    	"""
		val response = mockMvc.perform(
			post("/sportFacilities/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(sportJson))
			.andExpect(status().isOk)
			.andReturn().response.contentAsString
		assertEquals(response,sportJson,true)
	}
	@Test
	@Order(19)
	fun testDeleteSportFacility(){
		// delete a given sport facility by the ID
		mockMvc.perform(delete("/sportFacilities/delete/I010040006")).andExpect(status().isOk)
	}

	@Test
	@Order(20)
	fun testDeleteFakeSportFacility(){
		// throw exception when the sport ID doesn't exist
		assertThrows<Exception> { mockMvc.perform(delete("/cities/delete/fakeSport")) }
	}
}
