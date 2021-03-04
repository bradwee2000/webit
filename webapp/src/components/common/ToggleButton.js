import { useState } from 'react'

const ToggleButton = ({icon, label="", isEnabled, onClick}) => {

  const handleToggle = (e) => {
    const isActiveNow = !isEnabled;
    if (onClick) {
      onClick(isActiveNow, e);
    }
  }

  const className = isEnabled ? " active" : ""
  const fullLabel = (isEnabled ? "Disable " : "Enable ") + label;

  return (
    <div className={"toggle-btn" + className}>
      <button className="btn rounded-pill" role="checkbox" aria-checked="false" aria-label={fullLabel} title={fullLabel} onClick={handleToggle}>
        {icon}
      </button>
      <div className="indicator"></div>
    </div>
  )
}

export default ToggleButton
