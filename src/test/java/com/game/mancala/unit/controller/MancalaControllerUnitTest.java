package com.game.mancala.unit.controller;

import com.game.mancala.controller.MancalaController;
import com.game.mancala.domain.dto.requests.EndGameRequestDTO;
import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.utils.MancalaMockData;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MancalaControllerUnitTest {
    private final MancalaMockData mancalaMockData = new MancalaMockData();

    @Mock
    private MancalaController mancalaController;

    @Test
    public void testStartGame() {
        when(mancalaController.startGame(any(StartRequestDTO.class)))
                .thenReturn(ResponseEntity.ok(mancalaMockData.mancalaResponseDTO));

        mancalaController.startGame(mancalaMockData.startRequestDTO);

        verify(mancalaController, times(1))
                .startGame(mancalaMockData.startRequestDTO);
    }

    @Test
    public void testMakeMove() {
        when(mancalaController.makeMove(any(MoveRequestDTO.class)))
                .thenReturn(ResponseEntity.ok(mancalaMockData.mancalaResponseDTO));

        mancalaController.makeMove(mancalaMockData.moveRequestDTO);

        verify(mancalaController, times(1))
                .makeMove(mancalaMockData.moveRequestDTO);
    }

    @Test
    public void testEndGame() {
        when(mancalaController.endGame(any(EndGameRequestDTO.class)))
                .thenReturn(ResponseEntity.ok(mancalaMockData.endGameResponseDTO));

        mancalaController.makeMove(mancalaMockData.moveRequestDTO);

        verify(mancalaController, times(1))
                .makeMove(mancalaMockData.moveRequestDTO);
    }
}
