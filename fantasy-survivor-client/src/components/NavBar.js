import { useContext, useState } from 'react';
import { BsPlus, BsLightningFill, BsGearFill, BsPersonFill, BsPersonPlusFill, BsDoorOpenFill, BsPeopleFill } from 'react-icons/bs';
import { FaFire, FaPoo, FaHome, FaTv, FaBars } from 'react-icons/fa';

import { Link } from "react-router-dom";
import AuthContext from '../AuthContext';

const NavBar = () => {
    const auth = useContext(AuthContext);
    const [showNavBar, setShowNavBar] = useState(false);

    const handleShowNavbar = () => {
        setShowNavBar(!showNavBar);
    }

    return (
        <div className="nav-bar fixed top-0 left-0 h-16 w-screen m-0 flex bg-gray-900 text-white shadow-lg z-50">
            <p className='font-survivor font-bold text-lg text-center my-auto mx-4' >FANTASY<br />SURVIVOR</p>
            <div className='menu-icon mx-auto mr-2 mt-4' onClick={handleShowNavbar}>
                <FaBars size="24" />
            </div>
            <div className={`nav-elements flex w-screen ${showNavBar && 'active flex-col'}`}>
                <span className='my-auto'>
                    <Link to="/">
                        <NavBarIcon icon={<FaHome size="24" />} text="Home" />
                    </Link>
                </span>
                <span className='my-auto'>
                    <Link to="/seasons">
                        <NavBarIcon icon={<FaTv size="24" />} text="Seasons" />
                    </Link>
                </span>
                {
                    auth.user && (
                        <span className='my-auto'>
                            <Link to="/myleagues">
                                <NavBarIcon icon={<FaFire size="24" />} text="My Leagues" />
                            </Link>
                        </span>
                    )
                }
                {
                    auth.user && auth.user.hasRole('ROLE_LEAGUE_OWNER') && (
                        <span className='my-auto'>
                            <Link to="/ownedleagues">
                                <NavBarIcon icon={<BsPeopleFill size="24" />} text="My Owned Leagues" />
                            </Link>
                        </span>
                    )
                }
                {/* <NavBarIcon icon={<BsPlus size="28" />} text="Join" />
                <NavBarIcon icon={<BsLightningFill size="24" />} />
                <NavBarIcon icon={<FaPoo size="24" />} /> */}
                {
                    !auth.user && !showNavBar && (
                        <span className='ml-auto my-auto flex'>
                            <Link to="/login"><NavBarIcon icon={<BsPersonFill size="24" />} text="Login" /></Link>
                            <Link to="/register"><NavBarIcon icon={<BsPersonPlusFill size="24" />} text="Register" /></Link>
                        </span>
                    )
                }
                {
                    auth.user && !showNavBar && (
                        <span className='ml-auto my-auto flex mr-4'>
                            <p className='text-lg mx-2 my-auto text-green-600'>Welcome, {auth.user.username}!</p>
                            <Link to="/"><NavBarIcon icon={<BsDoorOpenFill size="24" onClick={() => auth.logout()} />} text="Logout" /></Link>
                        </span>
                    )
                }
                {
                    !auth.user && showNavBar && (
                        <span className='my-auto'>
                            <Link to="/login"><NavBarIcon icon={<BsPersonFill size="24" />} text="Login" /></Link>
                        </span>
                    )
                }
                {
                    !auth.user && showNavBar && (
                        <span className='my-auto'>
                            <Link to="/register"><NavBarIcon icon={<BsPersonPlusFill size="24" />} text="Register" /></Link>
                        </span>
                    )
                }
                {
                    auth.user && showNavBar && (
                        <span className={`${showNavBar ? 'my-auto space-y-6' : 'ml-auto my-auto flex'}`}>
                            <p className='text-lg mx-2 my-auto text-green-600'>Welcome, {auth.user.username}!</p>
                        </span>
                    )
                }
                {
                    auth.user && showNavBar && (
                        <span className={`${showNavBar ? 'my-auto space-y-6' : 'ml-auto my-auto flex'}`}>
                            <Link to="/"><NavBarIcon icon={<BsDoorOpenFill size="24" onClick={() => auth.logout()} />} text="Logout" /></Link>
                        </span>
                    )
                }
            </div>
        </div >
    )
};

const NavBarIcon = ({ icon, text = 'tooltip 💡' }) => (
    <div className="sidebar-icon group">
        {icon}

        <span className='sidebar-tooltip group-hover:scale-100'>
            {text}
        </span>
    </div>
);

export default NavBar;