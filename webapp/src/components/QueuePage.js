import TrackSection from './track/TrackSection';

function QueuePage({isPlaying, userState, eventHandler}) {

  const localEventHandler = {
    onTrackClick(trackId) {
      eventHandler.onQueueTrackClick(trackId)
    },

    onTrackPlay(trackId) {
      eventHandler.onQueueTrackClick(trackId);
    },

    onTrackPause(trackId) {
      eventHandler.onQueueTrackClick(trackId);
    },
  }

  return (
    <>
      <TrackSection tracks={userState.tracks} userState={userState} isPlaying={isPlaying} eventHandler={localEventHandler} className="mb-5" title="Queue"/>
    </>
  );
}

export default QueuePage;
