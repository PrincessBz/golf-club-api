package com.keyin.golfclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyin.golfclub.dto.TournamentDTO;
import com.keyin.golfclub.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TournamentController.class)
public class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTournament() throws Exception {
        // Arrange
        TournamentDTO inputDto = new TournamentDTO();
        inputDto.setLocation("St. Andrews");
        inputDto.setStartDate(LocalDate.of(2025, 7, 17));

        TournamentDTO outputDto = new TournamentDTO();
        outputDto.setId(1L);
        outputDto.setLocation("St. Andrews");
        outputDto.setStartDate(LocalDate.of(2025, 7, 17));

        when(tournamentService.createTournament(any(TournamentDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.location").value("St. Andrews"));
    }

    @Test
    void testAddMemberToTournament() throws Exception {
        // Arrange
        TournamentDTO outputDto = new TournamentDTO();
        outputDto.setId(1L);
        outputDto.setParticipatingMembers(Collections.singleton(2L));

        when(tournamentService.addMemberToTournament(1L, 2L)).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/tournaments/1/members/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participatingMembers[0]").value(2L));
    }

    @Test
    void testGetMembersInTournament() throws Exception {
        // Arrange
        when(tournamentService.getMembersInTournament(1L)).thenReturn(Collections.singleton(2L));

        // Act & Assert
        mockMvc.perform(get("/api/tournaments/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0]").value(2L));
    }

}
