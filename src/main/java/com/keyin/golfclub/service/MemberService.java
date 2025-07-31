package com.keyin.golfclub.service;

import com.keyin.golfclub.dto.MemberDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

    }

    public MemberDTO findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
        return toDTO(member);
    }

    public MemberDTO createMember(MemberDTO memberDTO) {
        Member member = fromDTO(memberDTO);
        Member savedMember = memberRepository.save(member);
        return toDTO(savedMember);
    }


    @Transactional
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));

        // Update member fields
        member.setName(memberDTO.getName());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setEmail(memberDTO.getEmail());
        member.setAddress(memberDTO.getAddress());
        member.setMembershipDuration(memberDTO.getMembershipDuration());
        member.setMembershipStartDate(memberDTO.getMembershipStartDate());

        Member updatedMember = memberRepository.save(member);
        return toDTO(updatedMember);
    }

    public void deleteById(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }

    public List<MemberDTO> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MemberDTO> searchByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private MemberDTO toDTO(Member member) {
        return  new MemberDTO(
                member.getId(),
                member.getName(),
                member.getAddress(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getMembershipStartDate(),
                member.getMembershipDuration()
        );
    }

    private Member fromDTO(MemberDTO memberDTO) {
        Member member = new Member();
        // We don't set the ID from the DTO on creation
        member.setName(memberDTO.getName());
        member.setAddress(memberDTO.getAddress());
        member.setEmail(memberDTO.getEmail());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setMembershipStartDate(memberDTO.getMembershipStartDate());
        member.setMembershipDuration(memberDTO.getMembershipDuration());
        return member;

    }
}
