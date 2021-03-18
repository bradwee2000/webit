import { LoginApi } from './../../api/Apis'
import { useHistory } from 'react-router-dom';

const LoginButton = ({onLoginSuccess}) => {

  const history = useHistory()

  const handleClick = () => {
    LoginApi.login("brad", "DeezNuts")
      .then(() => {
        history.push({pathname: '/'})
        onLoginSuccess()
      }).catch(e => {
        console.error(e)
      })
  };

  return (
    <>
      <div className="m-2" onClick={handleClick}>
       Login
      </div>
    </>
  )
}

export default LoginButton
