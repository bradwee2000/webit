import { ToggleButton } from './../common/Commons'

const QueueButton = ({userState, isEnabled, onClick}) => {

  const icon = (
    <svg role="img" height="16" width="16" viewBox="0 0 16 16">
      <path d="M0 0 L6 3 L0 6 Z"></path>
      <rect  height="1" width="8" y="3" x="8"/>
      <rect  height="1" width="16" y="9" x="0"/>
      <rect  height="1" width="16" y="15" x="0"/>
    </svg>
  )

  return (
    <ToggleButton icon={icon} isEnabled={isEnabled} onClick={onClick} label="Queue"/>
  )
}

export default QueueButton
