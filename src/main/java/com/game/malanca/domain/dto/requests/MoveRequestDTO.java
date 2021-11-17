package com.game.malanca.domain.dto.requests;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoveRequestDTO {
    private int pit;
    private List<PlayerMoveRequestDTO> players;
}
