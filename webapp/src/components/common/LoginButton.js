import { LoginApi, PlayCodeApi } from './../../api/Apis'

const LoginButton = ({playingTrack, eventHandler}) => {

  const handleClick = () => {
    const successCallback = PlayCodeApi.get;
    LoginApi.login("DeezNuts", "Hulala", successCallback);
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
