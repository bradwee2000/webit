import { useState, useEffect } from 'react'
import { useHistory } from 'react-router-dom';
import { SecurityContext } from './api/Apis';
import LoginPage from './components/LoginPage'

function Auth({children, loginPage}) {

  const history = useHistory()
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  const onLoginSuccess = () => {
    setIsLoggedIn(true);
  }

  useEffect(() => {
    if (!isLoggedIn) {
      history.push({pathname: '/login'})
    }
  }, [isLoggedIn]);


  if (!isLoggedIn) {
    return <><LoginPage onLoginSuccess={onLoginSuccess}/></>
  }

  return <>{children}</>;
}

export default Auth;
