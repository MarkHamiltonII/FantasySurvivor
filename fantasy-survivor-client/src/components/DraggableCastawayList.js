import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";
import { BsGripVertical } from "react-icons/bs";

import AuthContext from "../AuthContext";

function DraggableCastawayList() {

    const auth = useContext(AuthContext);
    const { leagueId, id } = useParams();
    const [castaways, setCastaways] = useState([]);
    const [rating, setRating] = useState({});
    const [fetchAttempt, setFetchAttempt] = useState(false);

    useEffect(() => {

        const init = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            }
        };

        if (id === 'edit') {
            fetch(`${process.env.REACT_APP_API_URL}/api/rating/league${leagueId}/user${auth.user.appUserId}`, init)
                .then(response => {
                    if (response.status === 200) {
                        return response.json();
                    } else {
                        setFetchAttempt(true);
                        return Promise.reject(`Unexpected status code: ${response.status}`);
                    }
                })
                .then(d => {
                    setRating(d);
                    setCastaways(d.castaways);
                    setFetchAttempt(true);
                })
                .catch(console.log)
        }

        if (id === 'new') {
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
                    fetch(`${process.env.REACT_APP_API_URL}/api/castaway/season${data.seasonId}`, init)
                        .then(response => {
                            if (response.status === 200) {
                                return response.json();
                            } else {
                                setFetchAttempt(true);
                                return Promise.reject(`Unexpected status code: ${response.status}`);
                            }
                        })
                        .then(d => {
                            setCastaways(d);
                            setFetchAttempt(true);
                        })
                        .catch(console.log)
                        
                })
                .catch(console.log)
        }

    }, [auth.user.token, leagueId])

    if (!fetchAttempt) {
        return null;
    }

    const onDragEnd = function () {

    }


    return (
        <div className="mt-20 w-3/4 mx-auto">
            <div className="flex flex-col items-center w-fit mx-auto">
                <h2 className="font-survivor text-xl mb-4">Castaways</h2>
                <DragDropContext onDragEnd={onDragEnd} >
                    <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg w-full">
                        <Droppable droppableId="Table">
                            {provided => (
                                <table ref={provided.innerRef} {...provided.droppableProps} className="w-full text-sm text-left table-auto">
                                    <thead className="table-header-group bg-black text-white">
                                        <tr>
                                            <th className="px-2 text-center">Rank</th>
                                            <th className="px-2 text-center">Castaway</th>
                                            <th className="px-2 text-center">Age</th>
                                            <th className="px-2 text-center">Current Residence</th>
                                            <th className="px-2 text-center">Occupation</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {castaways.map((castaway, index) => (
                                            <Draggable draggableId={`${castaway.id}`} index={index} key={index}>
                                                {(provided) => (
                                                    <tr ref={provided.innerRef} {...provided.draggableProps} className="border-b" key={index} >
                                                        <td className="text-center">{index + 1}</td>
                                                        <td className="px-2 text-center"><a href={castaway.pageURL} title={`${castaway.firstName}'s CBS page`} target="_blank" className="flex text-center items-center" to={`/castaway${castaway.id}`}><img className="w-8 mr-2" src={castaway.iconURL} /> <span className="text-center underline">{castaway.firstName + " " + castaway.lastName}</span></a></td>
                                                        <td className="px-2 text-center">{castaway.age}</td>
                                                        <td className="px-2 text-center">{castaway.currentResidence}</td>
                                                        <td className="px-2 text-center">{castaway.occupation}</td>
                                                        <td {...provided.dragHandleProps} ><BsGripVertical /></td>
                                                    </tr>
                                                )}
                                            </Draggable>
                                        ))}
                                        {provided.placeholder}
                                    </tbody>
                                </table>
                            )}
                        </Droppable>
                    </div>
                </DragDropContext>
            </div>
        </div>
    )
}

export default DraggableCastawayList;