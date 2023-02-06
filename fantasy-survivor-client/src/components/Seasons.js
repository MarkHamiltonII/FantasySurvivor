import Header from "./Header";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../AuthContext";
import { useContext } from "react";
import { BsPencilSquare } from "react-icons/bs";

function Seasons() {

    const auth = useContext(AuthContext);
    const [seasons, setSeasons] = useState({});
    const [fetchAttempt, setFetchAttempt] = useState(false)

    useEffect(() => {

        fetch(`${process.env.REACT_APP_API_URL}/api/season/all`, { method: 'GET' })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    setFetchAttempt(true)
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data[0].seasonId) {
                    setSeasons(data)
                }
                setFetchAttempt(true);
            })
            .catch(console.log);

    }, [])

    if (!fetchAttempt) {
        return null;
    }

    return (
        <>
            <Header heading={"Seasons"} paragraph={'Seasons available for fantasy survivor'} />
            {seasons.map(season => (
                <div key={season.seasonId} className="max-w-sm border-2 rounded overflow-hidden bg-gray-500 shadow-lg mx-auto hover:border-2 hover:border-green-600 hover:z-10 transition-all duration-100 ease-linear font-bold text-3xl mb-4 underline text-ellipsis text-white hover:text-green-500 px-4 pt-6 pb-8 flex w-full" >
                    <Link className="cursor-pointer mx-auto" to={season.seasonId ? `/season/season${season.seasonId}` : "/"} >
                        {season.name}
                    </Link>
                    {auth.user && auth.user.hasRole('ROLE_ADMIN') &&
                        <Link className="" to={`/season/edit_season${season.seasonId}`}><button className="btn-blue p-2 ml-auto "><BsPencilSquare /></button></Link>
                    }
                </div>
            ))}
        </>
    )
}

export default Seasons;