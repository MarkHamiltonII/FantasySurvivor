import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Ranking from './components/Ranking';
import Leaderboard from './components/Leaderboard';

let leaderboard = require('./data/league2/leaderboard.json')
let league = require('./data/league2/league2.json')

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <p>
            Fantasy Survivor Season 44
          </p>
        </header>
        <div className='data-container'>
          <div className='item-container'>
            <Leaderboard leaderboard={leaderboard} />
          </div>
          <div className='item-container'>
            <Routes>
              <Route path='/' />
              <Route path='/:username' element={<Ranking />} />
            </Routes>
          </div>
        </div>
      </div>
    </Router>
  );
}

export default App;
