import SecurityContext from './../../security/SecurityContext'

const TrackApi = {

  get: function(trackId, successCallback, errorCallback) {

    const requestOptions = {
        method: "GET",
        headers: {
          "Authorization": SecurityContext.getToken()
        }
    };

    fetch("http://localhost:8080/music/tracks/" + trackId, requestOptions)
      .then(res => res.json())
      .then(
        (result) => {
          console.log(result);
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
  },

  play: function(trackId) {
    const playToken = SecurityContext.getPlayTokenHash(trackId);
    return "http://localhost:8080/music/tracks/" + trackId + "/play?token=" + playToken;
  }
}

export default TrackApi
