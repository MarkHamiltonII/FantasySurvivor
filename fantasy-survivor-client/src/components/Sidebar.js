import {BsPlus, BsLightningFill, BsGearFill, BsPersonFill, BsDoorOpenFill} from 'react-icons/bs';
import {FaFire, FaPoo, FaHome} from 'react-icons/fa';

const Sidebar = () => {
    return (
        <div className="fixed top-0 left-0 h-16 w-screen m-0 flex bg-gray-900 text-white shadow-lg">
            <p className='font-survivor font-bold text-lg text-center my-auto mx-4' >FANTASY<br/>SURVIVOR</p>
            <SidebarIcon icon = {<FaHome size="24" />} text="Home" />
            <SidebarIcon icon = {<FaFire size="24" />} text="Leagues" />
            <SidebarIcon icon = {<BsPlus size="28" />} text="Join" />
            <SidebarIcon icon = {<BsLightningFill size="24" />} />
            <SidebarIcon icon = {<FaPoo size="24" />} />
            <span className='ml-auto my-auto'>
                <SidebarIcon icon = {<BsPersonFill size="24" />} text="Login" />
            </span>
        </div>
    )
};

const SidebarIcon = ({icon, text = 'tooltip 💡'}) => (
    <div className="sidebar-icon group">
        {icon}

        <span className='sidebar-tooltip group-hover:scale-100'>
            {text}
        </span>
    </div>
);

export default Sidebar;