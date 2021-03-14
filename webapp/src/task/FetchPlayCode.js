import { PlayCodeApi, SecurityContext } from './../api/Apis';

const interval = 3 * 60 * 1000 // 3 mins

const FetchPlayCode = {
  run: () => {
    return PlayCodeApi.get()
      .then(result => SecurityContext.setPlayTrackCode(result.playCode))
      .catch(error => console.error("Failed to fetch play code", error));
  },

  schedule: () => {
    return setInterval(FetchPlayCode.run, interval)
  }
}

export default FetchPlayCode
