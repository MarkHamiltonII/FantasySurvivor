import Header from "./Header";

function Rules() {

    return (
        <>
            <Header heading="Fantasy Survivor Rules" paragraph="The goal of the game: get the most points." />
            <div className="text-center max-w-3xl mx-auto px-6 py-6 mb-4 bg-amber-100 border-2 border-black sm:rounded-lg">
                <p>The game does not begin until the second episode of the Survivor Season. Everyone gets the opportunity to watch the first episode and do as much research as they deem fit.</p>
                <h2 className="mt-10 text-center text-2xl font-extrabold text-gray-900 mb-2" >Rankings</h2>
                <p>After the first episode has aired, you will have until the beginning of the second episode to upload your rankings. In concept, this is simple: rank all the survivors from First to Last. You can put as much or as little thought into it as you want.</p>
                <img className="mx-auto my-4" src="/RankYourSurvivors.png" alt='Ranking' />
                <p>You can essentially treat your rankings as your own personal boot order for the cast as that is how the scoring will work going forward.</p>
                <h2 className="mt-10 text-center text-2xl font-extrabold text-gray-900 mb-2" >Scoring Points</h2>
                <p>Points will be awarded after each tribal council attended with a maximum of 100 points awarded per tribal council. After tribal has concluded, your list will be awarded points based on how accurate your order reflects the remaining castaways. This is done by taking 1 to # of remaining castaways of your list and comparing them to the castaways still in the game. Points will be awarded based on the percentage of your list that is correct </p>
                <img className="mx-auto my-4" src="/PointsPerTribal.png" alt='Points per Tribal' />
                <h2 className="mt-10 text-center text-2xl font-extrabold text-gray-900 mb-2" >Points Clarification</h2>
                <p>Points will be awarded after each tribal council attended. To be clear, this is <span className="font-bold">ONLY</span> for tribal councils attended and not whenever a cast member leaves the island. This means that tribal councils attended where no vote is cast (e.g. losing Do-Or-Die) will trigger scoring to occur. Meanwhile, med-evacs, quitting, or other emergencies that do not result in tribal council attendance will not trigger scoring to occur. On the other hand, if there is an episode that has two tribal councils, points will be scored twice that episode in the order that the tribals occured. <span className=" font-bold">NOTE:</span> this means that points are not necessarily awarded every episode episode. </p>
                <h2 className="mt-10 text-center text-2xl font-extrabold text-gray-900 mb-2" >Final Tribal</h2>
                <p>Final tribal points will be awarded differently than the rest. This time, you will be awarded points based on how high you ranked the winner. You will recieve 100 points if you have the winner ranked first, and 1/(total castaways) points if you have them last. </p>
                <img className="mx-auto my-4" src="/FinalTribal.png" alt='Final Tribal Points' />
                <p>Once the final tribal points are awarded, the player with the most points in the league <span className="font-bold">WINS!</span></p>
            </div>
        </>
    )
}

export default Rules;