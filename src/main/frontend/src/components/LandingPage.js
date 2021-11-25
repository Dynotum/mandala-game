import React from "react";
import axios from "axios";
// import mocked from "../response1.json";

const BASE_URL = "http://localhost:8080";

export default function LandingPage({ gamePlayers, setGamePlayers, setGameState }) {
  const handleGameStart = async () => {
    const payload = {
      players: [
        {
          name: gamePlayers.playerOne || "P1",
        },
        {
          name: gamePlayers.playerTwo || "P2",
        },
      ],
    };
    const response = await axios.post(`${BASE_URL}/mancala/start`, payload);
    setGameState(response.data);
  };

  const handleOnChange = (evt) => {
    setGamePlayers((currentState) => ({
      ...currentState,
      [evt.target.name]: evt.target.value,
    }));
  };

  return (
    <div>
      <input
        name="playerOne"
        placeholder="Player One"
        onChange={(evt) => handleOnChange(evt)}
      />
      <br />
      <input
        name="playerTwo"
        placeholder="Player Two"
        onChange={(evt) => handleOnChange(evt)}
      />
      <br />
      <button type="submit" onClick={handleGameStart}>
        Start
      </button>
    </div>
  );
}
