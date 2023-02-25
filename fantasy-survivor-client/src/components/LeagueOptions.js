import { useContext, useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import EditableUserList from "./EditableUserList";
import Errors from "./Errors";
import Header from "./Header";
import { BsAwardFill } from "react-icons/bs";

function LeagueOptions() {

    const auth = useContext(AuthContext)
    const { leagueId } = useParams();
    const [errors, setErrors] = useState([]);
    const [league, setLeague] = useState({});
    const [users, setUsers] = useState([]);
    const [searchUsers, setSearchUsers] = useState([]);
    const [leagueUsers, setLeagueUsers] = useState([]);
    const [fetchAttempt, setFetchAttempt] = useState(false);
    const [pointsUpdated, setPointsUpdated] = useState(false);

    useEffect(() => {

        const init = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
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
                if (data.leagueId) {
                    setLeague(data);
                    setLeagueUsers(data.appUsers);
                }
                setFetchAttempt(true);
            })
            .catch(console.log);

        fetch(`${process.env.REACT_APP_API_URL}/api/users`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data[0].appUserId) {
                    setUsers(data);
                    setSearchUsers(data);
                }
            })
            .catch(console.log)
    }, [auth.user, leagueId])


    if (!fetchAttempt) {
        return null;
    }

    const updateLeagueUsers = (users) => {
        let tempLeague = {...league}
        tempLeague.appUsers = users;
        setLeague(tempLeague)
    }

    const addUser = (user) => {
        userFetchCall('POST', user);
    }

    const removeUser = (user) => {
        userFetchCall('DELETE', user)
    }

    const userFetchCall = (postOrDelete, user) => {
        const init = {
            method: postOrDelete,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/leagues/league${leagueId}/user${user.appUserId}`, init)
            .then(response => {
                if (response.status === 204) {
                    setErrors([])
                    return null;
                } else {
                    setErrors(['Cannot add/delete that user -- they may already have created a ranking for this league'])
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (!data) {
                    if (postOrDelete === 'POST') {
                        let tempUsers = [...leagueUsers];
                        tempUsers.push(user);
                        setLeagueUsers(tempUsers);
                        updateLeagueUsers(tempUsers);
                    } else if (postOrDelete === 'DELETE') {
                        setLeagueUsers(leagueUsers.filter(u => u.appUserId !== user.appUserId));
                        updateLeagueUsers(leagueUsers.filter(u => u.appUserId !== user.appUserId))
                    }
                } else {
                    setErrors([data.messages]);
                }
            })
            .catch(console.log)
    }

    const handleChange = (e) => {
        setSearchUsers(users.filter(u => u.username.includes(e.target.value)))
    }

    const updatePoints = () => {

        if (pointsUpdated) {
            return;
        }

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
            body: JSON.stringify(league)
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/points/league/all`, init)
            .then(response => {
                if (response.status === 201) {
                    setErrors([])
                    return null;
                } else if (response.status === 400) {
                    return response.json();
                } else {
                    console.log(response)
                    setErrors(response.messages)
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (!data)
                    setPointsUpdated(true)
                else {
                    if(data.messages)
                        setErrors(data.messages)
                    else
                    setErrors(data)
                }
            })
            .catch(console.log)
    }

    return (
        <>
            <Header heading={`League Options: ${league.name} - Season ${league.seasonId}`} />
            <Errors errors={errors} />
            <div className=" flex-col">
                <div className="flex items-center justify-center max-w-lg mx-auto mb-8">
                    <label className="font-bold" htmlFor="userSearch">Search Users</label>
                    <input type="text" name="userSearch" id="userSearch" className="mx-4" onChange={handleChange} placeholder="Username" />
                </div>
                <EditableUserList users={searchUsers} leage_users={leagueUsers} on_add={addUser} on_delete={removeUser} />
                <button className={pointsUpdated ? "btn-red w-fit mx-auto py-1 mb-4" : "btn-blue w-fit mx-auto py-1 mb-4"} onClick={updatePoints}><BsAwardFill /><p className="ml-2">Update Tribal Points</p></button>
            </div>
        </>
    )
}

export default LeagueOptions;