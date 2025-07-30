package com.keyin.golfclub.controller;

import com.keyin.golfclub.dto.TournamentDTO;
import com.keyin.golfclub.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    private final TournamentService tournamentService;


    @PostMapping
    public ResponseEntity<TournamentDTO> createTournament(@RequestBody TournamentDTO dto) {
        return new ResponseEntity<>(tournamentService.createTournament(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TournamentDTO> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public TournamentDTO getTournamentById(@PathVariable Long id) {
        return tournamentService.getTournamentById(id);
    }

    @PutMapping("/{id}")
    public TournamentDTO updateTournament(@PathVariable Long id, @RequestBody TournamentDTO dto) {
        return tournamentService.updateTournament(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
    }

    @PostMapping("/{tournamentId}/members/{memberId}")
    public TournamentDTO addMemberToTournament(@PathVariable Long tournamentId, @PathVariable Long memberId) {
        return tournamentService.addMemberToTournament(tournamentId, memberId);
    }

    @GetMapping("/{tournamentId}/members")
    public Set<Long> getMembersInTournament(@PathVariable Long tournamentId) {
        return tournamentService.getMembersInTournament(tournamentId);
    }

    @GetMapping("/search")
    public List<TournamentDTO> searchTournaments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) String location) {
        return tournamentService.searchTournaments(startDate, location);
    }
}
