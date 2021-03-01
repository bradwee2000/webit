import LoginApi from './../../api/LoginApi'

const LoginButton = ({playingTrack, eventHandler}) => {

  const handleLoginSuccess = (result) => {

  };

  const handleLoginFail = (result) => {
      console.log(result);
  };

  const handleClick = () => {
    LoginApi.login("DeezNuts", "Hulala")
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
