import AlbumItem from './AlbumItem'

const AlbumList = ({albums, userState, isPlaying, eventHandler}) => {

  const albumItemComponents = albums.map(album => <AlbumItem key={album.id} album={album} userState={userState} eventHandler={eventHandler} isPlaying={isPlaying}/>)

  return <div className="row">{albumItemComponents}</div>;
}

export default AlbumList
