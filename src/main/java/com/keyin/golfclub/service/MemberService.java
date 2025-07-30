package com.keyin.golfclub.service;

import com.keyin.golfclub.dto.MemberDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    public Member save(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setAddress(memberDTO.getAddress());
        member.setEmail(memberDTO.getEmail());
        member.setPhoneNumber(memberDTO.getPhoneNumber());
        member.setMembershipStartDate(memberDTO.getMembershipStartDate());
        member.setMembershipDuration(memberDTO.getMembershipDuration());

        return memberRepository.save(member);
    }

    @Transactional
    public Member update(Long id, MemberDTO memberDetails) {
        Member member = findById(id); // Reuse findById to handle not found case

        member.setName(memberDetails.getName());
        member.setAddress(memberDetails.getAddress());
        member.setEmail(memberDetails.getEmail());
        member.setPhoneNumber(memberDetails.getPhoneNumber());
        member.setMembershipStartDate(memberDetails.getMembershipStartDate());
        member.setMembershipDuration(memberDetails.getMembershipDuration());

        return memberRepository.save(member);
    }

    public void deleteById(Long id) {
        Member member = findById(id); // Check if member exists before deleting
        memberRepository.delete(member);
    }

    public List<Member> searchByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Member> searchByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }


}
