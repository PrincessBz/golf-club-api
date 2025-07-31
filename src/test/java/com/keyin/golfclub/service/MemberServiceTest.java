package com.keyin.golfclub.service;

import com.keyin.golfclub.dto.MemberDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void testCreateMember() {
        // Arrange
        MemberDTO inputDto = new MemberDTO(null, "New Member", "123 Main St", "new@example.com", "555-1234", LocalDate.now(), 12);

        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member memberToSave = invocation.getArgument(0);
            memberToSave.setId(1L); // Simulate the database generating an ID
            return memberToSave;
        });

        // Act
        MemberDTO resultDto = memberService.createMember(inputDto);

        // Assert
        assertNotNull(resultDto);
        assertEquals(1L, resultDto.getId()); // Verify an ID was assigned
        assertEquals("New Member", resultDto.getName());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember_Success() {
        // Arrange
        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setName("Old Name");

        MemberDTO updateDetails = new MemberDTO(1L, "Updated Name", "456 New Ave", "updated@example.com", "555-5678", LocalDate.now(), 24);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        MemberDTO resultDto = memberService.updateMember(1L, updateDetails);

        // Assert
        assertNotNull(resultDto);
        assertEquals("Updated Name", resultDto.getName());
        assertEquals("456 New Ave", resultDto.getAddress());
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember_NotFound() {
        // Arrange
        MemberDTO updateDetails = new MemberDTO();
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            memberService.updateMember(99L, updateDetails);
        });
        verify(memberRepository, never()).save(any(Member.class)); // Ensure save is never called
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(memberRepository.existsById(1L)).thenReturn(true);
        doNothing().when(memberRepository).deleteById(1L);

        // Act
        memberService.deleteById(1L);

        // Assert
        verify(memberRepository, times(1)).existsById(1L);
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(memberRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            memberService.deleteById(99L);
        });
        verify(memberRepository, never()).deleteById(anyLong()); // Ensure delete is never called
    }
}



