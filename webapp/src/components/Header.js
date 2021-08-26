import { useState } from 'react'
import { Link, useHistory } from 'react-router-dom';
import SecurityContext from '../security/SecurityContext'

const Header = () => {
    const history = useHistory()
    const [showAccountMenu, setShowAccountMenu] = useState(false)
    const userName = SecurityContext.getName()

    const onLogoutClick = function(e) {
        e.preventDefault()
        SecurityContext.logout()
        history.push({pathname: '/login'})
    }

    const onAccountClick = function(e) {
        e.preventDefault()
        setShowAccountMenu(!showAccountMenu)
    }

    return (
        <header className="my-3">
            <nav className="d-flex justify-content-between">
                <div>
                    <Link to="/" className="link pt-3">Home</Link>
                </div>
                <div>
                    <div className="link" onClick={onAccountClick}>{userName}</div>
                    <div className={'link ' + (showAccountMenu ? "" : "d-none")}>
                        <div onClick={onLogoutClick}>Logout</div>
                    </div>
                </div>
            </nav>
        </header>
    )
}

export default Header
