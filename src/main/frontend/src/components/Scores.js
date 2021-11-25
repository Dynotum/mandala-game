import React from "react";
import "../App.css";

export default function Scores({ scoreState, onReset }) {
  const { tie, players } = scoreState;
  const sortedPlayers = players.sort((a, b) => b.winner - a.winner);

  // border: 4px solid;
  // width: 30rem;
  // height: 18rem;
  // flex-direction: column;
  // display: flex;
  // align-items: center;
  // padding: 10px;
  // margin: auto;

  return (
    <div>
      <h2>Game results:</h2>
      <div
        style={{
          border: "4px solid",
          width: "30rem",
          height: "18rem",
          flexDirection: "column",
          display: "flex",
          alignItems: "center",
          padding: "10px",
          margin: "auto",
        }}
      >
        <h3>{tie ? "The game was a tie 🤝" : `🥳 Winner 🥳`}</h3>
        <h3>{!tie ? ` ${sortedPlayers[0].name} 🏅🏆` : `🙂`}</h3>
        <div
          style={{
            fontWeight: "bold",
            paddingBottom: "15px",
            display: "flex",
            alignSelf: "stretch",
          }}
        >
          Scores:{" "}
        </div>
        {sortedPlayers.map(({ name, bigPit }) => (
          <div className="Score-element">
            <span>
              {name}: {bigPit} stones
            </span>
          </div>
        ))}
        <button style={{ padding: "10px" }} onClick={onReset}>
          Play again
        </button>
      </div>
    </div>
  );
}
