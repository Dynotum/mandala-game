package com.game.malanca.domain.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardResponseDTO {

    private int[] pits;
    private int bigPit;
}

/*
  "board": {
      "pits": [6,6,6,6,6,6],
      "bigPit": 0
  }
*/