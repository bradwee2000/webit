import TrackList from './TrackList'
import { Link, useLocation } from 'react-router-dom'

const TrackSection = ({tracks, className='', userState, isPlaying, eventHandler, seeAllLink="", showAlbum=true, showTrackNum=false, title="Tracks"}) => {
  const search = useLocation().search;

  if (tracks === undefined || tracks.length === 0) {
    return <></>;
  }

  const searchType = new URLSearchParams(search).get('type');
  const seeAllLinkComponent = seeAllLink === "" ? <></> : <Link to={seeAllLink} className="link">See All</Link>

  return (
    <section className={className}>
      <div className="d-flex justify-content-between">
        <h4>{title}</h4>
        <div className="mt-1">{seeAllLinkComponent}</div>
      </div>
      <TrackList items={tracks} userState={userState} isPlaying={isPlaying} showAlbum={showAlbum} showTrackNum={showTrackNum} eventHandler={eventHandler} />
    </section>
  )
}

export default TrackSection
