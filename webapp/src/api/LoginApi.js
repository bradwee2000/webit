import SecurityContext from './../security/SecurityContext'

const LoginApi = {

  login: function(username, password) {
    const requestOptions = {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "username": username,
            "password": password
          }
    };

    return fetch("http://localhost:8080/login", requestOptions)
      .then(res => res.json())
      .then(
        (result) => {
          SecurityContext.setToken(result.authToken);
        },
        (error) => {
          console.log(error)
        }
      )
  }
}

export default LoginApi
