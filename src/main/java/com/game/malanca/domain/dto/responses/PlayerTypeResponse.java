package com.game.malanca.domain.dto.responses;

import java.util.List;

import static com.game.malanca.utils.Constants.RANDOM;

public enum PlayerTypeResponse {
    PLAYER_ONE(0), PLAYER_TWO(1);

    private static final List<PlayerTypeResponse> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    public final int playerTypeValue;

    PlayerTypeResponse(int playerTypeValue) {
        this.playerTypeValue = playerTypeValue;
    }

    public static PlayerTypeResponse randomType() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static PlayerTypeResponse missingType(PlayerTypeResponse playerType) {
        return playerType.equals(PLAYER_ONE) ? PLAYER_TWO : PLAYER_ONE;
    }
}
