package com.game.malanca.domain.dto.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MancalaResponseDTO {

    private List<PlayerResponseDTO> players;
    private boolean gameEnded;
}