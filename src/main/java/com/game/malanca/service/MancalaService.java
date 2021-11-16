package com.game.malanca.service;

import com.game.malanca.domain.dto.requests.*;
import com.game.malanca.domain.dto.responses.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.game.malanca.domain.dto.responses.PlayerTypeResponse.*;
import static com.game.malanca.utils.Constants.*;

@Service
public class MancalaService {

    public MancalaResponseDTO startGame(StartRequestDTO startRequestDTO) {

        final List<PlayerResponseDTO> responseDTOArrayList = new ArrayList<>();
//        final PlayerTypeResponse playerType = PlayerTypeResponse.randomType();
//        final boolean isPlayerTurnFirst = playerType.equals(FIRST);
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

        return moveRequestDTOToMancalaResponseDTO(moveRequestDTO);
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
//        randomPlayerBoard(moveRequestDTO.getPlayers().get(1).getBoard().getPits()));

        return new MancalaResponseDTO(List.of(playerResponseDTO, playerResponseDTO2), false);
    }

    private boolean isValidMoveRequest(MoveRequestDTO moveRequestDTO) {

        final int pit = moveRequestDTO.getPit();

        final PlayerResponseDTO currentTurnPlayer = moveRequestDTO.
                getPlayers().stream().
                filter(PlayerResponseDTO::isPlayerTurn).
                findAny().
                orElse(null); //TODO


        if (currentTurnPlayer.getPlayerType().equals(FIRST)) {
            return pit >= 0 && pit <= 5;
        }

        if (currentTurnPlayer.getPlayerType().equals(SECOND)) {
            return pit >= 6 && pit <= 11;
        }

        /*
         *       first
         *       0 1 2 3 4 5
         *
         *       6 6 6 6 6 6
         *       6 6 6 6 6 6
         *
         *       6 7 8 9 0 1
         *       second
         *
         *
         * */

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
