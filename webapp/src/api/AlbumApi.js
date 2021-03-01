import SecurityContext from './../security/SecurityContext'

const AlbumApi = {

  get: function(albumId, successCallback, errorCallback) {
    const requestOptions = {
        method: "GET",
        headers: {
          "Authorization": SecurityContext.getToken()
        }
    };

    fetch("http://localhost:8080/music/albums/" + albumId, requestOptions)
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
  }
}

export default AlbumApi
