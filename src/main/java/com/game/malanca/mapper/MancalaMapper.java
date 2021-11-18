package com.game.malanca.mapper;


import com.game.malanca.domain.dto.requests.MoveRequestDTO;
import com.game.malanca.domain.dto.requests.PlayerRequestDTO;
import com.game.malanca.domain.dto.requests.StartRequestDTO;
import com.game.malanca.domain.dto.responses.BoardResponseDTO;
import com.game.malanca.domain.dto.responses.MancalaResponseDTO;
import com.game.malanca.domain.dto.responses.PlayerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public MancalaResponseDTO moveRequestDTOToMancalaResponseDTO(MoveRequestDTO moveRequestDTO) {
        final PlayerResponseDTO playerResponseDTO = new PlayerResponseDTO(
                moveRequestDTO.getPlayers().get(0).getName(),
                moveRequestDTO.getPlayers().get(0).getPlayerType(),
                !moveRequestDTO.getPlayers().get(0).isPlayerTurn(), moveRequestDTO.getPlayers().get(0).getBoard());

        final PlayerResponseDTO playerResponseDTO2 = new PlayerResponseDTO(
                moveRequestDTO.getPlayers().get(1).getName(),
                moveRequestDTO.getPlayers().get(1).getPlayerType(),
                !moveRequestDTO.getPlayers().get(1).isPlayerTurn(),
                moveRequestDTO.getPlayers().get(1).getBoard());

        return new MancalaResponseDTO(List.of(playerResponseDTO, playerResponseDTO2), false);
    }
}

/*        List<PlayerResponseDTO> playerResponseDtos = startRequestDTO.getPlayers().stream()
                .map(player -> PlayerResponseDTO.builder()
                        .playerType(PlayerTypeResponse.PLAYER_ONE)
                        .isPlayerTurn(turn)
                        .name(player.getName())
                        .board(boardResponseDTO)
                        .build())
                .collect(Collectors.toList());*/