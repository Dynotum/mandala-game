package com.game.malanca.domain.dto.responses;

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