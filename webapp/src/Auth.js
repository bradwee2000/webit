import { useHistory } from 'react-router-dom';
import { useState, useEffect } from 'react'
import LoginPage from './components/LoginPage'
import SecurityContext from './security/SecurityContext'

function Auth({children, loginPage}) {

  const history = useHistory()

  const onLoginSuccess = () => {
    
  }

  useEffect(() => {
    if (!SecurityContext.isLoggedIn()) {
      history.push({pathname: '/login'})
    }
  }, [SecurityContext.isLoggedIn(), history]);


  if (!SecurityContext.isLoggedIn()) {
    return <><LoginPage onLoginSuccess={onLoginSuccess}/></>
  }

  return <>{children}</>;
}

export default Auth;
