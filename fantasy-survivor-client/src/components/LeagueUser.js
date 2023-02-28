import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import Header from "./Header";
import CastawayList from "./CastawayList";

function LeagueUser() {

    const auth = useContext(AuthContext);
    const { leagueId, userId } = useParams();
    const [rating, setRating] = useState({});
    const [fetchAttempt, setFetchAttmept] = useState(false)
    const [user, setUser] = useState({})
    const [userFetchAttempt, setUserFetchAttempt] = useState(false)

    useEffect(() => {

        const init = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            }
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/rating/league${leagueId}/user${userId}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(d => {
                setRating(d);
                setFetchAttmept(true);
            })
            .catch(console.log)

        fetch(`${process.env.REACT_APP_API_URL}/api/users/${userId}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    setUserFetchAttempt(true);
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(d => {
                setUser(d);
                setUserFetchAttempt(true);
            })
            .catch(console.log)

    }, [auth, userId, leagueId])

    return (
        <>
            {userFetchAttempt && <Header heading={`${user.username}'s Ranking`} />}
            {rating && fetchAttempt &&
                <div className="justify-center">
                    <CastawayList castaways={rating.castaways} />
                </div>}
        </>
    )
}

export default LeagueUser;