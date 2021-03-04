import { Config, Request } from './../Apis'

const SearchApi = {

  search: function(query) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query));
  }
}

export default SearchApi
