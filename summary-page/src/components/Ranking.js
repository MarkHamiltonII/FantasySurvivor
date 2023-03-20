import { useParams } from "react-router-dom"

export default function Ranking() {

    const {username} = useParams();
    const ranking = require("./../data/league2/"+username+".json")

    return (
        <div className="ranking-container">
            <h2 className="table-heading">{`${username}'s Rankings`}</h2>
            <table className="table table-hover table-sm" >
                <thead className="table-header">
                    <tr>
                        <th>Rank</th>
                        <th></th>
                        <th>Castaway</th>
                    </tr>
                </thead>
                <tbody>
                    {ranking.castaways.map((castaway, index) => (
                        <tr className={`bg-${castaway.tribeColor}`} key={index}>
                            <td>{index + 1}</td>
                            <td className="headshot"><img src={castaway.iconURL} alt='Castaway Portrait' /></td><td><a href={castaway.pageURL} title={`${castaway.firstName}'s Survivor Wiki page`} target="_blank" rel="noreferrer" to={`/castaway${castaway.id}`}><span>{castaway.firstName + " " + castaway.lastName}</span></a></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
}