const DeviceButton = ({userState, onClick}) => {
  return (
    <button className="btn rounded-pill" aria-label="Device" data-testid="control-button-skip-forward" title="Device" onClick={onClick}>

      <svg aria-hidden="true" height="16" width="16" focusable="false" data-prefix="fad" data-icon="computer-speaker" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 512">
          <path fill="currentColor" d="M480 192a48 48 0 1 0-48-48 48 48 0 0 0 48 48zm0-64a16 16 0 1 1-16 16 16 16 0 0 1 16-16zm0 96a96 96 0 1 0 96 96 96.1 96.1 0 0 0-96-96zm0 160a64 64 0 1 1 64-64 64.08 64.08 0 0 1-64 64zm0-88a24 24 0 1 0 24 24 24 24 0 0 0-24-24zM592 32H368a48 48 0 0 0-48 48v352a48 48 0 0 0 48 48h224a48 48 0 0 0 48-48V80a48 48 0 0 0-48-48zm16 400a16 16 0 0 1-16 16H368a16 16 0 0 1-16-16V80a16 16 0 0 1 16-16h224a16 16 0 0 1 16 16zm-504 16a8 8 0 0 0-8 8v16a8 8 0 0 0 8 8h200.41a79.15 79.15 0 0 1-14.79-32zM0 64v288a32 32 0 0 0 32 32h256v-32H32V64h257.62a79.15 79.15 0 0 1 14.79-32H32A32 32 0 0 0 0 64z" />
      </svg>
    </button>
  )
}

export default DeviceButton