package com.game.mancala.mapper;

import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.PlayerRequestDTO;
import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.EndGameResponseDTO;
import com.game.mancala.domain.dto.responses.MancalaResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerEndGameResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;
import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;

@Slf4j
@Component
public class MancalaMapper {

    public MancalaResponseDTO startRequestDTOToMancalaResponseDTO(StartRequestDTO startRequestDTO, boolean turn, BoardResponseDTO boardResponseDTO) {
        if (startRequestDTO == null || boardResponseDTO == null) {
            log.info("Something goes wrong in " + MancalaMapper.class.getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something goes wrong in " + MancalaMapper.class.getSimpleName());
        }

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
        if (moveRequestDTO == null) {
            log.info("Something goes wrong in " + MancalaMapper.class.getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something goes wrong in " + MancalaMapper.class.getSimpleName());
        }

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

    public EndGameResponseDTO endGameRequestDTOToEndGameResponse(boolean isTie, List<PlayerEndGameResponseDTO> players) {
        if (players == null) {
            log.info("Something goes wrong in " + MancalaMapper.class.getSimpleName());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something goes wrong in " + MancalaMapper.class.getSimpleName());
        }
        return new EndGameResponseDTO(isTie, players);
    }
}