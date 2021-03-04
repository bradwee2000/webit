import Config from './../../config/Config'
import SecurityContext from './../../security/SecurityContext'

const PlayCodeApi = {

  get: function(successCallback, errorCallback) {
    const requestOptions = {
        method: "GET",
        headers: {
          "Authorization": SecurityContext.getToken()
        }
    };

    return new Promise((resolve, reject) => {
      fetch(Config.musicHost + "/play-code", requestOptions)
        .then(res => res.json())
        .then(result => resolve(result),
          (error) => {
            console.log(error)
            reject(error)
          }
        )
    });
  }
}

export default PlayCodeApi
