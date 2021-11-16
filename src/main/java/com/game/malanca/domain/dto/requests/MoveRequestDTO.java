package com.game.malanca.domain.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.game.malanca.domain.dto.responses.PlayerResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MoveRequestDTO {
    private int pit;
    private List<PlayerResponseDTO> players;
}
