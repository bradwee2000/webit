import TrackList from './TrackList'
import { Link } from 'react-router-dom'

const TrackSection = ({tracks, className='', userState, isPlaying, eventHandler, seeAllLink="", showAlbum=true, title="Tracks"}) => {
  if (tracks === undefined || tracks.length === 0) {
    return <></>;
  }

  const seeAllLinkComponent = seeAllLink === "" ? <></> : <Link to={seeAllLink} className="link">See All</Link>

  return (
    <section className={className}>
      <div className="d-flex justify-content-between">
        <h4>{title}</h4>
        <div className="mt-1">{seeAllLinkComponent}</div>
      </div>
      <TrackList items={tracks} userState={userState} isPlaying={isPlaying} showAlbum={showAlbum} eventHandler={eventHandler} />
    </section>
  )
}

export default TrackSection
