import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_okta_signin_plugin/model/response.dart';
import 'package:flutter_okta_signin_plugin/okta_manager.dart';

import 'user_details_screen.dart';

class LoginScreen extends StatelessWidget {
  static final String _TAG = "LoginScreen:: ";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text("Okta Login"),
      ),
      body: Container(
        alignment: Alignment.center,
        child:ElevatedButton(onPressed: () {
          return _login().then((value) => {
          print("$_TAG login then $value"),
            Navigator.push(context, MaterialPageRoute(
                builder: (BuildContext c) {
                  return UserDetailsScreen(Response.fromJson(json.decode(value)));
                })
            )
          }).onError((error, stackTrace) => {
            print("$_TAG login error"),
          });
        }, child: Text("Sign In"),
        ),
      ),
    );
  }

  Future _login() async {
    final result = await OktaManager().signIn();
    // print("$_TAG login response $result");
    return result;
  }
}
