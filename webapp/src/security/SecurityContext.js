class SecurityContext {
  static token;

  static setToken(token) {
    SecurityContext.token = token;
  }

  static getToken() {
    return SecurityContext.token;
  }
}

export default SecurityContext
