package com.game.malanca.domain.dto.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StartRequestDTO {
    private List<PlayerRequestDTO> players;
}

