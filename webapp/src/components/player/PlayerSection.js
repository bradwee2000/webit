import { PlayerControl, PlayerOtherControls, TrackInfo } from './Player'

const PlayerSection = ({userState, isPlaying, eventHandler}) => {

  return (
    <section className="player">
      <div className="row">
        <div className="col my-auto">
          <div className="d-flex justify-content-start">
            <TrackInfo userState={userState} eventHandler={eventHandler}/>
          </div>
        </div>
        <div className="col my-auto">
          <PlayerControl userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </div>
        <div className="col my-auto">
          <div className="d-flex justify-content-end p-3">
            <PlayerOtherControls userState={userState} eventHandler={eventHandler}/>
          </div>
        </div>
      </div>
    </section>
  )
}

export default PlayerSection
