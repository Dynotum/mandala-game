package com.game.mancala.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

public class Constants {

    public static final int NUMBER_OF_STONES = 6;
    public static final int NUMBER_OF_PITS = 6;
    public static final int ZERO_BIG_PIT = 0;
    public static final int EMPTY_PIT = 0;

    public static final Random RANDOM = new Random();


    public static final int START_BOARD_PLAYER_ONE = 0;
    public static final int LIMIT_BOARD_PLAYER_ONE = 6;

    public static final int START_BOARD_PLAYER_TWO = 7;
    public static final int LIMIT_BOARD_PLAYER_TWO = 12;

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
