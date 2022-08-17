import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../AuthContext";
import Header from "./Header";
import LeagueList from "./LeagueList";

function MyLeagues() {

    const [leagues, setLeagues] = useState([]);
    const [fetchAttempt, setFetchAttempt] = useState(false);
    const auth = useContext(AuthContext);

    useEffect(() => {
        if (auth.user) {
            const init = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.user.token}`
                },
            };
            
            fetch(`${process.env.REACT_APP_API_URL}/api/leagues/user`, init)
                .then(response => {
                    if (response.status === 200) {
                        return response.json();
                    } else {
                        setFetchAttempt(true);
                        return Promise.reject(`Unexpected status code: ${response.status}`);
                    }
                })
                .then(data => {
                    if (data[0].leagueId) {
                        setLeagues(data);
                    }
                    setFetchAttempt(true);
                })
                .catch(console.log);
        };
    }, [auth.user])

    if (!fetchAttempt) {
        return null;
    }

    return (
        <>
            <Header heading='My Leagues' />
            {(leagues.length > 0) ? <LeagueList leagues={leagues}/>
            : <LeagueList />}            
        </>
    )
}

export default MyLeagues