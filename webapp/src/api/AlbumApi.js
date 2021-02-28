const AlbumApi = {

  get: function(albumId, successCallback, errorCallback) {
    fetch("http://localhost:8080/music/albums/" + albumId)
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
