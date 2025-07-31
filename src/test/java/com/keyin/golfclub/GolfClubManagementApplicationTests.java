package com.keyin.golfclub;

import com.keyin.golfclub.dto.MemberDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;



@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class GolfClubManagementApplicationTests {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.32")
            .withDatabaseName("test-db")
            .withUsername("testuser")
            .withPassword("testpass");


    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void contextLoads() {

        assertTrue(mysqlContainer.isRunning());
    }

    @Test
    void testCreateAndRetrieveMember() throws Exception {
        // Step 1: Create a new member by calling the POST endpoint
        MemberDTO newMember = new MemberDTO();
        newMember.setName("Integration Test User");
        newMember.setEmail("integration@test.com");

        MvcResult postResult = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMember)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        // Extract the created member's ID from the response
        String responseString = postResult.getResponse().getContentAsString();
        MemberDTO createdMember = objectMapper.readValue(responseString, MemberDTO.class);
        Long newMemberId = createdMember.getId();

        // Step 2: Retrieve the member using the GET endpoint to confirm it was saved
        mockMvc.perform(get("/api/members/" + newMemberId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newMemberId))
                .andExpect(jsonPath("$.name").value("Integration Test User"));
    }

}
