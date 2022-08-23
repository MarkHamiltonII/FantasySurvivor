import { useContext, useEffect, useState } from "react";
import { useHistory, useParams } from "react-router-dom";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";
import { BsGripVertical } from "react-icons/bs";

import AuthContext from "../AuthContext";
import Errors from "./Errors";

function DraggableCastawayList() {

    const auth = useContext(AuthContext);
    const history = useHistory();
    const { leagueId, id } = useParams();
    const [castaways, setCastaways] = useState([]);
    const [rating, setRating] = useState({});
    const [fetchAttempt, setFetchAttempt] = useState(false);
    const [errors, setErrors] = useState([]);

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

    }, [auth.user.token, auth.user.appUserId, id, leagueId])

    if (!fetchAttempt) {
        return null;
    }

    const onDragEnd = result => {
        const { destination, source } = result;

        if (!destination) {
            return;
        }

        if (destination.droppableId === source.droppableId &&
            destination.index === source.index) {
            return;
        }

        const newCastaways = [...castaways];
        const movedCastaway = newCastaways.splice(source.index,1);
        newCastaways.splice(destination.index,0,movedCastaway[0]);

        setCastaways(newCastaways);

        const newRating = {
            userId: auth.user.appUserId,
            leagueId: leagueId,
            castaways: newCastaways
        };

        setRating(newRating);
    };

    const handleSubmit = () => {
        let httpMethod = '';
        let endpoint = ''
        if (id === 'edit') {
            httpMethod = 'PUT';
            endpoint = process.env.REACT_APP_API_URL + '/api/rating/change_rating';
        } else {
            httpMethod = 'POST'
            endpoint = process.env.REACT_APP_API_URL + '/api/rating/new_rating';
        }

        const init = {
            method: httpMethod,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
            body: JSON.stringify(rating)
        };

        fetch(endpoint,init)
            .then(response => {
                if (response.status === 204 || response.status === 201) {
                    return null;
                } else if (response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (!data){
                    history.push(`/leaderboard/league${leagueId}`);
                } else {
                    setErrors([data.messages]);
                }
            })
            .catch(console.log)

    };


    return (
        <div className="mt-20 w-3/4 mx-auto">
            <div className="flex flex-col items-center w-fit mx-auto">
                <h2 className="font-survivor text-xl mb-4">Castaways</h2>
                <Errors errors={errors} />
                <DragDropContext onDragEnd={onDragEnd} >
                    <div className=" border-2 border-black overflow-x-auto relative shadow-md sm:rounded-lg w-full">
                        <Droppable droppableId="Table">
                            {(provided, snapshot) => (
                                <table ref={provided.innerRef} {...provided.droppableProps} className={`w-full text-sm text-left table-auto isDraggingOver-${snapshot.isDraggingOver} transition-all ease-linear duration-200`}>
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
                                            <Draggable draggableId={`${index}`} index={index} key={index}>
                                                {(provided, snapshot) => (
                                                    <tr ref={provided.innerRef} {...provided.draggableProps} className={`border-b ${'isDragging-'+snapshot.isDragging}`} key={index} >
                                                        <td className="text-center">{index + 1}</td>
                                                        <td className="px-2 text-center"><a href={castaway.pageURL} title={`${castaway.firstName}'s CBS page`} target="_blank" rel="noreferrer" className="flex text-center items-center" to={`/castaway${castaway.id}`}><img className="w-8 mr-2" src={castaway.iconURL} alt={`Portrait of ${castaway.firstName}`}/> <span className="text-center underline">{castaway.firstName + " " + castaway.lastName}</span></a></td>
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
                <button className="relative w-1/4 flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 mt-10" onClick={() => handleSubmit()}>Submit Rankings</button>
            </div>
        </div>
    )
}

export default DraggableCastawayList;