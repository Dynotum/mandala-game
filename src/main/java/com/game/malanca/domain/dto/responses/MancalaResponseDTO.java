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
public class MancalaResponseDTO {

    private List<PlayerResponseDTO> players;
    private boolean gameEnded;
}

/*
 MancalaResponseDTO
 {
     "players": [
         {
             "name": "Lalin Canallin",
             "playerType": "FIRST",
             "isMyTurn": true,
             "board": {
                 "pits": [6,6,6,6,6,6],
                 "bigPit": 0
             }
         },
         {
             "name": "Dyno",
             "playerType": "SECOND",
             "isMyTurn": false,
             "board": {
                 "pits": [6,6,6,6,6,6],
                 "bigPit": 0
             }
         }
     ],
     "gameEnded" : false
 }*/
