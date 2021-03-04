import AlbumItem from './AlbumItem'

const AlbumList = ({albums, userState, isPlaying, eventHandler}) => {

  const albumItemComponents = albums.map(album => <AlbumItem key={album.id} album={album} userState={userState} eventHandler={eventHandler} isPlaying={isPlaying}/>)

  return <div className="album-list d-grid gap-3">{albumItemComponents}</div>;
}

export default AlbumList
