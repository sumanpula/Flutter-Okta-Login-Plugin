import Flutter
import UIKit

public class SwiftFlutterOktaSigninPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "com.dnb.okta/login", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterOktaSigninPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if ([@"signIn" isEqualToString:call.method]) {
        result(FlutterMethodNotImplemented);
      } else {
        result(FlutterMethodNotImplemented);
      }
  }
}
