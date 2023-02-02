import { useContext } from "react";
import { Link } from "react-router-dom";
import AuthContext from "../AuthContext";
import { BsPlusCircle, BsPencilSquare, BsTrash } from "react-icons/bs";

const EMPTY_LEAGUES = [
    {
        "leagueId": 0,
        "name": "No Leagues Found",
        "seasonId": 42,
        "ownerId": 0,
        "appUsers": [
            {
                "username": "League Member",
                "appUserId": 0
            }
        ]
    }
]

function LeagueList({ leagues = EMPTY_LEAGUES }) {

    const auth = useContext(AuthContext);

    return (
        <>
            {leagues.map(league => (
                <div key={league.leagueId} className="max-w-sm border-2 rounded overflow-hidden bg-gray-500 shadow-lg mx-auto hover:border-2 hover:border-green-600 hover:z-10 transition-all duration-100 ease-linear" >
                    <div className="px-6 py-4">
                        <div className="flex justify-between w-full">
                            <Link className="cursor-pointer" to={league.leagueId ? `/leaderboard/league${league.leagueId}` : "/"} >
                                <div className="font-bold text-xl mb-4 underline text-ellipsis text-white hover:text-green-500">{league.name}</div>
                            </Link>
                            <div className="flex">
                                {(auth.user.appUserId === league.ownerId) &&
                                    <>
                                        <Link className="" to={`/change_league/${league.leagueId}`}><button className="btn-blue p-2 w-fit"><BsPencilSquare /></button></Link>
                                        <Link className="w-fit" to={`/delete_league/${league.leagueId}`}><button className="btn-red p-2 w-fit" ><BsTrash /></button></Link>
                                    </>}
                            </div>
                        </div>
                        {league.appUsers && league.appUsers.map(user => (
                            <Link key={user.appUserId} className="w-fit group " to={`/league${league.leagueId}/user${user.appUserId}`}>
                                <p className="rounded-xl px-2 border-2 bg-white border-gray-300 text-gray-700 font-bold text-base w-fit group-hover:bg-green-600 group-hover:border-green-600 group-hover:text-white" key={user.appUserId}>{user.username}</p>
                            </Link>
                        ))}
                    </div>
                    <div className="px-6 pt-4 pb-2 flex items-center justify-between">
                        <span className=" cursor-default inline-block bg-cyan-300 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">Season {league.seasonId}</span>

                    </div>
                </div>
            ))
            }
            {auth.user.hasRole('ROLE_LEAGUE_OWNER') && <Link className="flex justify-center mt-4" to='/new_league'><button className="btn max-w-xs shadow-lg"><BsPlusCircle className="mr-1" />Create League</button></Link>}
        </>
    )
}

export default LeagueList;