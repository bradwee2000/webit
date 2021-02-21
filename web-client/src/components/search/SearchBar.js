import { useState } from 'react'

const SearchBar = ({className='', eventHandler}) => {

  const [search, setSearch] = useState('');

  function handleKeyDown(e) {
    if (e.key === 'Enter') {
      const query = e.target.value;
      eventHandler.onSearchSubmit(query);
    }
  }

  function handleChange(e) {
    const query = e.target.value;
    setSearch(query);
    eventHandler.onSearchChange(query);
  }

  function handleClear(e) {
    e.stopPropagation();
    setSearch('');
  }

  return (
      <div className={`search-bar ${className}`}>

          <div className="input-group">

            <span className="input-group-text search-icon">
              <svg height="24" role="img" width="24" viewBox="0 0 512 512" aria-hidden="true">
                <path d="M349.714 347.937l93.714 109.969-16.254 13.969-93.969-109.969q-48.508 36.825-109.207 36.825-36.826 0-70.476-14.349t-57.905-38.603-38.603-57.905-14.349-70.476 14.349-70.476 38.603-57.905 57.905-38.603 70.476-14.349 70.476 14.349 57.905 38.603 38.603 57.905 14.349 70.476q0 37.841-14.73 71.619t-40.889 58.921zM224 377.397q43.428 0 80.254-21.461t58.286-58.286 21.461-80.254-21.461-80.254-58.286-58.285-80.254-21.46-80.254 21.46-58.285 58.285-21.46 80.254 21.46 80.254 58.285 58.286 80.254 21.461z" fill="currentColor">
                </path>
              </svg>
            </span>

            <input className="form-control" type="text" name="search" onKeyDown={handleKeyDown} onChange={handleChange} value={search} placeholder="Search for Albums, Artists, or Songs" autoCorrect="off" autoCapitalize="off" spellCheck="false"/>

            <button className="input-group-text clear-btn" aria-label="Clear search field" onClick={handleClear}>
              <svg height="24" role="img" width="24" viewBox="0 0 24 24">
                <path d="M4.93,4.93,19.07,19.07m-14.14,0L19.07,4.93" fill="none" stroke="currentColor" strokeMiterlimit="10"></path>
              </svg>
            </button>
          </div>

      </div>
  )
}

export default SearchBar
