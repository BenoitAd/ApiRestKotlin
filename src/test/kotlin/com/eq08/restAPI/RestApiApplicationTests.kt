package com.eq08.restAPI

import com.eq08.restAPI.DAO.CityDAO
import com.eq08.restAPI.DAO.SportFacilityDAO
import com.eq08.restAPI.controller.CityController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders



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
		// Get all the cities
		mockMvc.perform(get("/cities")).andExpect(status().isOk)
	}

	@Test
	@Order(2)
	fun testGetaCity(){
		// Get a given city
		mockMvc.perform(get("/cities/Nantes")).andExpect(status().isOk).andExpect(MockMvcResultMatchers.content().contentType(
			MediaType.APPLICATION_JSON_VALUE))
			.andExpect(jsonPath("$.nomVille").value("Nantes")).andExpect(jsonPath("$.population").value(633690))

	}



}
