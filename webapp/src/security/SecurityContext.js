class SecurityContext {
  static authToken;
  static playTrackCode;

  static isLoggedIn() {
    const token = SecurityContext.getToken()
    return token !== null && token !== ''
  }

  static setToken(authToken) {
    localStorage.setItem("au", authToken)
  }

  static getToken() {
    return localStorage.getItem("au")
  }

  static setName(name) {
    localStorage.setItem("name", name)
  }

  static getName(name) {
    return localStorage.getItem("name")
  }

  static setPlayTrackCode(playTrackCode) {
    SecurityContext.playTrackCode = playTrackCode;
  }

  static getPlayTrackCode() {
    return SecurityContext.playTrackCode;
  }

  static getPlayTokenHash(trackId) {
    const hash = SecurityContext.hash(SecurityContext.playTrackCode + ":" + trackId);
    return hash;
  }

  static logout() {
    localStorage.clear()
  }

  static hash(value) {
    var hash = 0, i;
    if (value.length === 0) return hash;
    for (i = 0; i < value.length; i++) {
      const chr = value.charCodeAt(i);
      hash = ((hash << 5) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  }
}

export default SecurityContext
