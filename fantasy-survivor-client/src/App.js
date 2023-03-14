import jwtDecode from "jwt-decode";
import { useState, useEffect } from "react";
import { BrowserRouter as Router, Redirect, Route, Switch } from "react-router-dom";
import AuthContext from "./AuthContext";

import Home from "./components/Home";
import Login from "./components/Login";
import NavBar from "./components/NavBar";
import Register from "./components/Register";
import MyLeagues from "./components/MyLeagues";
import League from "./components/League";
import Castaway from "./components/Castaway";
import LeagueUser from "./components/LeagueUser";
import DraggableCastawayList from "./components/DraggableCastawayList";
import Confirmation from "./components/Confirmation";
import LeagueOptions from "./components/LeagueOptions";
import Seasons from "./components/Seasons";
import Season from "./components/Season"
import TribalList from "./components/TribalList";
import Rules from "./components/Rules";

const LOCAL_STORAGE_TOKEN_KEY = 'fantasySurvivorToken';

function App() {

  const [user, setUser] = useState(null);
  const [restoreLoginAttmept, setRestoreLoginAttempt] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
    if (token) {
      login(token)
    }
    setRestoreLoginAttempt(true);
  }, [])

  const login = (token) => {
    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token);

    const { sub: username, authorities, appUserId } = jwtDecode(token);
    const roles = authorities.split(',');

    const userToLogin = {
      username,
      appUserId,
      roles,
      token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };

    setUser(userToLogin);
  }

  const logout = () => {
    setUser(null);
    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
  }

  const auth = {
    user,
    login,
    logout
  }

  if (!restoreLoginAttmept) {
    return null;
  }

  return (
    <AuthContext.Provider value={auth}>
      <Router>
        <NavBar />
        <div className="main-container">
          <Switch>
            <Route path="/" exact>
              <Home />
            </Route>
            <Route path="/login">
              <Login />
            </Route>
            <Route path="/register">
              <Register />
            </Route>
            <Route path="/confirmation">
              <Confirmation />
            </Route>
            <Route path={["/myleagues", "/ownedleagues"]}>
              <MyLeagues />
            </Route>
            <Route path="/change_league/:leagueId">
              <LeagueOptions/>
            </Route>
            <Route path="/leaderboard/league:leagueId">
              <League />
            </Route>
            <Route path="/castaway:castawayId">
              <Castaway />
            </Route>
            <Route path="/league:leagueId/user:userId">
              <LeagueUser />
            </Route>
            <Route path="/league:leagueId/list/:id" >
              <DraggableCastawayList />
            </Route>
            <Route path="/seasons">
              <Seasons></Seasons>
            </Route>
            <Route path="/season/season:seasonId">
              <Season />
            </Route>
            <Route path="/season:seasonId/add_tribal">
              <TribalList />
            </Route>
            <Route path="/rules" >
              <Rules />
            </Route>

          </Switch>
        </div>
      </Router>
      {/* <div style={{ position: "absolute", top: "0", left: "0", backgroundImage: "url(https://images.unsplash.com/photo-1624043200446-1559c7854710?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80)", backgroundPosition: 'center', backgroundRepeat: 'no-repeat', width: "100%", height: "100%", opacity: ".8", zIndex: "-5" }}></div> */}
    </AuthContext.Provider>
  );
}

export default App;
