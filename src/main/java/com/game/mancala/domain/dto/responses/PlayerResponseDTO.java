package com.game.mancala.domain.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerResponseDTO {
    private String name;
    private PlayerTypeResponse playerType;
    private boolean isPlayerTurn;
    private BoardResponseDTO board;

}
