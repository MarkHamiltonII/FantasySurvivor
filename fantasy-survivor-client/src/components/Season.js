import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import CastawayList from "./CastawayList";
import FormAction from "./FormAction";
import Header from "./Header";

// TODO: add onClick to submit to fetch Proper tribal (Starting will just be full case again)
// then compare lists and grey out the ones eliminated

function Season() {

    const auth = useContext(AuthContext);
    const { seasonId } = useParams();
    const [season, setSeason] = useState({});
    const [currentTribal, setCurrentTribal] = useState('Starting')
    const [tribalList, setTribalList] = useState(new Set(['Starting']))
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
            .then(data => data.map(d => setTribalList(tribalList.add(d))))
            .catch(console.log)
    }, [])

    const handleChange = (e) => {
        console.log(e.target.value)
        setCurrentTribal(e.target.value)
    }

    return (
        <>
            <Header heading={`Season ${seasonId} Homepage`} />
            <div className=" flex-col">
                <div className="flex items-center justify-center max-w-lg mx-auto mb-8">
                    <label className="mr-2 font-bold" htmlFor="tribalList">Current Tribal</label>
                    <select name="tribalList" id="tribalList" className="mx-4" onChange={handleChange}>
                        {[...tribalList].map(tl => (
                            <option key={tl} value={tl}>{tl}</option>
                        ))}
                    </select>
                    <button className="btn w-fit py-1 ml-3">Submit</button>
                </div>
                {fetchSeasonAttempt && <CastawayList castaways={season.castaways} />}
            </div>
        </>
    )

}

export default Season;