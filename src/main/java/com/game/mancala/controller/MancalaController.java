package com.game.mancala.controller;

import com.game.mancala.domain.dto.requests.EndGameRequestDTO;
import com.game.mancala.domain.dto.requests.MoveRequestDTO;
import com.game.mancala.domain.dto.requests.StartRequestDTO;
import com.game.mancala.domain.dto.responses.EndGameResponseDTO;
import com.game.mancala.domain.dto.responses.MancalaResponseDTO;
import com.game.mancala.service.MancalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mancala")
public class MancalaController {

    private final MancalaService mancalaService;

    @Autowired
    public MancalaController(MancalaService mancalaService) {
        this.mancalaService = mancalaService;
    }

    @PostMapping(path = "/start", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MancalaResponseDTO> startGame(@RequestBody StartRequestDTO startRequestDTO) {
        log.info("Hello mancala");
        return ResponseEntity.ok(mancalaService.startGame(startRequestDTO));
    }

    @PostMapping(path = "/move", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<MancalaResponseDTO> makeMove(@RequestBody MoveRequestDTO moveRequestDTO) {
        log.info("Pit move #" + moveRequestDTO.getPit());
        return ResponseEntity.ok(mancalaService.makeMove(moveRequestDTO));
    }

    @PostMapping(path = "/end", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EndGameResponseDTO> endGame(@RequestBody EndGameRequestDTO endGameRequestDTO) {
        log.info("End of game!");
        return ResponseEntity.ok(mancalaService.endGame(endGameRequestDTO));
    }
}