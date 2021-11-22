package com.game.mancala.domain.dto.requests;

import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerTypeResponse;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PlayerMoveRequestDTO {
    private String name;
    private PlayerTypeResponse playerType;
    private boolean isPlayerTurn;
    private BoardResponseDTO board;

}
