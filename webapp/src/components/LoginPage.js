import { LoginApi } from './../api/Apis'
import { LoginButton } from './common/Commons';
import { useState } from 'react'
import { useHistory } from 'react-router-dom';

function LoginPage({onLoginSuccess}) {

  const history = useHistory()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [errorMsg, setErrorMsg] = useState('')

  const onLoginClick = function(e) {
    e.stopPropagation()
    e.preventDefault()

    LoginApi.login(username, password)
          .then(() => {
            onLoginSuccess()
            history.push({pathname: '/'})
          }).catch(e => {
            setErrorMsg(e.error)
          })
  }

  const handleUsernameChange = function(e) {
    setUsername(e.target.value)
  }

  const handlePasswordChange = function(e) {
    setPassword(e.target.value)
  }

  return (
    <>
      <div className="row justify-content-md-center mt-5 m-3">
        <div className="col-12 col-xs-4 col-sm-6 col-md-4 ">
          <form>
            <div className={`alert alert-danger ` + (errorMsg === "" ? "d-none" : "")} role="alert">
              {errorMsg}
            </div>
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <input type="text" name="" id="username" onChange={handleUsernameChange} className="form-control"/>
            </div>

            <div className="form-group mt-3">
              <label htmlFor="password">Password</label>
              <input type="password" name="" id="password" onChange={handlePasswordChange} className="form-control"/>
            </div>

            <div className="form-group mt-3">
              <LoginButton onClick={onLoginClick}/>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}

export default LoginPage;
