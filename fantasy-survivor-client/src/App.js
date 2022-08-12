import jwtDecode from "jwt-decode";
import { useState } from "react";
import { BrowserRouter as Router, Redirect, Route, Switch } from "react-router-dom";
import AuthContext from "./AuthContext";
import Home from "./components/Home";


import Login from "./components/Login";
import NavBar from "./components/NavBar";
import Register from "./components/Register";

function App() {

  const [user, setUser] = useState(null);

  const login = (token) => {

    const { sub: username, authorities } = jwtDecode(token);
    const roles = authorities.split(',');

    const userToLogin = {
      username,
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
  }

  const auth = {
    user,
    login,
    logout
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
        </Switch>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;
