import { Link } from "react-router-dom";

function CastawayList({ castaways }) {

    return (
        <div className="flex flex-col items-center w-full">
            <h2 className=" font-survivor text-xl mb-4">Castaways</h2>
            <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg w-full">
                <table className="w-full text-sm text-left">
                    <thead className="table-header-group bg-black text-white">
                        <tr>
                            <th>Rank</th>
                            <th className="text-center">Castaway</th>
                        </tr>
                    </thead>
                    <tbody>
                        {castaways.map((castaway, index) => (
                            <tr className="border-b" key={index}>
                                <td className="text-center">{index + 1}</td>
                                <td><a href={castaway.pageURL} title={`${castaway.firstName}'s CBS page`} target="_blank" className="flex text-center items-center" to={`/castaway${castaway.id}`}><img className="w-8 mr-2" src={castaway.iconURL} /> <span className="text-center">{castaway.firstName + " " + castaway.lastName}</span></a></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default CastawayList;