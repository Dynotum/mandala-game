package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.*;
import com.game.malanca.domain.dto.responses.*;
import com.game.malanca.mapper.MancalaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.*;
import static com.game.malanca.utils.Constants.*;

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
     *
     * @param moveRequestDTO
     * @return
     */
    public MancalaResponseDTO makeMove(MoveRequestDTO moveRequestDTO) {

        if (!isValidMoveRequest(moveRequestDTO)) {
            System.out.println("not valid move - try again");
            throw new IllegalArgumentException("Invalid move");
        }

        MancalaResponseDTO mancalaResponseDTO = mancalaMapper.moveRequestDTOToMancalaResponseDTO(moveRequestDTO);

        System.out.println("result of is Ended game? = " + isEndedGame(mancalaResponseDTO));


        // TODO after making a move we need to check whether there's an empty array (full of zeros) - that's the end of the game
        // If that's the case we need to take the rest of the stones belong to the player who still has stones and sum them up to their bigPit
        // we need to set flag endedGame as true in our responseDTO
        return mancalaResponseDTO;
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
        final int pit = moveRequestDTO.getPit();

        final Optional<PlayerMoveRequestDTO> currentTurnPlayer = moveRequestDTO.
                getPlayers().stream().
                filter(PlayerMoveRequestDTO::isPlayerTurn).
                findAny();

        if (currentTurnPlayer.isPresent()) {
            final PlayerTypeResponse currentPlayer = currentTurnPlayer.get().getPlayerType();
            if (PLAYER_ONE.equals(currentPlayer)) {
                return pit >= 0 && pit <= 5;
            }

            if (PLAYER_TWO.equals(currentPlayer)) {
                return pit >= 6 && pit <= 11;
            }
        }

        return false;
    }

    private BoardResponseDTO randomPlayerBoard(int[] board) {
        Arrays.fill(board, RANDOM.nextInt(NUMBER_OF_STONES));
        return new BoardResponseDTO(board, RANDOM.nextInt(20));
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
