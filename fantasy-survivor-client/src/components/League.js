import { useContext, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { BsPencilSquare, BsPlusCircle } from "react-icons/bs";

import AuthContext from "../AuthContext";
import CastawayList from "./CastawayList";
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
    const [rating, setRating] = useState({});
    const [fetchLeagueAttempt, setFetchLeagueAttempt] = useState(false);
    const [fetchRatingAttempt, setFetchRatingAttempt] = useState(false);

    const { leagueId } = useParams();
    const auth = useContext(AuthContext);

    // Use Effect will find league and rankings
    useEffect(() => {
        const init = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            }
        };

        // Fetch for league
        fetch(`${process.env.REACT_APP_API_URL}/api/leagues/league${leagueId}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    setFetchLeagueAttempt(true);
                    setFetchRatingAttempt(true);
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                setLeague(data)
                // Fetch for leaderboard if league successful
                fetch(`${process.env.REACT_APP_API_URL}/api/leaderboard/season${data.seasonId}/league${data.leagueId}`, init)
                    .then(response => {
                        if (response.status === 200) {
                            return response.json();
                        } else {
                            setFetchLeagueAttempt(true);
                            return Promise.reject(`Unexpected status code: ${response.status}`);
                        }
                    })
                    .then(d => {
                        setLeaderboard(d);
                        setFetchLeagueAttempt(true);
                    })
                    .catch(console.log)

                // Fetch for rating if league successful
                fetch(`${process.env.REACT_APP_API_URL}/api/rating/league${data.leagueId}/user${auth.user.appUserId}`, init)
                    .then(response => {
                        if (response.status === 200) {
                            return response.json();
                        } else {
                            return Promise.reject(`Unexpected status code: ${response.status}`);
                        }
                    })
                    .then(d => {
                        setRating(d);
                        setFetchRatingAttempt(true);
                    })
                    .catch(console.log)
            })
            .catch(console.log)

    }, [auth.user.token, leagueId])


    return (
        <>
            <Header heading={`${league.name} Homepage`} />
            <div className="flex justify-evenly text-center">
                {leaderboard && fetchLeagueAttempt && <Leaderboard leaderboard={leaderboard} />}
                {(rating && fetchRatingAttempt) ? 
                    <div className="justify-center">
                        <h2 className=" font-survivor text-center text-xl mt-20">My Ranked</h2>
                        <CastawayList castaways={rating.castaways} />
                        <Link className="flex justify-center" to={`/league${league.leagueId}/list/edit`}>
                            <button className="flex mb-4 py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 mt-5"><BsPencilSquare className="mr-2 my-auto" />Edit</button>
                        </Link>
                    </div> 
                    :
                    <Link className="flex mt-20 justify-center" to={`/league${league.leagueId}/list/new`}><button className="flex my-auto py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 mt-5"><BsPlusCircle className="mr-2 my-auto" />Create List</button></Link>}
            </div>

        </>
    )
}

export default League;