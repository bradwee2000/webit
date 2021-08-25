import { Config, Request } from './../Apis'

const AlbumRecommendationsApi = {

  get: function(page = 0, size = 6) {
    const url = Config.musicHost + "/recommendations?page=" + page + "&size=" + size
    return Request.get(url);
  },
}

export default AlbumRecommendationsApi
