package com.game.mancala.unit.service;

import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.PlayerMoveRequestDTO;
import com.game.mancala.domain.dto.responses.BoardResponseDTO;
import com.game.mancala.domain.dto.responses.PlayerTypeResponse;
import com.game.mancala.mapper.MancalaMapper;
import com.game.mancala.service.MancalaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_ONE;
import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.PLAYER_TWO;
import static com.game.mancala.utils.MancalaMockData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MancalaServiceUnitTest {

    @Autowired
    MancalaMapper mancalaMapper;

    @InjectMocks
    private MancalaService service = new MancalaService(mancalaMapper);

    @Test
    public void getSumAllStones() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getSumAllStones", int[].class);
        method.setAccessible(true);

        final int total = (int) method.invoke(service, PLAYER_INIT_BOARD);
        final int expectedTotal = 36;
        assertEquals(expectedTotal, total);
    }

    @Test
    public void getStolenStones() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getStolenStones", int[].class, int.class);
        method.setAccessible(true);

        final int[] pits = new int[]{1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 10};
        final int pickedPit = 0;
        final int total = (int) method.invoke(service, pits, pickedPit);
        final int expectedTotal = 11;

        assertEquals(expectedTotal, total);
    }

    @Test
    public void getFullBoardAfterStolen() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getFullBoardAfterStolen", int[].class, int.class);
        method.setAccessible(true);

        final int[] board = new int[]{1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 10};
        final int pickedPit = 0;
        final int[] expectedBoard = new int[]{0, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0};
        final int[] updatedBoard = (int[]) method.invoke(service, board, pickedPit);

        assertArrayEquals(expectedBoard, updatedBoard);
    }

    @Test
    public void getPlayerBoardPlayerOne() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getPlayerBoard", PlayerTypeResponse.class, int[].class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        final int[] playerOneBoard = (int[]) method.invoke(service,
                moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).getPlayerType(),
                FULL_BOARD_RANDOM_STONES);

        assertArrayEquals(PLAYER_ONE_BOARD_RANDOM_STONES, playerOneBoard);
    }

    @Test
    public void getPlayerBoardPlayerTwo() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getPlayerBoard", PlayerTypeResponse.class, int[].class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        final int[] playerTwoBoard = (int[]) method.invoke(service,
                moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).getPlayerType(),
                FULL_BOARD_RANDOM_STONES);

        assertArrayEquals(PLAYER_TWO_BOARD_RANDOM_STONES, playerTwoBoard);
    }

    @Test
    public void getFullBoard() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getFullBoard", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final int[] expectedBoard = (int[]) method.invoke(service, moveRequestDTO);

        assertArrayEquals(expectedBoard, FULL_BOARD);
    }


    @Test
    public void initPlayerBoard() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("initPlayerBoard");
        method.setAccessible(true);

        final BoardResponseDTO expectedBoardResponseDTO = getInstance().boardResponseDTO;
        final BoardResponseDTO boardResponseDTO = (BoardResponseDTO) method.invoke(service);

        assertEquals(expectedBoardResponseDTO, boardResponseDTO);
    }

    @Test
    public void getCurrentPlayerOneTurn() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getCurrentPlayerTurn", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final PlayerMoveRequestDTO playerOne = moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        playerOne.setPlayerTurn(true);
        moveRequestDTO.setPit(2);

        final PlayerMoveRequestDTO playerMoveRequestDTO = (PlayerMoveRequestDTO) method.invoke(service, moveRequestDTO);
        assertEquals(playerOne, playerMoveRequestDTO);
    }

    @Test
    public void getCurrentPlayerTwoTurn() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getCurrentPlayerTurn", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final PlayerMoveRequestDTO expectedPlayerMoveRequestDTO = moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);
        final PlayerMoveRequestDTO playerMoveRequestDTO = (PlayerMoveRequestDTO) method.invoke(service, moveRequestDTO);

        assertNotSame(expectedPlayerMoveRequestDTO, playerMoveRequestDTO);
    }

    @Test
    public void boardPlayerOneIsFullZeros() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", MoveRequestDTO.class);
        method.setAccessible(true);

        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        boardResponseDTO.setPits(BOARD_FULL_ZERO);
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);

        assertTrue(isBoardFullZeros);
    }

    @Test
    public void getNotCurrentPlayerTurn() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("getCurrentPlayerTurn", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(false);
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setPlayerTurn(false);

        // reflection layer wraps any underlying exception
        final Exception exception = assertThrows(InvocationTargetException.class, () -> method.invoke(service, moveRequestDTO), "There is not current player!");

        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
    }

    @Test
    public void boardPlayerOneIsNotFullZeros() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);

        assertFalse(isBoardFullZeros);
    }

    @Test
    public void boardPlayerTwoIsFullZeros() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", MoveRequestDTO.class);
        method.setAccessible(true);

        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        boardResponseDTO.setPits(BOARD_FULL_ZERO);
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);

        assertTrue(isBoardFullZeros);
    }

    @Test
    public void boardPlayerTwoIsNotFullZeros() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        boardResponseDTO.setPits(PLAYER_TWO_BOARD_RANDOM_STONES);

        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);

        assertFalse(isBoardFullZeros);
    }

    @Test
    public void boardPlayerOneIsFullZerosPlayerRequest() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", PlayerMoveRequestDTO.class);
        method.setAccessible(true);

        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        boardResponseDTO.setPits(BOARD_FULL_ZERO);
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isPlayerBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue));

        assertTrue(isPlayerBoardFullZeros);
    }

    @Test
    public void boardPlayerOneIsNotFullZerosPlayerRequest() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", PlayerMoveRequestDTO.class);
        method.setAccessible(true);

        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        boardResponseDTO.setPits(PLAYER_ONE_BOARD_RANDOM_STONES);
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue));

        assertFalse(isBoardFullZeros);
    }

    @Test
    public void boardPlayerTwoIsFullZerosPlayerReques() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", PlayerMoveRequestDTO.class);
        method.setAccessible(true);

        final BoardResponseDTO boardResponseDTO = getInstance().boardResponseDTO;
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;

        boardResponseDTO.setPits(BOARD_FULL_ZERO);
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setBoard(boardResponseDTO);

        final boolean isPlayerBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue));
        assertTrue(isPlayerBoardFullZeros);
    }

    @Test
    public void boardPlayerTwoIsNotFullZerosPlayerReques() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("boardPlayerIsFullZeros", PlayerMoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue));

        assertFalse(isBoardFullZeros);
    }

    @Test
    public void isValidMoveRequestPlayerOne() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("isValidMoveRequest", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(true);
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setPlayerTurn(false);
        moveRequestDTO.setPit(1);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);

        System.out.println(asJsonString(moveRequestDTO));
        System.out.println(isBoardFullZeros);

        assertTrue(isBoardFullZeros);
    }

    @Test
    public void isValidMoveRequestPlayerTwo() throws Exception {
        final Method method = MancalaService.class.getDeclaredMethod("isValidMoveRequest", MoveRequestDTO.class);
        method.setAccessible(true);

        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(false);
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setPlayerTurn(true);
        moveRequestDTO.setPit(LIMIT_BOARD_PLAYER_TWO - 1);

        final boolean isBoardFullZeros = (boolean) method.invoke(service, moveRequestDTO);
        System.out.println(asJsonString(moveRequestDTO));
        System.out.println(isBoardFullZeros);


        assertTrue(isBoardFullZeros);
    }

    @Test
    public void isInvalidMoveRequestPlayerOne() {
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(true);
        moveRequestDTO.setPit(START_BOARD_PLAYER_TWO);

        assertThrows(IllegalArgumentException.class, () -> service.makeMove(moveRequestDTO));
    }

    @Test
    public void isInvalidMoveRequestPlayerTwo() {
        final MoveRequestDTO moveRequestDTO = getInstance().moveRequestDTO;
        moveRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue).setPlayerTurn(true);
        moveRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue).setPlayerTurn(false);
        moveRequestDTO.setPit(START_BOARD_PLAYER_ONE);

        assertThrows(IllegalArgumentException.class, () -> service.makeMove(moveRequestDTO));
    }

}
