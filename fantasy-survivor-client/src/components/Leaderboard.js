function Leaderboard({ leaderboard }) {

    if (!leaderboard){
        return null;
    }
    
    const sortedRankings = Object.entries(leaderboard.rankings)
    .sort(([,a],[,b]) => b-a)
    .reduce((r, [k, v]) => ({ ...r, [k]: v }), {});


    return (
        <div className="flex flex-col mt-20 items-center mx-4">
            <h2 className=" font-survivor text-xl mb-4">{`Leaderboard for Tribal ${leaderboard.currentTribal}`} </h2>
            <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg w-full mx-2">
                <table className="w-full table-auto text-sm text-left">
                    <thead className="table-header-group bg-black text-white">
                        <tr>
                            <th>Rank</th>
                            <th>Username</th>
                            <th>Points</th>
                        </tr>
                    </thead>
                    <tbody>
                        {Object.keys(sortedRankings).map((key, index) => (
                            <tr key={index} className={`rank-${index}`}>
                                <td className={`rank-${index}`}>{index + 1}</td>
                                <td>{key}</td>
                                <td>{leaderboard.rankings[key]}</td>
                            </tr>
                        )
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default Leaderboard;