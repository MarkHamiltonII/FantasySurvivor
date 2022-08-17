import { useParams } from "react-router-dom";
import Header from "./Header";

function Castaway() {

    const { castawayId } = useParams();

    return (
        <Header heading={`Castaway ${castawayId}`} />
    )
}

export default Castaway;