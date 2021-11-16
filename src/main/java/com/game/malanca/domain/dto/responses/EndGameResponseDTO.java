package com.game.malanca.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndGameResponseDTO {
    private boolean isTie;
    private List<PlayerEndGameResponseDTO> players;
}
