import { useParams } from "react-router-dom";
import Header from "./Header";

function LeagueOptions() {

    const { leagueId } = useParams();

    return (
        <Header heading={`League Options: League ${leagueId}`} />
    )
}

export default LeagueOptions;