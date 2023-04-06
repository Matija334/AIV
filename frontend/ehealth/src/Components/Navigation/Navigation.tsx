import {Link} from 'react-router-dom';
import './Navigation.css';

export default function Navigation() {
    return (
        <nav className="navbar">
            <div className="navbar-container">
                <ul className="nav-menu">
                    <li className="nav-item">
                        <Link to="/">Home</Link>
                    </li>
                    <li className="nav-item">
                        <Link to="/patientList">Patient list</Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
}