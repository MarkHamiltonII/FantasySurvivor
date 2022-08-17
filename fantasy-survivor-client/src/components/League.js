import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import Header from "./Header";
import Leaderboard from "./Leaderboard";

const EMPTY_LEADERBOARD = {
    currentTribal: "INVALID LEAGUE",
    rankings: {
        "Points Not Assigned": "Or No Tribals Yet"
    },
}

function League() {

    const [league, setLeague] = useState({});
    const [leaderboard, setLeaderboard] = useState(EMPTY_LEADERBOARD);
    const [fetchAttempt, setFetchAttempt] = useState(false);

    const { leagueId } = useParams();
    const auth = useContext(AuthContext);

    useEffect(() => {
        const init = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            }
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/leagues/league${leagueId}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    setFetchAttempt(true);
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                setLeague(data)
                fetch(`${process.env.REACT_APP_API_URL}/api/leaderboard/season${data.seasonId}/league${data.leagueId}`, init)
                    .then(response => {
                        if (response.status === 200) {
                            return response.json();
                        } else {
                            setFetchAttempt(true);
                            return Promise.reject(`Unexpected status code: ${response.status}`);
                        }
                    })
                    .then(d => {
                        setLeaderboard(d);
                        setFetchAttempt(true);
                    })
                    .catch(console.log)
            })
            .catch(console.log)
    }, [auth.user.token, leagueId])

    if (!fetchAttempt){
        return null;
    }


    return (
        <>
            <Header heading={`${league.name} Homepage`} />
            {leaderboard && <Leaderboard leaderboard={leaderboard} />}
            
        </>
    )
}

export default League;