import AlbumItem from './AlbumItem'

const AlbumList = ({albums, playingAlbum, isPlaying, eventHandler}) => {

  const albumItemComponents = albums.map(album => <AlbumItem key={album.id} album={album} playingAlbum={playingAlbum} eventHandler={eventHandler} isPlaying={isPlaying}/>)

  return <div className="album-list d-grid gap-3">{albumItemComponents}</div>;
}

export default AlbumList
