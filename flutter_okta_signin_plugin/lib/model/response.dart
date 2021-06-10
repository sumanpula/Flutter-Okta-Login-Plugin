class Response {
  String access_token;
  String refresh_token;
  String id_token;
  String expires_in;

  Response.fromJson(Map<String, dynamic> json) {
    access_token = json['access_token'];
    refresh_token = json['refresh_token'];
    id_token = json['id_token'];
    expires_in = json['expires_in'];
  }
}