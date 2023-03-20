import { Link } from "react-router-dom";

function Leaderboard({ leaderboard }) {

    const sortedRankings = Object.entries(leaderboard.rankings)
        .sort(([, a], [, b]) => b - a)
        .reduce((r, [k, v]) => ({ ...r, [k]: v }), {});

    const Ranking = [];
    let current_ranking = 0;
    for (let ranking in sortedRankings) {
        if (current_ranking === 0) {
            current_ranking += 1;
        } else {
            if (Ranking[Ranking.length - 1]["points"] !== sortedRankings[ranking]) {
                current_ranking += 1;
            }
        }
        Ranking.push(
            {
                "rank": current_ranking,
                "username": ranking,
                "points": sortedRankings[ranking]
            }
        )
    }

    return (
        <div className="leaderboard-container">
            <h2 className="table-heading">{`Leaderboard for Tribal ${leaderboard.currentTribal}`}</h2>
            <table className="table table-hover table-sm" >
                <thead className="table-header">
                    <tr>
                        <th>Rank</th>
                        <th>Username</th>
                        <th>Points</th>
                    </tr>
                </thead>
                <tbody>
                    {Ranking.map((ranking, index) => (
                        <tr key={index} className={`rank-${ranking.rank}`}>
                            <td>{ranking.rank}</td>
                            <td> <Link to={`/${ranking.username}`}> {ranking.username}</Link></td>
                            <td>{ranking.points}</td>
                        </tr>
                    )
                    )}
                </tbody>
            </table>
        </div>
    )
}

export default Leaderboard;