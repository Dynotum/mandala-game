import React, { useState } from "react";
import Board from "./components/Board";
import LandingPage from "./components/LandingPage";
import Scores from "./components/Scores";

const PLAYERS_INITIAL_STATE = {
  playerOne: "",
  playerTwo: "",
};

const GAME_INITIAL_STATE = {
  players: [],
  gameEnded: true,
};

const SCORE_INITIAL_STATE = null;

export default function App() {
  const [gamePlayers, setGamePlayers] = useState(PLAYERS_INITIAL_STATE);
  const [gameState, setGameState] = useState(GAME_INITIAL_STATE);
  const [scoreState, setScoreState] = useState(SCORE_INITIAL_STATE);

  const { players, gameEnded } = gameState;
  const gameExists = players.length > 0;

  const onReset = () => {
    setGamePlayers(PLAYERS_INITIAL_STATE);
    setGameState(GAME_INITIAL_STATE);
    setScoreState(SCORE_INITIAL_STATE);
  };

  return (
    <div style={{ textAlign: "center" }}>
      <h1>Mancala Game</h1>
      {!gameExists && (
        <LandingPage
          gamePlayers={gamePlayers}
          setGamePlayers={setGamePlayers}
          setGameState={setGameState}
        />
      )}
      {gameExists && !gameEnded && (
        <Board
          gameState={gameState}
          setGameState={setGameState}
          setScoreState={setScoreState}
        />
      )}
      {scoreState && <Scores scoreState={scoreState} onReset={onReset} />}
    </div>
  );
}
