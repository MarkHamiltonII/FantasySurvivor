import Header from "./Header";

function Home() {

    return (
        <div className=" font-survivor">
            <Header heading="Welcome to Fantasy Survivor" paragraph="Login or make an account to continue." linkName="Login Here." linkUrl="./login" />
        </div>
    )
}

export default Home;