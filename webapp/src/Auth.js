import { useHistory } from 'react-router-dom';
import { useState, useEffect } from 'react'
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
  }, [isLoggedIn, history]);


  if (!isLoggedIn) {
    return <><LoginPage onLoginSuccess={onLoginSuccess}/></>
  }

  return <>{children}</>;
}

export default Auth;
