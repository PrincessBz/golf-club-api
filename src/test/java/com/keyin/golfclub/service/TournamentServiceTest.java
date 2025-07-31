package com.keyin.golfclub.service;

import com.keyin.golfclub.dto.TournamentDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.model.Tournament;
import com.keyin.golfclub.repository.MemberRepository;
import com.keyin.golfclub.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @Test
    void testCreateTournament() {
        // Arrange
        TournamentDTO inputDto = new TournamentDTO();
        inputDto.setLocation("Pebble Beach");

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament tournamentToSave = invocation.getArgument(0);
            tournamentToSave.setId(1L); // Simulate DB generating ID
            return tournamentToSave;
        });

        // Act
        TournamentDTO resultDto = tournamentService.createTournament(inputDto);

        // Assert
        assertNotNull(resultDto);
        assertEquals(1L, resultDto.getId());
        assertEquals("Pebble Beach", resultDto.getLocation());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testAddMemberToTournament_Success() {
        // Arrange
        Tournament existingTournament = new Tournament();
        existingTournament.setId(1L);

        Member existingMember = new Member();
        existingMember.setId(2L);

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(existingTournament));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(existingMember));
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(existingTournament);

        // Act
        tournamentService.addMemberToTournament(1L, 2L);

        // Assert
        assertTrue(existingTournament.getParticipatingMembers().contains(existingMember));
        verify(tournamentRepository, times(1)).save(existingTournament);
    }

    @Test
    void testAddMemberToTournament_TournamentNotFound() {
        // Arrange
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            tournamentService.addMemberToTournament(99L, 2L);
        });
        verify(memberRepository, never()).findById(anyLong()); // Ensure we never looked for the member
        verify(tournamentRepository, never()).save(any(Tournament.class));
    }
}
