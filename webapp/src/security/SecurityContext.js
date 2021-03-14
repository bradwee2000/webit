class SecurityContext {
  static authToken;
  static playTrackCode;

  static isLoggedIn() {
    return SecurityContext.authToken !== undefined
      && SecurityContext.authToken !== '';
  }

  static setToken(authToken) {
    SecurityContext.authToken = authToken;
  }

  static getToken() {
    return SecurityContext.authToken;
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
