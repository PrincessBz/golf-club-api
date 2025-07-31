package com.keyin.golfclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyin.golfclub.dto.MemberDTO;
import com.keyin.golfclub.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper; // Utility to convert objects to JSON strings

    @Test
    void testAddMember() throws Exception {
        // Arrange
        MemberDTO inputDto = new MemberDTO(null, "Test Member", "123 Test St", "test@example.com", "555-0000", LocalDate.now(), 12);
        MemberDTO outputDto = new MemberDTO(1L, "Test Member", "123 Test St", "test@example.com", "555-0000", LocalDate.now(), 12);

        when(memberService.createMember(any(MemberDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated()) // Expect 201 CREATED
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Member"));
    }

    @Test
    void testGetMemberById() throws Exception {
        // Arrange
        MemberDTO memberDto = new MemberDTO(1L, "Found Member", "456 Found St", "found@example.com", "555-1111", LocalDate.now(), 24);
        when(memberService.findById(1L)).thenReturn(memberDto);

        // Act & Assert
        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Found Member"));
    }

    @Test
    void testUpdateMember() throws Exception {
        // Arrange
        MemberDTO inputDto = new MemberDTO(null, "Updated Info", null, null, null, null, 0);
        MemberDTO outputDto = new MemberDTO(1L, "Updated Info", "Address", "email@email.com", "555-2222", LocalDate.now(), 12);

        when(memberService.updateMember(eq(1L), any(MemberDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Info"));
    }

    @Test
    void testDeleteMember() throws Exception {
        // Arrange
        doNothing().when(memberService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/members/1"))
                .andExpect(status().isNoContent()); // Expect 204 NO CONTENT
    }

}
