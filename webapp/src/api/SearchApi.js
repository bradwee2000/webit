const SearchApi = {

  search: function(query, successCallback) {
    fetch("http://localhost:8080/music/search/" + encodeURIComponent(query))
      .then(res => res.json())
      .then(
        (result) => {
          successCallback(result);
        },
        (error) => {
          console.log(error)
        }
      )
  }
}

export default SearchApi
