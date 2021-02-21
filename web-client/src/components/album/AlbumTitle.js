const AlbumTitle = ({title, eventHandler}) => {

  return (
    <div className="title-container">
      <div className="title" title={title} >
        {title}
      </div>
    </div>
  )
}

export default AlbumTitle
