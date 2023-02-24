import { useContext, useEffect, useState } from "react";
import { Link, useHistory, useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import EditableCastawayList from "./EditableCastawayList";
import Errors from "./Errors";
import Header from "./Header";

function TribalList() {
    
    const auth = useContext(AuthContext);
    const history = useHistory();
    const { seasonId } = useParams();
    const [season, setSeason] = useState({});
    const [previousTribal, setPreviousTribal] = useState([])
    const [errors, setErrors] = useState([])
    const [isSure, setIsSure] = useState(false)
    const [tribalCastaways, setTribalCastaways] = useState([])
    const [currentTribal, setCurrentTribal] = useState('1')
    const [fetchSeasonAttempt, setFetchSeasonAttempt] = useState(false);

    useEffect(() => {

        fetch(`${process.env.REACT_APP_API_URL}/api/season/season${seasonId}`, { method: 'GET' })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                setSeason(data)
                setTribalCastaways(data.castaways)
                setPreviousTribal(data.castaways)
                setFetchSeasonAttempt(true)
            })
            .catch(console.log)

        fetch(`${process.env.REACT_APP_API_URL}/api/castaway/tribals/season${seasonId}`, { method: 'GET' })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                fetch(`${process.env.REACT_APP_API_URL}/api/castaway/season${seasonId}/tribal${data[data.length - 1]}`, { method: 'GET' })
                    .then(response => {
                        setCurrentTribal(data[data.length - 1])
                        if (response.status === 200) {
                            return response.json();
                        } else {
                            return Promise.reject(`Unexpected status code: ${response.status}`);
                        }
                    })
                    .then(data => {
                        setTribalCastaways(data)
                    })
                    .catch(console.log)
            })
            .catch(console.log)
    }, [])

    const addCastaway = (castaway) => {
        let tempCastaways = [...tribalCastaways];
        tempCastaways.push(castaway);
        setTribalCastaways(tempCastaways);
    }

    const removeCastaway = (castaway) => {
        setTribalCastaways(tribalCastaways.filter(c => c.id != castaway.id));
    }

    const onClick = () => {
        if (previousTribal === tribalCastaways && !isSure) {
            setErrors(["This is the same tribal as the previous tribal. Please hit submit again to confirm this is correct."])
            setIsSure(true)
            return;
        }

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
            body: JSON.stringify(tribalCastaways)
        };

        fetch(`${process.env.REACT_APP_API_URL}/api/castaway/season${seasonId}/tribal${currentTribal + 1}`,init)
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
                    history.push(`/season/season${seasonId}`);
                } else {
                    setErrors([data.messages]);
                }
            })
            .catch(console.log)
    }

    return (
        <>
            <Header heading={`Adding Tribal ${currentTribal + 1} to Season ${seasonId}`} />
            <Errors errors={errors} />
            {fetchSeasonAttempt && <EditableCastawayList castaways={season.castaways} tribal_castaways={tribalCastaways} on_add={addCastaway} on_delete={removeCastaway} />}
            <button className="btn w-fit mx-auto py-1 mb-4" onClick={onClick}>Submit</button>
        </>
    )
}

export default TribalList;