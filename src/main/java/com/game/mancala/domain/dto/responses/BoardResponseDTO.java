package com.game.mancala.domain.dto.responses;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BoardResponseDTO {

    private int[] pits;
    private int bigPit;
}