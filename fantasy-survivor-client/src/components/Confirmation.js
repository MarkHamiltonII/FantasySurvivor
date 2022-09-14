import { useHistory } from "react-router-dom";
import Header from "./Header";

function Confirmation() {
    const history = useHistory();
    return (
        <Header
            heading="Congrats!"
            paragraph={`${history.location.state.msg} account!`}
            linkName="Click here to login"
            linkUrl="/login"
        />
    )
}

export default Confirmation;