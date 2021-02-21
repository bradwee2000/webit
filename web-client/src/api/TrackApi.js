const TrackApi = {

  get: function(trackId, successCallback, errorCallback) {
    fetch("http://localhost:8080/music/tracks/" + trackId)
      .then(res => res.json())
      .then(
        (result) => {
          console.log(result);
          successCallback(result);
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
    return "http://localhost:8080/music/tracks/" + trackId + "/play";
  }
}

export default TrackApi
