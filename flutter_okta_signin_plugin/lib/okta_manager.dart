import 'dart:convert';

import 'package:flutter/services.dart';

import 'constants.dart';

class OktaManager  {
  final MethodChannel _methodChannel = const MethodChannel(Constants.CHANNEL);
  /// * This method is to config okta data required to sign in
  dynamic config(final Map<String, dynamic> configuration) async {
    return await _methodChannel.invokeMethod(Constants.config, json.encode(configuration));
  }

  /// * This method is to open login page in web to sign into okta
  dynamic signIn() async {
    try {
      return await _methodChannel.invokeMethod(Constants.signIn);
    } on MissingPluginException {
      throw Exception("User signIn method not implemented");
    }
  }

  /// * This method is to open login page in web to sign out from okta
  dynamic signOut() async {
    try {
      return await _methodChannel.invokeMethod(Constants.signOut);
    } on MissingPluginException {
      throw Exception("User signOut method not implemented");
    }
  }

  /// * This method is to check whether user already logged in
  dynamic isSignedIn() async {
    try {
      return await _methodChannel.invokeMethod(Constants.isSignedIn);
    } on MissingPluginException {
      throw Exception("isSignedIn method not implemented");
    }
  }

  /// * This method is to check whether user already logged in
  dynamic getUserProfile() async {
    try {
      return await _methodChannel.invokeMethod(Constants.getUserProfile);
    } on MissingPluginException {
        throw Exception("User profile method not implemented");
    }
  }
}