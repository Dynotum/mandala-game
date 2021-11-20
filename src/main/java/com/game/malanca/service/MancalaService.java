package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.*;
import com.game.malanca.domain.dto.responses.*;
import com.game.malanca.mapper.MancalaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.*;
import static com.game.malanca.utils.Constants.*;

@Slf4j
@Service
public class MancalaService {

    private MancalaMapper mancalaMapper;

    @Autowired
    public MancalaService(MancalaMapper mancalaMapper) {
        this.mancalaMapper = mancalaMapper;
    }

    public MancalaResponseDTO startGame(StartRequestDTO startRequestDTO) {
        return mancalaMapper.startRequestDTOToMancalaResponseDTO(startRequestDTO, getRandomTurn(), initPlayerBoard());
    }

    private boolean getRandomTurn() {
        return new Random().nextBoolean(); // displaying a random boolean
    }

    private BoardResponseDTO initPlayerBoard() {
        final int[] pit = new int[NUMBER_OF_PITS];
        Arrays.fill(pit, NUMBER_OF_STONES);

        return new BoardResponseDTO(pit, ZERO_BIG_PIT);
    }

    /**
     * @param moveRequestDTO
     * @return
     */
    public MancalaResponseDTO makeMove(MoveRequestDTO moveRequestDTO) {

        if (!isValidMoveRequest(moveRequestDTO)) {
            log.debug("Not a valid move");
            throw new IllegalArgumentException("Invalid move");
        }

        int[] fullBoard = getFullBoard(moveRequestDTO);

        moveRequestDTO = doMove(fullBoard, moveRequestDTO);

        boolean isEndedGame = boardPlayerAllMatchZero(moveRequestDTO);

        if (isEndedGame) {
            isEndedGame(moveRequestDTO);
        }

        return mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO, isEndedGame);
    }

    private MoveRequestDTO isEndedGame(MoveRequestDTO moveRequestDTO) {
        final PlayerMoveRequestDTO currentPlayer = getCurrentPlayerTurn(moveRequestDTO);
        final PlayerMoveRequestDTO oppositePlayer = moveRequestDTO.getPlayers()
                .get(oppositeType(
                        currentPlayer.getPlayerType())
                        .playerTypeValue);

        final int newBigPit;

        if (boardPlayerAllMatchZero(currentPlayer)) {
            newBigPit = oppositePlayer.getBoard().getBigPit() + getSumAllStones(oppositePlayer.getBoard().getPits());
            oppositePlayer.getBoard().setBigPit(newBigPit);
        } else {
            newBigPit = currentPlayer.getBoard().getBigPit() + getSumAllStones(currentPlayer.getBoard().getPits());
            currentPlayer.getBoard().setBigPit(newBigPit);
        }

        moveRequestDTO.setPlayers(List.of(currentPlayer, oppositePlayer));
        moveRequestDTO.getPlayers().forEach(turn -> turn.setPlayerTurn(false));
        return moveRequestDTO;
    }

    private int getSumAllStones(int[] pits) {
        return IntStream.of(pits).sum();
    }


    private PlayerMoveRequestDTO getCurrentPlayerTurn(MoveRequestDTO moveRequestDTO) {
        return moveRequestDTO.
                getPlayers().stream().
                filter(PlayerMoveRequestDTO::isPlayerTurn).
                findAny().orElseThrow(() -> new IllegalArgumentException("Invalid move"));
    }

    /*    private void doMove(int[] board, MoveRequestDTO moveRequestDTO) {
            final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);

            int pickedPit = moveRequestDTO.getPit();
            int stones = board[pickedPit];
            int bigPitCurrentPlayerTurn = currentPlayerTurn.getBoard().getBigPit();

            if (stones == 0) {
                throw new IllegalArgumentException("There is an empty pit");
            }

            // Take stones
            board[pickedPit] = 0; //

            while (stones > 0) {
                pickedPit++;
                if (pickedPit == LIMIT_BOARD_PLAYER_ONE && PLAYER_ONE.equals(currentPlayerTurn.getPlayerType())) {
                    bigPitCurrentPlayerTurn++;
                    stones--;

                    if (stones == 0) {
                        boolean x = isEndedGame(board, currentPlayerTurn.getPlayerType());
                        System.out.println(x ? "is ended game " : "PLAYER " + currentPlayerTurn.getPlayerType() + " HAS AN EXTRA TURN");
                    }

                    if (stones > 0) {
                        board[pickedPit]++;
                        stones--;
                    }
                    continue;
                }

                if (pickedPit == board.length && PLAYER_TWO.equals(currentPlayerTurn.getPlayerType())) {
                    bigPitCurrentPlayerTurn++;
                    stones--;

                    if (stones == 0) {
                        boolean x = isEndedGame(board, currentPlayerTurn.getPlayerType());
                        System.out.println(x ? "is ended game " : "PLAYER " + currentPlayerTurn.getPlayerType() + " HAS AN EXTRA TURN");
                    }

                    if (stones > 0) {
                        pickedPit = 0;
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
        }*/
    private MoveRequestDTO doMove(int[] board, MoveRequestDTO moveRequestDTO) {

        final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);

        int pickedPit = moveRequestDTO.getPit();
        int stones = board[pickedPit];
        int bigPitCurrentPlayer = currentPlayerTurn.getBoard().getBigPit();
        boolean isCurrentPlayerTurn = currentPlayerTurn.isPlayerTurn();

        if (stones == 0) {
            throw new IllegalArgumentException("There is an empty pit");
        }
        System.out.println("######################");
        System.out.print("bp" + (1 + currentPlayerTurn.getPlayerType().playerTypeValue) + ": " + bigPitCurrentPlayer + " ");
        System.out.print(Arrays.toString(board));
        System.out.println();
        System.out.println("######################");

        // Take stones
        board[pickedPit] = 0; //

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


        // last movement


        // board[pickedPit] == 1 -> it was empty -> then picked enemies stones up -> sum them up to my bigPit
        if (board[pickedPit] == 1)
            System.out.println("get opponent stones!");
        // bigPit current player

        return updateMove(moveRequestDTO, board, bigPitCurrentPlayer, isCurrentPlayerTurn);

    }

    private MoveRequestDTO updateMove(MoveRequestDTO moveRequestDTO, int[] board, int bigPitCurrentPlayer, boolean isCurrentPlayerTurn) {

        final PlayerMoveRequestDTO currentPlayerTurn = getCurrentPlayerTurn(moveRequestDTO);

        final PlayerMoveRequestDTO currentPlayer = moveRequestDTO.getPlayers().get(currentPlayerTurn.getPlayerType().playerTypeValue);
        final PlayerMoveRequestDTO oppositePlayer = moveRequestDTO.getPlayers().get(oppositeType(currentPlayerTurn.getPlayerType()).playerTypeValue);

        // update current bigPit player
        currentPlayer.getBoard().setBigPit(bigPitCurrentPlayer);

        // update current player board
        currentPlayer.getBoard().setPits(getPlayerBoard(currentPlayerTurn.getPlayerType(), board));

        // update opposite player board
        oppositePlayer.getBoard().setPits(getPlayerBoard(oppositePlayer.getPlayerType(), board));

        // update current player turn
        currentPlayer.setPlayerTurn(isCurrentPlayerTurn);
        // TODO check draw/won
        // update opposite player turn
        oppositePlayer.setPlayerTurn(!isCurrentPlayerTurn);

        moveRequestDTO.setPlayers(List.of(currentPlayer, oppositePlayer));

        return moveRequestDTO;
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

    private static boolean boardPlayerAllMatchZero(MoveRequestDTO moveRequestDTO) {

        return moveRequestDTO.getPlayers().stream()
                .map(PlayerMoveRequestDTO::getBoard)
                .map(BoardResponseDTO::getPits)
                .anyMatch(pits -> IntStream.range(0, 6).allMatch(i -> pits[i] == 0));
    }

    private static boolean boardPlayerAllMatchZero(PlayerMoveRequestDTO player) {
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
            return pickedPit >= 0 && pickedPit <= 5;
        }

        if (PLAYER_TWO.equals(currentPlayer)) {
            return pickedPit >= 6 && pickedPit <= 11;
        }

        return false;
    }

    public EndGameResponseDTO endGame(EndGameRequestDTO endGameRequestDTO) {

        final PlayerEndGameRequestDTO player1 = endGameRequestDTO.getPlayers().get(0);
        final PlayerEndGameRequestDTO player2 = endGameRequestDTO.getPlayers().get(1);

        final int bigPitPlayer1 = player1.getBigPit();
        final int bigPitPlayer2 = player2.getBigPit();

        final boolean isTie = bigPitPlayer1 == bigPitPlayer2;

        final PlayerEndGameResponseDTO playerEndGameResponseDTO1 = new PlayerEndGameResponseDTO(player1.getName(), player1.getBigPit(), false);
        final PlayerEndGameResponseDTO playerEndGameResponseDTO2 = new PlayerEndGameResponseDTO(player2.getName(), player2.getBigPit(), false);

        if (player1.getBigPit() > player2.getBigPit()) {
            playerEndGameResponseDTO1.setWinner(true);
        } else if (player1.getBigPit() < player2.getBigPit()) {
            playerEndGameResponseDTO2.setWinner(true);
        }

        return new EndGameResponseDTO(isTie, List.of(playerEndGameResponseDTO1, playerEndGameResponseDTO2));
    }
}