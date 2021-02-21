import { useState } from 'react'

const ToggleButton = ({icon, label="", onClick}) => {

  const [active, setActive] = useState(false);

  const handleToggle = (e) => {
    const isActiveNow = !active;

    setActive(isActiveNow);
    if (onClick) {
      onClick(isActiveNow, e);
    }
  }

  const className = active ? " active" : ""
  const fullLabel = (active ? "Disable " : "Enable ") + label;

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
