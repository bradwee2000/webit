import { Config, SecurityContext } from './Apis'

const LoginApi = {

  login: function(username, password) {
    const requestOptions = {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
             username: username,
             password: password })
    };

    return fetch(Config.authHost + "/login", requestOptions)
        .then(res => res.json())
        .then(res => {
          if (res.error) {
            throw new Error(res.error)
          }
          return res
        })
        .then(res => SecurityContext.setToken(res.accessToken))
  }
}

export default LoginApi
