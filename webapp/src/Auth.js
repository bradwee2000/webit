import { useHistory } from 'react-router-dom';
import { useState, useEffect } from 'react'
import LoginPage from './components/LoginPage'
import SecurityContext from './security/SecurityContext'

function Auth({children, loginPage}) {

  const history = useHistory()

  const [isLoggedIn, setIsLoggedIn] = useState(SecurityContext.isLoggedIn())

  const onLoginSuccess = () => {
    setIsLoggedIn(SecurityContext.isLoggedIn());
  }

  useEffect(() => {
    if (!isLoggedIn) {
      history.push({pathname: '/login'})
    }
  }, [isLoggedIn, history]);

  if (!SecurityContext.isLoggedIn()) {
    return <><LoginPage onLoginSuccess={onLoginSuccess}/></>
  }

  return <>{children}</>;
}

export default Auth;
