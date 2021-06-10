class UserProfile {
  String sub;
  String name;
  String locale;
  String preferredUsername;
  String givenName;
  String familyName;
  String zoneinfo;
  int updatedAt;

  UserProfile(
      {this.sub,
        this.name,
        this.locale,
        this.preferredUsername,
        this.givenName,
        this.familyName,
        this.zoneinfo,
        this.updatedAt});

  UserProfile.fromJson(Map<String, dynamic> json) {
    sub = json['sub'];
    name = json['name'];
    locale = json['locale'];
    preferredUsername = json['preferred_username'];
    givenName = json['given_name'];
    familyName = json['family_name'];
    zoneinfo = json['zoneinfo'];
    updatedAt = json['updated_at'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['sub'] = this.sub;
    data['name'] = this.name;
    data['locale'] = this.locale;
    data['preferred_username'] = this.preferredUsername;
    data['given_name'] = this.givenName;
    data['family_name'] = this.familyName;
    data['zoneinfo'] = this.zoneinfo;
    data['updated_at'] = this.updatedAt;
    return data;
  }
}
