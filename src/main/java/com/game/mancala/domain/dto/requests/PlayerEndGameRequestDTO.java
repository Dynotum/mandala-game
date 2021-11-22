package com.game.mancala.domain.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerEndGameRequestDTO {
    private String name;
    private int bigPit;
}
