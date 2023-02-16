function CastawayList({ castaways, tribal_castaways = castaways, rank = true }) {
    
    const stillThere = (castaway) => {
        let c = tribal_castaways.filter(tC => tC.id === castaway.id);
        return c.length > 0
    }

    return (
        <div className="flex flex-col items-center max-w-md mx-auto mb-4">
            <h2 className=" font-survivor text-xl mb-4">Castaways</h2>
            <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg ">
                <table className="w-full text-sm text-left">
                    <thead className="table-header-group bg-black text-white">
                        <tr>
                            {rank && <th>Rank</th>}
                            <th className="text-center">Castaway</th>
                        </tr>
                    </thead>
                    <tbody>
                        {castaways.filter(c=>stillThere(c)).map((castaway, index) => (
                            <tr className={castaway.tribeColor ? `border-b bg-${castaway.tribeColor}` : `border-b bg-slate-100`} key={index}>
                                {rank && <td className="text-center">{index + 1}</td>}
                                <td><a href={castaway.pageURL} title={`${castaway.firstName}'s CBS page`} target="_blank" rel="noreferrer" className="flex text-center items-center" to={`/castaway${castaway.id}`}><img className="w-8 mr-2" src={castaway.iconURL} alt='Castaway Portrait' /> <span className="text-center mr-auto pr-2">{castaway.firstName + " " + castaway.lastName}</span></a></td>
                            </tr>
                        ))}
                        {castaways.filter(c=>!stillThere(c)).map((castaway, index) => (
                            <tr className={`border-b bg-slate-800`} key={index}>
                                {rank && <td className="text-center">{index + 1}</td>}
                                <td><a href={castaway.pageURL} title={`${castaway.firstName}'s CBS page`} target="_blank" rel="noreferrer" className="flex text-center items-center" to={`/castaway${castaway.id}`}><img className="w-8 mr-2" src={castaway.iconURL} alt='Castaway Portrait' /> <span className="text-center mr-auto pr-2">{castaway.firstName + " " + castaway.lastName}</span></a></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default CastawayList;