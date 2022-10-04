import { useContext, useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import AuthContext from "../AuthContext";
import Header from "./Header";
import LeagueList from "./LeagueList";

function MyLeagues() {

    const [leagues, setLeagues] = useState([]);
    const [fetchAttempt, setFetchAttempt] = useState(false);
    const [title, setTitle] = useState('Owned Leagues');
    const auth = useContext(AuthContext);
    const location = useLocation();

    useEffect(() => {
        if (auth.user) {
            const init = {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.user.token}`
                },
            };

            let endpont = '/api/leagues/owner';
            if (location.pathname === '/myleagues') {
                endpont = '/api/leagues/user';
                setTitle('My Leagues');
            }

            fetch(`${process.env.REACT_APP_API_URL}${endpont}`, init)
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
    }, [auth.user, location.pathname])


    if (!fetchAttempt) {
        return null;
    }

    return (
        <>
            <Header heading={title} />
            {(leagues.length > 0) ?
                <LeagueList leagues={leagues} />
                :
                <Header
                    paragraph={'Whoops, no leagues here!'}
                    linkName={'Return Home'}
                    linkUrl={'/'}
                />}
        </>
    )
}

export default MyLeagues