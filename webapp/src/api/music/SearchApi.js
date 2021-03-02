import SecurityContext from './../../security/SecurityContext'

const SearchApi = {

  search: function(query, successCallback) {

    const requestOptions = {
        method: "GET",
        headers: {
          "Authorization": SecurityContext.getToken()
        }
    };

    fetch("http://localhost:8080/music/search/" + encodeURIComponent(query), requestOptions)
      .then(res => res.json())
      .then(
        (result) => {
          if (successCallback) {
            successCallback(result);
          }
        },
        (error) => {
          console.log(error)
        }
      )
  }
}

export default SearchApi
