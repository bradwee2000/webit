import { PlayCodeApi, SecurityContext } from './../api/Apis';

const interval = 3 * 60 * 1000 // 3 mins

const FetchPlayCode = {
  run: () => {
    return PlayCodeApi.get()
      .then(result => SecurityContext.setPlayTrackCode(result.playCode));
  },

  schedule: () => {
    return setInterval(FetchPlayCode.run, interval)
  }
}

export default FetchPlayCode
