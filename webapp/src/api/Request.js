import { SecurityContext } from './Apis'

const options = (method, body) => {
  const headers = {
    "Authorization": SecurityContext.getToken()
  }

  if (body === undefined) {
    return {
        method: method,
        headers: headers
    };
  }

  return {
      method: method,
      headers: headers,
      body: JSON.stringify(body)
  };
}

const doFetch = (url, requestOptions) => {
  return fetch(url, requestOptions)
    .then(res => res.json())
    .then(res => {
      if (res.error) {
        throw {error: res.error}
      }
      return res
    })
}

const Requests = {

  get: function(url) {
    return doFetch(url, options("GET"))
  },

  post: function(url, body) {
    return doFetch(url, options("POST", body))
  }
}

export default Requests
