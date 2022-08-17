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
          <Route path="/myleagues">
            <MyLeagues />
          </Route>
          <Route path="/leaderboard/league:leagueId">
            <League />
          </Route>

        </Switch>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
