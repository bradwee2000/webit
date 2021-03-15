import { PlayerControl, PlayerOtherControls, TrackInfo } from './Player'

const PlayerSection = ({userState, isPlaying, selectedDevice, eventHandler}) => {

  return (
    <section className="player">
      <div className="row">

        <div className="col col-md-3 my-auto">
          <div className="d-flex justify-content-start">
            <TrackInfo userState={userState} eventHandler={eventHandler}/>
          </div>
        </div>

        <div className="col col-md-3 my-auto order-md-1">
          <div className="d-flex justify-content-end p-3">
            <PlayerOtherControls userState={userState} selectedDevice={selectedDevice} eventHandler={eventHandler}/>
          </div>
        </div>

        <div className="col-12 col-md my-auto">
          <PlayerControl userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </div>

      </div>
    </section>
  )
}

export default PlayerSection
