import { Route, Switch, useParams } from "react-router-dom";
import { SearchBar } from './common/Commons';

import SearchAllPage from './SearchAllPage'
import SearchTracksPage from './SearchTracksPage'
import SearchAlbumsPage from './SearchAlbumsPage'


function SearchMainPage({isPlaying, userState, eventHandler}) {
  const { query } = useParams()

  return (
    <>
      <SearchBar eventHandler={eventHandler} defaultQuery={query} className="mt-3 mb-4"/>

      <Switch>
        <Route path='/search/:query' exact>
          <SearchAllPage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </Route>
        <Route path='/search/:query/tracks' exact>
          <SearchTracksPage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </Route>
        <Route path='/search/:query/albums' exact>
          <SearchAlbumsPage userState={userState} isPlaying={isPlaying} eventHandler={eventHandler}/>
        </Route>
      </Switch>
    </>
  );
}

export default SearchMainPage;
