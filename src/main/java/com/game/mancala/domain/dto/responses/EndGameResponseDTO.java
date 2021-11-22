package com.game.mancala.domain.dto.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EndGameResponseDTO {
    private boolean tie;
    private List<PlayerEndGameResponseDTO> players;
}
