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
        // TODO check empty strings -> if error throw exception otherwise continue
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

        doMove(getFullBoard(moveRequestDTO), moveRequestDTO);


        // TODO after making a move we need to check whether there's an empty array (full of zeros) - that's the end of the game
        // If that's the case we need to take the rest of the stones belong to the player who still has stones and sum them up to their bigPit
        // we need to set flag endedGame as true in our responseDTO
        return mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO);
    }

    private PlayerMoveRequestDTO getPlayerTurn(MoveRequestDTO moveRequestDTO) {

        final Optional<PlayerMoveRequestDTO> currentTurnPlayer = moveRequestDTO.
                getPlayers().stream().
                filter(PlayerMoveRequestDTO::isPlayerTurn).
                findAny();

        return currentTurnPlayer.orElseThrow(() -> new IllegalArgumentException("Invalid move"));
    }

    private void doMove(int[] board, MoveRequestDTO moveRequestDTO) {
        final PlayerMoveRequestDTO currentPlayerTurn = getPlayerTurn(moveRequestDTO);

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
                System.out.print("bp1: " + bigPitCurrentPlayerTurn + " ");
                System.out.print(Arrays.toString(board) + " bp2: ");
                System.out.println();
                stones--;

                if (stones == 0) {
                    // TODO player has an extra turn -> DTO -> isMyTurn: true
                    boolean x = isEndedGame(board, currentPlayerTurn.getPlayerType());
                    System.out.println(x ? "is ended game " : "PLAYER ONE HAS AN EXTRA TURN");
                }

                if (stones > 0) {
                    board[pickedPit]++;
                    System.out.print("bp1: " + bigPitCurrentPlayerTurn + " ");
                    System.out.print(Arrays.toString(board) + " bp2: ");
                    System.out.println();
                    stones--;
                }
                continue;
            }

            if (pickedPit == board.length && PLAYER_TWO.equals(currentPlayerTurn.getPlayerType())) {
                bigPitCurrentPlayerTurn++;
                System.out.print("bp1: ");
                System.out.print(Arrays.toString(board) + " bp2: " + bigPitCurrentPlayerTurn);
                System.out.println();
                stones--;

                if (stones == 0) {
                    // TODO player has an extra turn -> DTO -> isMyTurn: true
                    boolean x = isEndedGame(board, currentPlayerTurn.getPlayerType());
                    System.out.println(x ? "is ended game " : "PLAYER 2 HAS AN EXTRA TURN");
                }

                if (stones > 0) {
                    pickedPit = 0;
                    board[pickedPit]++;
                    System.out.print("bp1: ");
                    System.out.print(Arrays.toString(board) + " bp2: " + bigPitCurrentPlayerTurn);
                    System.out.println();
                    stones--;
                }
                continue;
            } else if (pickedPit == board.length) {
                pickedPit = 0;
            }

            board[pickedPit]++;
            System.out.print("bp1: ");
            System.out.print(Arrays.toString(board) + " bp2: " + bigPitCurrentPlayerTurn);
            System.out.println();
            stones--;
        }
        // board[pickedPit] == 1 -> it was empty -> then picked enemies stones up -> sum them up to my bigPit

    }

    /*
     *
     *
     *           1 0 9
     *        7  1 0 6 6 6 0
     *
     *           8 6 6 6 0 0  7
     *           0 1 2
     *
     *
     *  5 * 2 = 10 - 1 // todo
     *
     *
     *
     *
     *      *      5 * 2 = 10
     *
     *   0 - 11
     *   1 - 10
     *        1  0 6 6 6 6 6
     *
     *           7 7 7 7 7 6  0
    *
    *
    * */

    private int[] getFullBoard(MoveRequestDTO moveRequestDTO) {
        return moveRequestDTO.getPlayers().stream()
                .map(PlayerMoveRequestDTO::getBoard)
                .map(BoardResponseDTO::getPits)
                .flatMapToInt(IntStream::of).toArray();
    }

    private static boolean isEndedGame(int[] board, PlayerTypeResponse playerType) {
        if (playerType.equals(PlayerTypeResponse.PLAYER_ONE))
            return IntStream.range(START_BOARD_PLAYER_ONE, LIMIT_BOARD_PLAYER_ONE).allMatch(i -> board[i] == EMPTY_PIT);

        return IntStream.range(START_BOARD_PLAYER_TWO, LIMIT_BOARD_PLAYER_TWO).allMatch(i -> board[i] == EMPTY_PIT);
    }

    private boolean isEndedGame(MancalaResponseDTO mancalaResponseDTO) {
        final PlayerResponseDTO player1 = mancalaResponseDTO.getPlayers().get(0);
        final PlayerResponseDTO player2 = mancalaResponseDTO.getPlayers().get(1);

        int[] result1 = player1.getBoard().getPits();
        int[] result2 = player2.getBoard().getPits();

        boolean p1 = true;
        boolean p2 = true;
        for (int number : result1) {
            if (number != 0) {
                p1 = false;
                break;
            }
        }
        for (int number : result2) {
            if (number != 0) {
                p2 = false;
                break;
            }
        }

        return p1 || p2;
    }


    /**
     * first
     * 0 1 2 3 4 5
     * <p>
     * 6 6 6 6 6 6
     * 6 6 6 6 6 6
     * <p>
     * 6 7 8 9 0 1
     * second
     *
     * @return success whether is allowed to make a move. Otherwise, return false
     */
    private boolean isValidMoveRequest(MoveRequestDTO moveRequestDTO) {
        final int pickedPit = moveRequestDTO.getPit();
        final PlayerMoveRequestDTO currentPlayerTurn = getPlayerTurn(moveRequestDTO);

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

/*
* int[] arr = new int[] {15, 1, 2, 3, 7, 9, 10};
int startIndex = 1;
int endIndex = 4;
int sum = Arrays.stream(arr, startIndex, endIndex)
         .sum();
System.out.println(sum);
* */