const Duration = ({millis}) => {

  if (millis === undefined || isNaN(millis)) {
    return <></>
  }

  const durationMins = Math.floor(millis / 1000 / 60);
  const durationSec = Math.floor((millis / 1000) % 60).toLocaleString('en-US', {minimumIntegerDigits: 2, useGrouping:false});

  return (
    <>
      <div className="duration">{durationMins}:{durationSec}</div>
    </>
  )
}

export default Duration
