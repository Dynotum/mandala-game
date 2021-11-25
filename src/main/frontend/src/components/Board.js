import React, {useState} from "react";
import axios from "axios";

const BASE_URL = "http://localhost:8080";

const getCurrentTurn = (gameState) => {
    const [player] = gameState.players.filter((player) => player.playerTurn);
    return {name: player.name, type: player.playerType};
};

export default function Board({gameState, setGameState, setScoreState}) {
    // eslint-disable-next-line react-hooks/rules-of-hooks
    const [turn, setTurn] = useState(getCurrentTurn(gameState));
    const [playerOne, playerTwo] = gameState.players;

    const handleBoardCellSelected = async (idx, playerType) => {
        const index = playerType === "PLAYER_TWO" ? Math.abs(idx - 11) : idx;
        const payload = {
            ...gameState,
            pit: index,
        };

        try {
            const newGameState = await axios.post(
                `${BASE_URL}/mancala/move`,
                payload
            );
            // newGameState.data
            const {data} = newGameState;
            setGameState(data);

            if (!data.gameEnded) {
                setTurn(getCurrentTurn(data));
            } else {
                const endPayload = {
                    players: data.players.map(({name, board: {bigPit}}) => ({
                        name,
                        bigPit,
                    })),
                };

                const scoreState = await axios.post(
                    `${BASE_URL}/mancala/end`,
                    endPayload
                );
                setScoreState(scoreState.data);
            }
        } catch (error) {
            const err = error?.response?.data;
            alert(`${err?.error}\n\n${err?.status}\n${err?.timestamp}`);
        }
    };

    const buildBoardForPlayer = (playerData) => {
        let pits = playerData.board.pits;

        if (playerData.playerType === "PLAYER_TWO") {
            pits = playerData.board.pits.slice().reverse();
        }

        return (
            <div>
                {/*pits*/}
                {pits.map((pit, idx) => (
                    <button
                        style={{
                            width: "80px",
                            height: "80px",
                            backgroundColor: "#EADDCA",
                            color: playerData.playerType === "PLAYER_ONE" ? "red" : "blue",
                            fontSize: "1rem",
                            cursor: "pointer",
                        }}
                        onClick={() => handleBoardCellSelected(idx, playerData.playerType)}
                    >
                        {pit}
                    </button>
                ))}
            </div>
        );
    };

    return (
        <div>
            <div style={{fontSize: "1.5rem"}}>Player Turn</div>
            {" "}
            <div
                style={{
                    fontSize: "1.5rem",
                    color: turn.name === playerOne.name ? "red" : "blue",
                }}
            >
                {turn.name}
            </div>
            <br/>
            <div>Player Two:</div>
            <div style={{color: "blue"}}>{playerTwo.name}</div>
            <div
                style={{
                    display: "flex",
                    flexDirection: "row",
                    justifyContent: "center",
                    alignItems: "center",
                    borderRadius: "10px",
                }}
            >
                {/* bigPit P2*/}
                <div
                    style={{
                        border: "4px solid black",
                        width: "5rem",
                        height: "10rem",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: "mediumspringgreen",
                        borderRadius: "10px",
                        fontSize: "2rem",
                        color: "blue",
                    }}
                >
                    {gameState.players[1].board.bigPit}
                </div>
                {/* under table*/}
                <div
                    style={{
                        border: "4px solid black",
                        width: "30rem",
                        height: "10rem",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: "lightblue",
                        borderRadius: "5px",
                    }}
                >
                    {buildBoardForPlayer(playerTwo)}
                    {buildBoardForPlayer(playerOne)}
                </div>
                {/* bigPit P1*/}
                <div
                    style={{
                        border: "4px solid black",
                        width: "5rem",
                        height: "10rem",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: "mediumspringgreen",
                        borderRadius: "10px",
                        fontSize: "2rem",
                        color: "red",
                    }}
                >
                    {gameState.players[0].board.bigPit}
                </div>
            </div>
            <div>
                Player One:
                <div/>
                <div style={{color: "red"}}>{playerOne.name}</div>
            </div>
        </div>
    );
}
