import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import CastawayList from "./CastawayList";
import Header from "./Header";

// TODO: use currentTribal != 'Starting' to fetch tribal
// then compare lists and grey out the ones eliminated

function Season() {

    const auth = useContext(AuthContext);
    const { seasonId } = useParams();
    const [ season, setSeason ] = useState({});
    const [ currentTribal, setCurrentTribal ] = useState('Starting')
    const [ tribalList, setTribalList ] = useState(['Starting'])
    const [ fetchSeasonAttempt, setFetchSeasonAttempt ] = useState(false);

    useEffect(() => {

        fetch(`${process.env.REACT_APP_API_URL}/api/season/season${seasonId}`, {method: 'GET'})
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

        fetch(`${process.env.REACT_APP_API_URL}/api/castaway/tribals/season${seasonId}`, {method: 'GET'})
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => setTribalList(tribalList.concat(data)) )
            .catch(console.log)
    }, [seasonId])

    console.log(tribalList)
    return (
        <>
            <Header heading={`Season ${seasonId} Homepage`} />
            {fetchSeasonAttempt && <CastawayList castaways={season.castaways} />}
        </>
    )

}

export default Season;