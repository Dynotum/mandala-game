package com.game.mancala.integration.mapper;

import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.PlayerMoveRequestDTO;
import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.EndGameResponseDTO;
import com.game.mancala.domain.dto.responses.MancalaResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerEndGameResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerResponseDTO;
import com.game.mancala.mapper.MancalaMapper;
import com.game.mancala.utils.MancalaMockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;
import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;
import static com.game.mancala.utils.Constants.EMPTY_PIT;
import static com.game.mancala.utils.MancalaMockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MancalaMapperIntegrationTest {

    @Autowired
    MancalaMapper mancalaMapper;

    private final MancalaMockData mancalaMockData = MancalaMockData.getInstance();

    @Test
    void startRequestDTOToMancalaResponseDTO() {
        final BoardResponseDTO boardResponseDTO = mancalaMockData.boardResponseDTO;
        final StartRequestDTO startRequestDTO = mancalaMockData.startRequestDTO;

        final List<PlayerResponseDTO> playerResponseDTOList = mancalaMockData.playerResponseDTOList;
        playerResponseDTOList.get(PLAYER_ONE.playerTypeValue).setBoard(boardResponseDTO);
        playerResponseDTOList.get(PLAYER_TWO.playerTypeValue).setBoard(boardResponseDTO);

        final MancalaResponseDTO mancalaResponseDTO = MancalaResponseDTO.builder()
                .players(playerResponseDTOList)
                .gameEnded(false)
                .build();

        mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(false);
        mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setPlayerTurn(true);

        final MancalaResponseDTO expectedMancalaResponseDTO = mancalaMapper.startRequestDTOToMancalaResponseDTO(startRequestDTO, false, boardResponseDTO);

        assertEquals(asJsonString(mancalaResponseDTO), asJsonString(expectedMancalaResponseDTO));
        assertEquals(mancalaResponseDTO, expectedMancalaResponseDTO);
    }

    @Test
    void moveRequestDTOToMancalaResponseDTO() {
        final BoardResponseDTO boardResponseDTO = BoardResponseDTO.builder()
                .bigPit(ZERO_BIG_PIT)
                .pits(PLAYER_INIT_BOARD)
                .build();

        final List<PlayerResponseDTO> playerResponseDTOList = Arrays.asList(
                PlayerResponseDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(EMPTY_PIT).
                                        pits(PLAYER_INIT_BOARD).build()
                        ).build(),
                PlayerResponseDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(EMPTY_PIT).
                                        pits(PLAYER_INIT_BOARD).build()
                        ).build());

        final List<PlayerMoveRequestDTO> playerMoveRequestDTOList = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(false)
                        .board(boardResponseDTO).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(true)
                        .board(boardResponseDTO).build()
        );

        final MoveRequestDTO moveRequestDTO =MoveRequestDTO.builder()
                .pit(EMPTY_PIT)
                .players(playerMoveRequestDTOList)
                .build();


        final MancalaResponseDTO expectedMancalaResponseDTO = MancalaResponseDTO.builder()
                .players(playerResponseDTOList)
                .gameEnded(false)
                .build();

        final MancalaResponseDTO mancalaResponseDTO = mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO, false);

        assertEquals(asJsonString(mancalaResponseDTO), asJsonString(expectedMancalaResponseDTO));
        assertEquals(expectedMancalaResponseDTO, mancalaResponseDTO);
    }

    @Test
    void endGameRequestDTOToEndGameResponse() {

        final List<PlayerEndGameResponseDTO> playerEndGameResponseDTOS = mancalaMockData.playerEndGameResponseDTOS;
        final EndGameResponseDTO expectedMancalaResponseDTO = mancalaMockData.endGameResponseDTO;
        final EndGameResponseDTO endGameResponseDTO = mancalaMapper.endGameRequestDTOToEndGameResponse(false, playerEndGameResponseDTOS);

        assertEquals(asJsonString(endGameResponseDTO), asJsonString(expectedMancalaResponseDTO));
        assertEquals(expectedMancalaResponseDTO, endGameResponseDTO);
    }
}