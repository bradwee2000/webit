import SecurityContext from './../../security/SecurityContext'

const PlayCodeApi = {

  get: function(successCallback, errorCallback) {
    const requestOptions = {
        method: "GET",
        headers: {
          "Authorization": SecurityContext.getToken()
        }
    };

    fetch("http://localhost:8080/music/play-code/", requestOptions)
      .then(res => res.json())
      .then(
        (result) => {
          SecurityContext.setPlayTrackCode(result.playCode);
          if (successCallback) {
            successCallback(result);
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

export default PlayCodeApi
