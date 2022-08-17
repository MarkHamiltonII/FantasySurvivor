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
        <div className="max-w-sm rounded overflow-hidden shadow-lg mx-auto hover:border-2 hover:border-green-600 hover:z-10 transition-all duration-100 ease-linear cursor-pointer" >
            {leagues.map(league => (
                <Link key={league.leagueId} to={`/leaderboard/league${league.leagueId}`} >
                        <div className="px-6 py-4">
                            <div className="font-bold text-xl mb-2">{league.name}</div>
                            {league.appUsers && league.appUsers.map(user => (
                                <p className="text-gray-700 text-base" key={user.appUserId}>
                                    {user.username}
                                </p>
                            ))}
                        </div>
                        <div className="px-6 pt-4 pb-2">
                            <span className="inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2 mb-2">Season {league.seasonId}</span>
                        </div>
                </Link>
            ))}
        </div>
    )
}

export default LeagueList;