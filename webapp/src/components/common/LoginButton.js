import { LoginApi } from './../../api/Apis'
import { useHistory } from 'react-router-dom';

const LoginButton = ({playingTrack, eventHandler}) => {

  const history = useHistory()

  const handleClick = () => {
    LoginApi.login("brad", "DeezNuts")
      .then(() => {
        history.push({pathname: '/'})
        eventHandler.onLogin({name:"DeezNuts"})
      })
      .catch(e => console.log(e))
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
