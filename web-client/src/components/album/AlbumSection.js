import AlbumList from './AlbumList'

const AlbumSection = ({albums, playingAlbum, className='', isPlaying, eventHandler}) => {

  if (albums === undefined || albums.length === 0) {
    return <></>;
  }

  return (
    <section className={className}>
      <h4>Albums</h4>
      <AlbumList albums={albums} playingAlbum={playingAlbum} eventHandler={eventHandler} isPlaying={isPlaying}/>
    </section>
  )
}

export default AlbumSection
