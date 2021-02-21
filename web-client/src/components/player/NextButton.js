const NextButton = ({onClick}) => {

  return (
    <button className="btn rounded-pill" aria-label="Next" data-testid="control-button-skip-forward" title="Next" onClick={onClick}>
      <svg role="img" height="16" width="16" viewBox="0 0 16 16">
        <path d="M11 3v4.119L3 2.5v11l8-4.619V13h2V3z"></path>
      </svg>
    </button>
  )
}

export default NextButton
