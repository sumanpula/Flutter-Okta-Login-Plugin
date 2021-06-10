/// * Add your okta configuration details here
class OktaConfig {
  static var configuration = {
    "client_id": "0oawfbl5zwMGMvM6V5d6",
    "redirect_uri": "com.dnb.okta.flutter.signin:/callback",
    "end_session_redirect_uri": "com.dnb.okta.flutter.signin:/logout",
    "scopes": ["openid", "profile", "offline_access"],
    "discovery_uri": "https://dev-52334454.okta.com"
  };
}
