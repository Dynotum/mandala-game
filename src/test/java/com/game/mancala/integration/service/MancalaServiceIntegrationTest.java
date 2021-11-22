package com.game.mancala.integration.service;

import com.game.mancala.domain.dto.requests.EndGameRequestDTO;
import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.PlayerEndGameRequestDTO;
import com.game.mancala.domain.dto.requests.PlayerMoveRequestDTO;
import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.EndGameResponseDTO;
import com.game.mancala.domain.dto.responses.MancalaResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerEndGameResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerResponseDTO;
import com.game.mancala.service.MancalaService;
import com.game.mancala.utils.MancalaMockData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;
import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;
import static com.game.mancala.utils.MancalaMockData.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class MancalaServiceIntegrationTest {

    @Autowired
    public MancalaServiceIntegrationTest(MancalaService mancalaService) {
        this.mancalaService = mancalaService;
    }

    private final MancalaService mancalaService;
    private final MancalaMockData mancalaMockData = MancalaMockData.getInstance();

    private int[] requestBoarPlayerOne;
    private int[] responseBoardPlayerOne;
    private int bigPitPlayerOne;
    private int finalbigPitPlayerOne;

    private int[] responseBoardPlayerTwo;
    private int[] requestBoarPlayerTwo;
    private int bigPitPlayerTwo;
    private int finalbigPitPlayerTwo;

    private int pitSelected;

    @Test
    void startGame() {
        final MancalaResponseDTO mancalaResponseDTO = mancalaService.startGame(mancalaMockData.startRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        final int[] playerOnePits = playerOne.getBoard().getPits();
        final int[] playerTwoPits = playerTwo.getBoard().getPits();

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertArrayEquals(PLAYER_INIT_BOARD, playerOnePits);
        assertArrayEquals(PLAYER_INIT_BOARD, playerTwoPits);

        assertEquals(playerOne.getPlayerType(), PLAYER_ONE);
        assertEquals(playerTwo.getPlayerType(), PLAYER_TWO);

        assertEquals(playerOne.getName(), PLAYER_ONE_NAME);
        assertEquals(playerTwo.getName(), PLAYER_TWO_NAME);

        assertEquals(playerOne.getBoard().getBigPit(), ZERO_BIG_PIT);
        assertEquals(playerTwo.getBoard().getBigPit(), ZERO_BIG_PIT);
    }

    @Test
    void makeMoveEndGame() {
        pitSelected = 5;
        requestBoarPlayerOne = new int[]{0, 0, 0, 0, 0, 1};
        requestBoarPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        responseBoardPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 3;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 36;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(BOARD_FULL_ZERO, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        System.out.println(playerOne.getBoard().getBigPit());
        System.out.println(playerTwo.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertTrue(mancalaResponseDTO.isGameEnded());

        assertFalse(playerOne.isPlayerTurn());
        assertFalse(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void makeRegularMovePlayerOne() {
        pitSelected = 1;
        requestBoarPlayerOne = new int[]{3, 2, 1, 5, 7, 6};
        requestBoarPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        responseBoardPlayerOne = new int[]{3, 0, 2, 6, 7, 6};
        responseBoardPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 2;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 5;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertFalse(playerOne.isPlayerTurn());
        assertTrue(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void invalidMovePlayerOne() {
        pitSelected = START_BOARD_PLAYER_TWO;
        requestBoarPlayerOne = new int[]{3, 2, 1, 5, 7, 6};
        requestBoarPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        responseBoardPlayerOne = new int[]{3, 0, 2, 6, 7, 6};
        responseBoardPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 2;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 5;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        assertThrows(IllegalArgumentException.class , () -> mancalaService.makeMove(moveRequestDTO));
    }

    @Test
    void makeRegularMovePlayerTwo() {
        pitSelected = 7;
        requestBoarPlayerOne = new int[]{3, 2, 1, 5, 7, 6};
        requestBoarPlayerTwo = new int[]{6, 3, 6, 6, 1, 6};
        responseBoardPlayerOne = new int[]{3, 2, 1, 5, 7, 6};
        responseBoardPlayerTwo = new int[]{6, 0, 7, 7, 2, 6};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 2;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 5;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());


        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertTrue(playerOne.isPlayerTurn());
        assertFalse(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void stealStonesFromPlayerTwo() {
        pitSelected = 2;
        requestBoarPlayerOne = new int[]{7, 2, 2, 9, 0, 4};
        requestBoarPlayerTwo = new int[]{3, 7, 2, 4, 1, 6};
        responseBoardPlayerOne = new int[]{7, 2, 0, 10, 0, 4};
        responseBoardPlayerTwo = new int[]{3, 0, 2, 4, 1, 6};
        bigPitPlayerOne = 10;
        finalbigPitPlayerOne = 19;
        bigPitPlayerTwo = 9;
        finalbigPitPlayerTwo = 8;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        assertEquals(18, playerOne.getBoard().getBigPit());
        assertEquals(9, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertTrue(playerTwo.isPlayerTurn());
        assertFalse(playerOne.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void stealStonesFromPlayerOne() {
        pitSelected = 6;
        requestBoarPlayerOne = new int[]{7, 2, 0, 10, 0, 4};
        requestBoarPlayerTwo = new int[]{5, 0, 2, 4, 1, 0};
        responseBoardPlayerOne = new int[]{0, 2, 0, 10, 0, 4};
        responseBoardPlayerTwo = new int[]{0, 1, 3, 5, 2, 0};
        bigPitPlayerOne = 18;
        finalbigPitPlayerOne = 18;
        bigPitPlayerTwo = 9;
        finalbigPitPlayerTwo = 17;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertTrue(playerOne.isPlayerTurn());
        assertFalse(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }

    @Test
    void endGameTie() {
        final PlayerEndGameRequestDTO playerOneRequest = PlayerEndGameRequestDTO.builder()
                .name(PLAYER_ONE_NAME)
                .bigPit(EQUAL_BIG_PIT)
                .build();

        final PlayerEndGameRequestDTO playerTwoRequest = PlayerEndGameRequestDTO.builder()
                .name(PLAYER_TWO_NAME)
                .bigPit(EQUAL_BIG_PIT)
                .build();

        final EndGameRequestDTO endGameRequestDTO = mancalaMockData.endGameRequestDTO;
        endGameRequestDTO.setPlayers(Arrays.asList(playerOneRequest, playerTwoRequest));

        final EndGameResponseDTO endGameResponseDTO = mancalaService.endGame(endGameRequestDTO);

        final PlayerEndGameResponseDTO playerOneResponse = endGameResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerEndGameResponseDTO playerTwoResponse = endGameResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertTrue(endGameResponseDTO.isTie());

        assertFalse(playerOneResponse.isWinner());
        assertFalse(playerTwoResponse.isWinner());

        assertEquals(PLAYER_ONE_NAME, playerOneRequest.getName());
        assertEquals(PLAYER_TWO_NAME, playerTwoResponse.getName());

        assertEquals(EQUAL_BIG_PIT, playerOneRequest.getBigPit());
        assertEquals(EQUAL_BIG_PIT, playerTwoResponse.getBigPit());
    }

    @Test
    void endGamePlayerOneWins() {
        final List<PlayerEndGameRequestDTO> playerEndGameRequestDTOList = Arrays.asList(
                PlayerEndGameRequestDTO.builder()
                        .name(PLAYER_ONE_NAME)
                        .bigPit(GREATEST_BIG_PIT)
                        .build(),
                PlayerEndGameRequestDTO.builder()
                        .name(PLAYER_TWO_NAME)
                        .bigPit(LOWEST_BIG_PIT)
                        .build());

        final EndGameRequestDTO endGameRequestDTO = mancalaMockData.endGameRequestDTO;
        endGameRequestDTO.setPlayers(playerEndGameRequestDTOList);

        final EndGameResponseDTO endGameResponseDTO = mancalaService.endGame(endGameRequestDTO);

        final PlayerEndGameResponseDTO playerOneResponse = endGameResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerEndGameResponseDTO playerTwoResponse = endGameResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertFalse(endGameResponseDTO.isTie());

        assertTrue(playerOneResponse.isWinner());
        assertFalse(playerTwoResponse.isWinner());

        assertEquals(PLAYER_ONE_NAME, playerEndGameRequestDTOList.get(PLAYER_ONE.playerTypeValue).getName());
        assertEquals(PLAYER_TWO_NAME, playerEndGameRequestDTOList.get(PLAYER_TWO.playerTypeValue).getName());

        assertEquals(GREATEST_BIG_PIT, playerEndGameRequestDTOList.get(PLAYER_ONE.playerTypeValue).getBigPit());
        assertEquals(LOWEST_BIG_PIT, playerEndGameRequestDTOList.get(PLAYER_TWO.playerTypeValue).getBigPit());
    }

    @Test
    void endGamePlayerTwoWins() {
        final PlayerEndGameRequestDTO playerOneRequest = PlayerEndGameRequestDTO.builder()
                .name(PLAYER_ONE_NAME)
                .bigPit(LOWEST_BIG_PIT)
                .build();
        final PlayerEndGameRequestDTO playerTwoRequest = PlayerEndGameRequestDTO.builder()
                .name(PLAYER_TWO_NAME)
                .bigPit(GREATEST_BIG_PIT)
                .build();

        final EndGameRequestDTO endGameRequestDTO = mancalaMockData.endGameRequestDTO;
        endGameRequestDTO.setPlayers(Arrays.asList(playerOneRequest, playerTwoRequest));

        final EndGameResponseDTO endGameResponseDTO = mancalaService.endGame(endGameRequestDTO);

        final PlayerEndGameResponseDTO playerOneResponse = endGameResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerEndGameResponseDTO playerTwoResponse = endGameResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertFalse(endGameResponseDTO.isTie());

        assertFalse(playerOneResponse.isWinner());
        assertTrue(playerTwoResponse.isWinner());

        assertEquals(PLAYER_ONE_NAME, playerOneRequest.getName());
        assertEquals(PLAYER_TWO_NAME, playerTwoResponse.getName());

        assertEquals(LOWEST_BIG_PIT, playerOneRequest.getBigPit());
        assertEquals(GREATEST_BIG_PIT, playerTwoResponse.getBigPit());
    }

    @Test
    void oneMoreTurnPlayerOne() {
        pitSelected = 5;
        requestBoarPlayerOne = new int[]{0, 0, 0, 0, 1, 1};
        requestBoarPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        responseBoardPlayerOne = new int[]{0, 0, 0, 0, 1, 0};
        responseBoardPlayerTwo = new int[]{6, 6, 6, 6, 1, 6};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 3;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 5;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        System.out.println(playerOne.getBoard().getBigPit());
        System.out.println(playerTwo.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertTrue(playerOne.isPlayerTurn());
        assertFalse(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());

    }

    @Test
    void oneMoreTurnPlayerTwo() {
        pitSelected = 10;
        requestBoarPlayerOne = new int[]{0, 0, 0, 0, 1, 1};
        requestBoarPlayerTwo = new int[]{6, 6, 6, 6, 2, 6};
        responseBoardPlayerOne = new int[]{0, 0, 0, 0, 1, 1};
        responseBoardPlayerTwo = new int[]{6, 6, 6, 6, 0, 7};
        bigPitPlayerOne = 2;
        finalbigPitPlayerOne = 2;
        bigPitPlayerTwo = 5;
        finalbigPitPlayerTwo = 6;

        final List<PlayerMoveRequestDTO> players = Arrays.asList(
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_ONE)
                        .name(PLAYER_ONE_NAME)
                        .isPlayerTurn(false)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerOne).
                                        pits(requestBoarPlayerOne).build()
                        ).build(),
                PlayerMoveRequestDTO.builder()
                        .playerType(PLAYER_TWO)
                        .name(PLAYER_TWO_NAME)
                        .isPlayerTurn(true)
                        .board(
                                BoardResponseDTO.builder()
                                        .bigPit(bigPitPlayerTwo).
                                        pits(requestBoarPlayerTwo).build()
                        ).build()
        );

        final MoveRequestDTO moveRequestDTO = mancalaMockData.moveRequestDTO;
        moveRequestDTO.setPit(pitSelected);
        moveRequestDTO.setPlayers(players);

        final MancalaResponseDTO mancalaResponseDTO = mancalaService.makeMove(moveRequestDTO);

        final PlayerResponseDTO playerOne = mancalaResponseDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerResponseDTO playerTwo = mancalaResponseDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        assertArrayEquals(responseBoardPlayerOne, playerOne.getBoard().getPits());
        assertArrayEquals(responseBoardPlayerTwo, playerTwo.getBoard().getPits());

        System.out.println(playerOne.getBoard().getBigPit());
        System.out.println(playerTwo.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerOne, playerOne.getBoard().getBigPit());
        assertEquals(finalbigPitPlayerTwo, playerTwo.getBoard().getBigPit());

        assertFalse(mancalaResponseDTO.isGameEnded());

        assertFalse(playerOne.isPlayerTurn());
        assertTrue(playerTwo.isPlayerTurn());

        assertEquals(PLAYER_ONE, playerOne.getPlayerType());
        assertEquals(PLAYER_TWO, playerTwo.getPlayerType());
    }
}