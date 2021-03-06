import { Config, Request } from './../Apis'

const SearchApi = {

  search: function(query) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query));
  },

  searchTracks: function(query) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/tracks");
  },

  searchAlbums: function(query) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/albums");
  },
}

export default SearchApi
