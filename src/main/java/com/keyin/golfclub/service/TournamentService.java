package com.keyin.golfclub.service;

import com.keyin.golfclub.dto.TournamentDTO;
import com.keyin.golfclub.exception.ResourceNotFoundException;
import com.keyin.golfclub.model.Member;
import com.keyin.golfclub.model.Tournament;
import com.keyin.golfclub.repository.MemberRepository;
import com.keyin.golfclub.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final MemberRepository memberRepository;

public TournamentDTO createTournament(TournamentDTO dto) {
    Tournament tournament = fromDTO(dto);
    Tournament saved = tournamentRepository.save(tournament);
    return toDTO(saved);
}

public List<TournamentDTO> getAllTournaments() {
    return tournamentRepository.findAll().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
}

public TournamentDTO getTournamentById(Long id) {
    Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament", id));
    return toDTO(tournament);
}

public TournamentDTO updateTournament(Long id, TournamentDTO dto) {
    Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament", id));

    tournament.setStartDate(dto.getStartDate());
    tournament.setEndDate(dto.getEndDate());
    tournament.setLocation(dto.getLocation());
    tournament.setEntryFee(dto.getEntryFee());
    tournament.setCashPrize(dto.getCashPrize());

    Tournament updated = tournamentRepository.save(tournament);
    return toDTO(updated);
}

public void deleteTournament(Long id) {
    Tournament tournament = tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament", id));
    tournamentRepository.delete(tournament);
}

public TournamentDTO addMemberToTournament(Long tournamentId, Long memberId) {
    Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament", tournamentId));

    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));

    tournament.getParticipatingMembers().add(member);
    return toDTO(tournamentRepository.save(tournament));
}

public Set<Long> getMembersInTournament(Long tournamentId) {
    Tournament tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament", tournamentId));
    return tournament.getParticipatingMembers().stream()
            .map(Member::getId)
            .collect(Collectors.toSet());
}

public List<TournamentDTO> searchTournaments(LocalDate startDate, String location) {
    if (startDate != null) {
        return tournamentRepository.findByStartDate(startDate).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    if (location != null) {
        return tournamentRepository.findByLocationContaining(location).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    return getAllTournaments();
}

private Tournament fromDTO(TournamentDTO dto) {
    Tournament tournament = new Tournament();
    tournament.setId(dto.getId());
    tournament.setStartDate(dto.getStartDate());
    tournament.setEndDate(dto.getEndDate());
    tournament.setLocation(dto.getLocation());
    tournament.setEntryFee(dto.getEntryFee());
    tournament.setCashPrize(dto.getCashPrize());

    if (dto.getParticipatingMembers() != null) {
        Set<Member> members = dto.getParticipatingMembers().stream()
                .map(id -> memberRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Member", id)))
                .collect(Collectors.toSet());
        tournament.setParticipatingMembers(members);
    }

    return tournament;
}

private TournamentDTO toDTO(Tournament tournament) {
    TournamentDTO dto = new TournamentDTO();
    dto.setId(tournament.getId());
    dto.setStartDate(tournament.getStartDate());
    dto.setEndDate(tournament.getEndDate());
    dto.setLocation(tournament.getLocation());
    dto.setEntryFee(tournament.getEntryFee());
    dto.setCashPrize(tournament.getCashPrize());

    if (tournament.getParticipatingMembers() != null) {
        Set<Long> memberIds = tournament.getParticipatingMembers().stream()
                .map(Member::getId)
                .collect(Collectors.toSet());
        dto.setParticipatingMembers(memberIds);
    }

    return dto;
}
}


