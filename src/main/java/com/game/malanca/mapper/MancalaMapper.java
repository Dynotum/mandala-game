package com.game.malanca.mapper;


import com.game.malanca.domain.dto.requests.MoveRequestDTO;
import com.game.malanca.domain.dto.requests.PlayerRequestDTO;
import com.game.malanca.domain.dto.requests.StartRequestDTO;
import com.game.malanca.domain.dto.responses.BoardResponseDTO;
import com.game.malanca.domain.dto.responses.MancalaResponseDTO;
import com.game.malanca.domain.dto.responses.PlayerResponseDTO;
import com.game.malanca.domain.dto.responses.PlayerTypeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;
import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;

@Component
public class MancalaMapper {

    public MancalaResponseDTO startRequestDTOToMancalaResponseDTO(StartRequestDTO startRequestDTO, boolean turn, BoardResponseDTO boardResponseDTO) {
        final List<PlayerResponseDTO> playerResponseDTOs = new ArrayList<>();

        for (PlayerRequestDTO player : startRequestDTO.getPlayers()) {
            final PlayerResponseDTO playerResponseDTO = playerResponseDTOs.isEmpty() ?
                    new PlayerResponseDTO(player.getName(), PLAYER_ONE, turn, boardResponseDTO) :
                    new PlayerResponseDTO(player.getName(), PLAYER_TWO, !turn, boardResponseDTO);

            playerResponseDTOs.add(playerResponseDTO);
        }

        return MancalaResponseDTO.builder()
                .players(playerResponseDTOs)
                .gameEnded(false)
                .build();
    }

    public MancalaResponseDTO moveRequestDTOToMancalaResponseDTO(MoveRequestDTO moveRequestDTO, boolean isEndedGame) {
        final List<PlayerResponseDTO> playerResponseDtos = moveRequestDTO.getPlayers().stream()
                .map(player -> PlayerResponseDTO.builder()
                        .playerType(player.getPlayerType())
                        .isPlayerTurn(player.isPlayerTurn())
                        .name(player.getName())
                        .board(player.getBoard())
                        .build())
                .collect(Collectors.toList());

        return new MancalaResponseDTO(playerResponseDtos, isEndedGame);
    }
}