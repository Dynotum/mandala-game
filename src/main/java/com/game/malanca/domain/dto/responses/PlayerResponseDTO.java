package com.game.malanca.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerResponseDTO {
    //TODO could be used by response and request
    private String name;
    private PlayerTypeResponse playerType;
    private boolean isPlayerTurn;
    private BoardResponseDTO board;

}
