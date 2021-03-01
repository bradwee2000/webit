import SecurityContext from './../security/SecurityContext'

const LoginApi = {

  login: function(username, password, successCallback, errorCallback) {
    const requestOptions = {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "username": username,
            "password": password
          }
    };

    fetch("http://localhost:8080/login", requestOptions)
      .then(res => res.json())
      .then(
        (result) => {
          SecurityContext.setToken(result.authToken);
          if (successCallback) {
              successCallback();
          }
        },
        (error) => {
          if (errorCallback) {
            errorCallback(error);
          }
          console.log(error)
        }
      )
  }
}

export default LoginApi
