package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.*;
import com.game.malanca.domain.dto.responses.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.*;
import static com.game.malanca.utils.Constants.*;

@Service
public class MancalaService {

    public MancalaResponseDTO startGame(StartRequestDTO startRequestDTO) {
        final List<PlayerResponseDTO> responseDTOArrayList = new ArrayList<>();
        final boolean randomTurn = getRandomTurn();

        for (PlayerRequestDTO player : startRequestDTO.getPlayers()) {
            final PlayerResponseDTO playerResponseDTO;
            if (responseDTOArrayList.isEmpty()) {
                playerResponseDTO = new PlayerResponseDTO(player.getName(), FIRST, randomTurn, initPlayerBoard());
            } else {
                playerResponseDTO = new PlayerResponseDTO(player.getName(), SECOND, !randomTurn, initPlayerBoard());
            }
            responseDTOArrayList.add(playerResponseDTO);
        }

        return new MancalaResponseDTO(responseDTOArrayList, false);
    }

    private boolean getRandomTurn() {
        return new Random().nextBoolean(); // displaying a random boolean
    }

    private BoardResponseDTO initPlayerBoard() {
        final int[] pit = new int[NUMBER_OF_PITS];
        Arrays.fill(pit, NUMBER_OF_STONES);

        return new BoardResponseDTO(pit, ZERO_BIG_PIT);
    }

    public MancalaResponseDTO makeMove(MoveRequestDTO moveRequestDTO) {
        if (!isValidMoveRequest(moveRequestDTO)) {
            System.out.println("not valid move - try again");
            return moveRequestDTOToMancalaResponseDTO(moveRequestDTO);
        }

        MancalaResponseDTO mancalaResponseDTO = moveRequestDTOToMancalaResponseDTO(moveRequestDTO);

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
/*        Optional<Integer> optional = Arrays.stream(result1)
                .filter(x -> x != 0)
                .boxed().findAny();

        Optional<Integer> optional2 = Arrays.stream(result2)
                .filter(x -> x != 0)
                .boxed().findAny();

        if (optional.isPresent()) {//Check whether optional has element you are looking for
            Integer p = optional.get();//get it from optional
            System.out.println("result --> " + p);
            return false;
        }*/


        return p1 || p2;
    }


    private MancalaResponseDTO moveRequestDTOToMancalaResponseDTO(MoveRequestDTO moveRequestDTO) {
        final PlayerResponseDTO playerResponseDTO = new PlayerResponseDTO(
                moveRequestDTO.getPlayers().get(0).getName(),
                moveRequestDTO.getPlayers().get(0).getPlayerType(),
                !moveRequestDTO.getPlayers().get(0).isPlayerTurn(), moveRequestDTO.getPlayers().get(0).getBoard());

        final PlayerResponseDTO playerResponseDTO2 = new PlayerResponseDTO(
                moveRequestDTO.getPlayers().get(1).getName(),
                moveRequestDTO.getPlayers().get(1).getPlayerType(),
                !moveRequestDTO.getPlayers().get(1).isPlayerTurn(),
                moveRequestDTO.getPlayers().get(1).getBoard());

        return new MancalaResponseDTO(List.of(playerResponseDTO, playerResponseDTO2), false);
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

        final PlayerMoveRequestDTO currentTurnPlayer = moveRequestDTO.
                getPlayers().stream().
                filter(PlayerMoveRequestDTO::isPlayerTurn).
                findAny().
                orElse(null); //TODO


        if (currentTurnPlayer.getPlayerType().equals(FIRST)) {
            return pit >= 0 && pit <= 5;
        }

        if (currentTurnPlayer.getPlayerType().equals(SECOND)) {
            return pit >= 6 && pit <= 11;
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
