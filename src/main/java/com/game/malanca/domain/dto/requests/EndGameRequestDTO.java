package com.game.malanca.domain.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EndGameRequestDTO {
    private List<PlayerEndGameRequestDTO> players;
}
