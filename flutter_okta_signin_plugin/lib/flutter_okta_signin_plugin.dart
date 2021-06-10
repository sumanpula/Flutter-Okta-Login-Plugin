
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterOktaSigninPlugin {
  static const MethodChannel _channel =
      const MethodChannel('com.dnb.okta/login');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
