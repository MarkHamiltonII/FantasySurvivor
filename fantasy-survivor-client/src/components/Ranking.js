import { useContext } from "react";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";
import Header from "./Header";

function Ranking() {

    const {leagueId} = useParams();
    const auth = useContext(AuthContext);

    return (
        <Header heading={`League ${leagueId} - Ranking for user id: ${auth.user.appUserId}`} />
    )
}

export default Ranking;