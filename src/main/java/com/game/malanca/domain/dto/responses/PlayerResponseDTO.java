package com.game.malanca.domain.dto.responses;

import lombok.*;

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
