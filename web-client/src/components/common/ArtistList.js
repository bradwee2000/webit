const ArtistList = ({artists, maxDisplay=3, eventHandler}) => {
  if (!Array.isArray(artists)) {
    artists = [artists];
  }

  maxDisplay = Math.min(maxDisplay, artists.length);

  const handleOnClick = (e, artist) => {
    e.stopPropagation();
    eventHandler.onArtistClick(artist);
  };

  const components = [];

  for (let i=0; i<maxDisplay; i++) {
      const artist = artists[i];
      const component = (
        <div key={artist} className="artist d-inline">
          <span className="link" onClick={(e) => handleOnClick(e, artist)}>{artist}</span>
          {i+1 < maxDisplay && <span>, </span>}
        </div>
      )
      components.push(component);
  }

  return (
    <div className="">
      {components}
    </div>
  )
}

export default ArtistList
