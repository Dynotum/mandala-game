package com.game.mancala.service;

import com.game.mancala.domain.dto.requests.*;
import com.game.mancala.domain.dto.responses.*;
import com.game.mancala.mapper.MancalaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

import static com.game.mancala.domain.dto.responses.PlayerTypeResponse.*;
import static com.game.mancala.utils.Constants.*;

@Slf4j
@Service
public class MancalaService {

    private final MancalaMapper mancalaMapper;

    @Autowired
    public MancalaService(MancalaMapper mancalaMapper) {
        this.mancalaMapper = mancalaMapper;
    }

    public MancalaResponseDTO startGame(StartRequestDTO startRequestDTO) {
        return mancalaMapper.startRequestDTOToMancalaResponseDTO(startRequestDTO, getRandomTurn(), initPlayerBoard());
    }

    public MancalaResponseDTO makeMove(MoveRequestDTO moveRequestDTO) {
        if (!isValidMoveRequest(moveRequestDTO)) {
            log.debug("Invalid move for player " + getCurrentPlayerTurn(moveRequestDTO).getName());
            throw new IllegalArgumentException("Invalid move for player " + getCurrentPlayerTurn(moveRequestDTO).getName());
        }

        final int[] fullBoard = getFullBoard(moveRequestDTO);
        doMove(fullBoard, moveRequestDTO);

        final boolean isEndedGame = boardPlayerIsFullZeros(moveRequestDTO);
        if (isEndedGame) {
            isEndedGame(moveRequestDTO);
        }

        return mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO, isEndedGame);
    }

    public EndGameResponseDTO endGame(EndGameRequestDTO endGameRequestDTO) {

        final PlayerEndGameRequestDTO playerOne = endGameRequestDTO.getPlayers().get(PLAYER_ONE.playerTypeValue);
        final PlayerEndGameRequestDTO playerTwo = endGameRequestDTO.getPlayers().get(PLAYER_TWO.playerTypeValue);

        final int bigPitPlayerOne = playerOne.getBigPit();
        final int bigPitPlayerTwo = playerTwo.getBigPit();

        final boolean isTie = bigPitPlayerOne == bigPitPlayerTwo;

        final PlayerEndGameResponseDTO playerEndGameResponseDTO1 = new PlayerEndGameResponseDTO(playerOne.getName(), playerOne.getBigPit(), false);
        final PlayerEndGameResponseDTO playerEndGameResponseDTO2 = new PlayerEndGameResponseDTO(playerTwo.getName(), playerTwo.getBigPit(), false);

        if (playerOne.getBigPit() > playerTwo.getBigPit()) {
            playerEndGameResponseDTO1.setWinner(true);
        } else if (playerOne.getBigPit() < playerTwo.getBigPit()) {
            playerEndGameResponseDTO2.setWinner(true);
        }

        return mancalaMapper.endGameRequestDTOToEndGameResponse(isTie, List.of(playerEndGameResponseDTO1, playerEndGameResponseDTO2));
    }

    private void doMove(int[] board, MoveRequestDTO moveRequestDTO) {
        final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);

        int pickedPit = moveRequestDTO.getPit();
        int stones = board[pickedPit];
        int bigPitCurrentPlayer = currentPlayerTurn.getBoard().getBigPit();
        boolean isCurrentPlayerTurn = false;

        if (stones == 0) {
            log.debug("That is an empty pit! Player: " + getCurrentPlayerTurn(moveRequestDTO).getName());
            throw new IllegalArgumentException("That is an empty pit! Player: " + getCurrentPlayerTurn(moveRequestDTO).getName());
        }
        System.out.println("######################");
        System.out.print("bp" + (1 + currentPlayerTurn.getPlayerType().playerTypeValue) + ": " + bigPitCurrentPlayer + " ");
        System.out.print(Arrays.toString(board));
        System.out.println();
        System.out.println("######################");

        // Take stones
        board[pickedPit] = 0;

        while (stones > 0) {
            pickedPit++;
            if (pickedPit == getLimitBoardPlayerType(currentPlayerTurn.getPlayerType())) {
                bigPitCurrentPlayer++;
                stones--;

                if (stones == 0) {
                    isCurrentPlayerTurn = true;
                    System.out.println("PLAYER " + currentPlayerTurn.getPlayerType() + " HAS AN EXTRA TURN");
                }

                if (stones > 0) {
                    if (PLAYER_TWO.equals(currentPlayerTurn.getPlayerType())) pickedPit = 0;
                    board[pickedPit]++;
                    stones--;
                }
                continue;
            }

            if (pickedPit == board.length) {
                pickedPit = 0;
            }

            board[pickedPit]++;
            stones--;
        }

        System.out.print("bp" + (1 + currentPlayerTurn.getPlayerType().playerTypeValue) + ": " + bigPitCurrentPlayer + " ");
        System.out.print(Arrays.toString(board));
        System.out.println();

        if (pickedPit < LIMIT_BOARD_PLAYER_TWO && board[pickedPit] == 1) {
            bigPitCurrentPlayer += getStolenStones(board, pickedPit);
            getFullBoardAfterStolen(board, pickedPit);
        }

        updateMove(moveRequestDTO, board, bigPitCurrentPlayer, isCurrentPlayerTurn);
    }

    private void updateMove(MoveRequestDTO moveRequestDTO, int[] board, int bigPitCurrentPlayer, boolean isCurrentPlayerTurn) {
        final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);
        final PlayerMoveRequestDTO oppositePlayer = moveRequestDTO.getPlayers().get(oppositeType(currentPlayerTurn.getPlayerType()).playerTypeValue);

        // update current bigPit player
        currentPlayerTurn.getBoard().setBigPit(bigPitCurrentPlayer);

        // update current player board
        currentPlayerTurn.getBoard().setPits(getPlayerBoard(currentPlayerTurn.getPlayerType(), board));

        // update opposite player board
        oppositePlayer.getBoard().setPits(getPlayerBoard(oppositePlayer.getPlayerType(), board));

        // update current player turn
        currentPlayerTurn.setPlayerTurn(isCurrentPlayerTurn);

        // update opposite player turn
        oppositePlayer.setPlayerTurn(!isCurrentPlayerTurn);

        moveRequestDTO.setPlayers(
                PLAYER_ONE.equals(currentPlayerTurn.getPlayerType()) ?
                        List.of(currentPlayerTurn, oppositePlayer) :
                        List.of(oppositePlayer, currentPlayerTurn));

    }

    private boolean getRandomTurn() {
        return new Random().nextBoolean(); // displaying a random boolean
    }

    private BoardResponseDTO initPlayerBoard() {
        final int[] pit = new int[NUMBER_OF_PITS];
        Arrays.fill(pit, NUMBER_OF_STONES);

        return new BoardResponseDTO(pit, ZERO_BIG_PIT);
    }

    private void isEndedGame(MoveRequestDTO moveRequestDTO) {
        final PlayerMoveRequestDTO currentPlayer = getCurrentPlayerTurn(moveRequestDTO);
        final PlayerMoveRequestDTO oppositePlayer = moveRequestDTO.getPlayers()
                .get(oppositeType(
                        currentPlayer.getPlayerType())
                        .playerTypeValue);

        final int newBigPit;
        if (boardPlayerIsFullZeros(currentPlayer)) {
            newBigPit = oppositePlayer.getBoard().getBigPit() + getSumAllStones(oppositePlayer.getBoard().getPits());
            oppositePlayer.getBoard().setBigPit(newBigPit);
        } else {
            newBigPit = currentPlayer.getBoard().getBigPit() + getSumAllStones(currentPlayer.getBoard().getPits());
            currentPlayer.getBoard().setBigPit(newBigPit);
        }

        moveRequestDTO.setPlayers(List.of(currentPlayer, oppositePlayer));
        moveRequestDTO.getPlayers().forEach(turn -> turn.setPlayerTurn(false));
    }

    private int getSumAllStones(int[] pits) {
        return IntStream.of(pits).sum();
    }

    private PlayerMoveRequestDTO getCurrentPlayerTurn(MoveRequestDTO moveRequestDTO) {
        return moveRequestDTO.
                getPlayers().stream().
                filter(PlayerMoveRequestDTO::isPlayerTurn).
                findAny().orElseThrow(() -> new IllegalArgumentException("There is not current player!"));
    }

    private int[] getFullBoardAfterStolen(int[] board, int pickedPit) {
        board[pickedPit] = 0;
        board[Math.abs(pickedPit - (LIMIT_BOARD_PLAYER_TWO - 1))] = 0;
        return board;
    }

    private int getStolenStones(int[] board, int pickedPit) {
        return board[pickedPit] + board[Math.abs(pickedPit - (LIMIT_BOARD_PLAYER_TWO - 1))];
    }

    private int[] getFullBoard(MoveRequestDTO moveRequestDTO) {
        return moveRequestDTO.getPlayers().stream()
                .map(PlayerMoveRequestDTO::getBoard)
                .map(BoardResponseDTO::getPits)
                .flatMapToInt(IntStream::of).toArray();
    }

    private int[] getPlayerBoard(PlayerTypeResponse playerType, int[] fullBoard) {
        if (PLAYER_ONE.equals(playerType))
            return Arrays.copyOfRange(fullBoard, START_BOARD_PLAYER_ONE, LIMIT_BOARD_PLAYER_ONE);

        return Arrays.copyOfRange(fullBoard, START_BOARD_PLAYER_TWO - 1, LIMIT_BOARD_PLAYER_TWO);
    }

    private static boolean boardPlayerIsFullZeros(MoveRequestDTO moveRequestDTO) {
        return moveRequestDTO.getPlayers().stream()
                .map(PlayerMoveRequestDTO::getBoard)
                .map(BoardResponseDTO::getPits)
                .anyMatch(pits -> IntStream.range(0, 6).allMatch(i -> pits[i] == 0));
    }

    private static boolean boardPlayerIsFullZeros(PlayerMoveRequestDTO player) {
        return Arrays.stream(player.getBoard().getPits()).allMatch(i -> i == 0);
    }

    /**
     * @return success whether is allowed to make a move. Otherwise, return false
     */
    private boolean isValidMoveRequest(MoveRequestDTO moveRequestDTO) {
        final int pickedPit = moveRequestDTO.getPit();
        final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);

        final PlayerTypeResponse currentPlayer = currentPlayerTurn.getPlayerType();
        if (PLAYER_ONE.equals(currentPlayer)) {
            return pickedPit >= START_BOARD_PLAYER_ONE && pickedPit <= LIMIT_BOARD_PLAYER_ONE - 1;
        }

        if (PLAYER_TWO.equals(currentPlayer)) {
            return pickedPit >= LIMIT_BOARD_PLAYER_ONE && pickedPit <= LIMIT_BOARD_PLAYER_TWO - 1;
        }

        return false;
    }
}