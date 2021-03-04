import AlbumList from './AlbumList'

const AlbumSection = ({albums, userState, className='', isPlaying, eventHandler}) => {

  if (albums === undefined || albums.length === 0) {
    return <></>;
  }

  return (
    <section className={className}>
      <h4>Albums</h4>
      <AlbumList albums={albums} userState={userState} eventHandler={eventHandler} isPlaying={isPlaying}/>
    </section>
  )
}

export default AlbumSection
