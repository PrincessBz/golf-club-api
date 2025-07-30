package com.keyin.golfclub.controller;

import com.keyin.golfclub.dto.MemberDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController( MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public Member addMember(@RequestBody MemberDTO memberDTO) {
        return memberService.save(memberDTO);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.findAll();
    }

    @GetMapping("/search")
    public List<Member> searchMembers (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phoneNumber){

        if (name != null) {
            return memberService.searchByName(name);
        } else if (phoneNumber != null) {
            return memberService.searchByPhoneNumber(phoneNumber);
        } else {
            return memberService.findAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody MemberDTO memberDTO) {
        Member updatedMember = memberService.update(id, memberDTO);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
