package com.game.mancala.unit.mapper;

import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerEndGameResponseDTO;
import com.game.mancala.mapper.MancalaMapper;
import com.game.mancala.utils.MancalaMockData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MancalaMapperUnitTest {

    @Autowired
    MancalaMapper mancalaMapper;

    private final MancalaMockData mancalaMockData = MancalaMockData.getInstance();

    @Test
    void startRequestDTONullException() {
        final StartRequestDTO startRequestDTO = null;
        final BoardResponseDTO boardResponseDTO = mancalaMockData.boardResponseDTO;

        assertThrows(ResponseStatusException.class, () -> mancalaMapper.startRequestDTOToMancalaResponseDTO(startRequestDTO, false, boardResponseDTO));
    }

    @Test
    void boardResponseDTONullException() {
        final StartRequestDTO startRequestDTO = mancalaMockData.startRequestDTO;
        final BoardResponseDTO boardResponseDTO = null;

        assertThrows(ResponseStatusException.class, () -> mancalaMapper.startRequestDTOToMancalaResponseDTO(startRequestDTO, true, boardResponseDTO));
    }

    @Test
    void moveRequestDTONullException() {
        final MoveRequestDTO moveRequestDTO = null;
        assertThrows(ResponseStatusException.class, () -> mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO, false));
    }

    @Test
    void PlayerEndGameResponseDTONullException() {
        final List<PlayerEndGameResponseDTO> playerEndGameResponseDTOList = null;
        assertThrows(ResponseStatusException.class, () -> mancalaMapper.endGameRequestDTOToEndGameResponse(false, playerEndGameResponseDTOList));
    }


}
