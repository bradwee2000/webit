import { Config, Request } from './../Apis'

const SearchApi = {

  search: function(query, page = 0, size = 6) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + queryParams(page, size));
  },

  searchByArtist: function(query, page = 0, size = 6) {
      return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + queryParams(page, size, 'artist'));
  },

  searchTracks: function(query, page = 0, size = 6) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/tracks" + queryParams(page, size));
  },

  searchTracksByArtist: function(query, page = 0, size = 6) {
      return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/tracks" + queryParams(page, size, 'artist'));
  },

  searchAlbums: function(query, page = 0, size = 6) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/albums" + queryParams(page, size));
  },

  searchAlbumsByArtist: function(query, page = 0, size = 6) {
    return Request.get(Config.musicHost + "/search/" + encodeURIComponent(query) + "/albums" + queryParams(page, size, 'artist'));
  },
}

function queryParams(page, size, searchType) {
    var params = '?';

    if (page) {
        params += '&page=' + page
    }

    if (size) {
        params += '&size=' + size
    }

    if (searchType) {
        params += '&type=' + searchType
    }

    return params;
}

export default SearchApi
