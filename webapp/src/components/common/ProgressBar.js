const ProgressBar = ({onProgressChange, progress}) => {

  let progressPcnt = progress * 100.0;

  const onProgressClick = (e) => {
    const element = e.currentTarget;
    const rect = element.getBoundingClientRect();
    const progressBarWidth = rect.right - rect.left;
    progressPcnt = (e.clientX - rect.left) / progressBarWidth;
    progressPcnt = Math.min(1, progressPcnt)
    progressPcnt = Math.max(0, progressPcnt)

    if (onProgressChange) {
      onProgressChange(progressPcnt);
    }
  };

  return (
    <>
    <div className="progress-wrapper d-flex flex-wrap align-content-center position-relative" onClick={onProgressClick}>
      <div className="progress thin">
          <div className="progress-bar no-transition" style={{width: progressPcnt + "%"}} role="progressbar" aria-valuenow={progressPcnt} aria-valuemin="0" aria-valuemax="100"></div>
      </div>
      <button className="d-none progress-btn position-absolute top-50 translate-middle rounded-pill btn p-0" style={{left: progressPcnt + "%"}}></button>
    </div>
    </>
  )
}

export default ProgressBar
