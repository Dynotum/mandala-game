package com.game.malanca.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerEndGameResponseDTO {
    private String name;
    private int bigPit;
    private boolean isWinner;
}
