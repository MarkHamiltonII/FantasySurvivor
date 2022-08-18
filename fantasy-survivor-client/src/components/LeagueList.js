import { Link } from "react-router-dom";

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

    return (
    <>
        {leagues.map(league => (
                <div key={league.leagueId} className="max-w-sm border-2 rounded overflow-hidden shadow-lg mx-auto hover:border-2 hover:border-green-600 hover:z-10 transition-all duration-100 ease-linear" >
                    <div className="px-6 py-4">
                        <Link className=" cursor-pointer" to={league.leagueId ? `/leaderboard/league${league.leagueId}` : "/"} >
                            <div className="font-bold text-xl mb-2 underline">{league.name}</div>
                        </Link>
                        {league.appUsers && league.appUsers.map(user => (
                            <Link key={user.appUserId} className="w-fit group " to={`/league${league.leagueId}/user${user.appUserId}`}>
                                <p className="rounded-xl px-2 border-2 border-gray-300 text-gray-700 text-base w-fit group-hover:bg-green-600 group-hover:border-green-600 group-hover:text-white" key={user.appUserId}>{user.username}</p>
                            </Link>
                        ))}
                    </div>
                    <div className="px-6 pt-4 pb-2">
                        <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">Season {league.seasonId}</span>
                    </div>
                </div>
            ))
        }
    </>
    )
}

export default LeagueList;