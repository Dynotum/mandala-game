package com.game.mancala.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerEndGameResponseDTO {
    private String name;
    private int bigPit;
    private boolean winner;
}
