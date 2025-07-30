package com.keyin.golfclub.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TournamentDTO {
    private Long id;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private double entryFee;
    private double cashPrize;
    private Set<Long> participatingMembers;

    // Additional fields can be added as needed

}
