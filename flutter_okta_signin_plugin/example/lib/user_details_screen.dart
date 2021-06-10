import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_okta_signin_plugin/model/response.dart';
import 'package:flutter_okta_signin_plugin/model/user_profile.dart';
import 'package:flutter_okta_signin_plugin/okta_manager.dart';

import 'login_screen.dart';

class UserDetailsScreen extends StatefulWidget {
  final Response response;
  UserDetailsScreen(this.response);

  _UserDetailsScreenState createState() => _UserDetailsScreenState();
}

class _UserDetailsScreenState extends State<UserDetailsScreen> {
  static final String _TAG = "UserDetailsScreen:: ";
  final OktaManager oktaManager = OktaManager();
  bool showProfile = false;
  UserProfile userProfile;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("User Details"),
        actions: [
          IconButton(icon: Icon(Icons.account_circle_rounded), onPressed: () {
            _getUserProfile();
          }),
          IconButton(icon: Icon(Icons.logout), onPressed: () {
            _signOut();
          }),
        ],
      ),
      body: ListView(
        padding: EdgeInsets.only(top: 12, bottom: 12, left: 4, right: 4),
        children: [
          _getProfileView(userProfile),
          Card(
            color: Colors.amberAccent,
            elevation: 12,
            child: ListTile(
              title: Text("Oka Sign In Info"),
            ),
          ),
          Card(
            elevation: 12,
            child: ListTile(
              subtitle: Text("Access Token"),
              title: Text("${widget.response.access_token}"),
            ),
          ),
          Card(
            elevation: 12,
            child: ListTile(
              subtitle: Text("Refresh Token"),
              title: Text("${widget.response.refresh_token}"),
            ),
          ),
          Card(
            elevation: 12,
            child: ListTile(
              subtitle: Text("Id Token"),
              title: Text("${widget.response.id_token}"),
            ),
          ),
          Card(
            elevation: 12,
            child: ListTile(
              subtitle: Text("Expires in"),
              title: Text("${widget.response.expires_in}"),
            ),
          ),
      Padding(
        padding: const EdgeInsets.only(top: 12, bottom: 12, left: 32, right: 32),
        child: ElevatedButton(
          onPressed: () {
            _signOut();
          },
          child: Text("Sign Out"),),
      ),
        ],
      ),
    );
  }

  _signOut() {
    return _logout().then((value) => {
      // print("$_TAG response $value"),
      Navigator.push(context, MaterialPageRoute(
          builder: (BuildContext c) {
            return LoginScreen();
          })
      )
    });
  }

  _getProfileView(UserProfile userProfile) {
    return userProfile != null ? Column(
      children: [
        Card(
          elevation: 12,
          child: ListTile(
            tileColor: Colors.amberAccent,
            title: Text("User Profile"),
          ),
        ),
        Card(
          elevation: 12,
          child: ListTile(
            subtitle: Text("name"),
            title: Text("${userProfile.name}"),
          ),
        ),
        Card(
          elevation: 12,
          child: ListTile(
            subtitle: Text("Username"),
            title: Text("${userProfile.preferredUsername}"),
          ),
        ),
        Card(
          elevation: 12,
          child: ListTile(
            subtitle: Text("Last Updated"),
            title: Text("${DateTime.fromMillisecondsSinceEpoch(userProfile.updatedAt * 1000)}"),
          ),
        ),
      ],
    ) : Container();
  }

  Future _logout() async {
    final result = await oktaManager.signOut();
    // print("$_TAG _logout response $result");
    return result;
  }

  Future _getUserProfile() async {
    final result = await oktaManager.getUserProfile();
    // print(" $_TAG getUserProfile response $result");
    userProfile = UserProfile.fromJson(json.decode(result));
    setState(() {
      showProfile = !showProfile;
    });
    return result;
  }
}
