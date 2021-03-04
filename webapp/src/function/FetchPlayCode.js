import { PlayCodeApi } from './../api/Apis';
import SecurityContext from './../security/SecurityContext'

const FetchPlayCode = () => {
  return PlayCodeApi.get()
    .then(result => SecurityContext.setPlayTrackCode(result.playCode));
}

export default FetchPlayCode
