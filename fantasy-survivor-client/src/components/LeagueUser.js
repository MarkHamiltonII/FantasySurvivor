import { useParams } from "react-router-dom";
import Header from "./Header";

function LeagueUser() {

    const {leagueId, userId} = useParams();

    return (
        <Header heading={`League ${leagueId}: User ${userId}`} />
    )
}

export default LeagueUser;