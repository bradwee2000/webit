import AlbumList from './AlbumList'
import { Link } from 'react-router-dom'


const AlbumSection = ({albums, userState, className='', isPlaying, eventHandler, seeAllLink=""}) => {
  if (albums === undefined || albums.length === 0) {
    return <></>;
  }

  const seeAllLinkComponent = seeAllLink === "" ? <></> : <Link to={seeAllLink} className="link">See All</Link>

  return (
    <section className={className}>
      <div className="d-flex justify-content-between">
        <h4>Albums</h4>
        <div className="mt-1">{seeAllLinkComponent}</div>
      </div>
      <AlbumList albums={albums} userState={userState} eventHandler={eventHandler} isPlaying={isPlaying}/>
    </section>
  )
}

export default AlbumSection
