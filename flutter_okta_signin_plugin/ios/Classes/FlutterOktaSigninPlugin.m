#import "FlutterOktaSigninPlugin.h"
#if __has_include(<flutter_okta_signin_plugin/flutter_okta_signin_plugin-Swift.h>)
#import <flutter_okta_signin_plugin/flutter_okta_signin_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_okta_signin_plugin-Swift.h"
#endif

@implementation FlutterOktaSigninPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterOktaSigninPlugin registerWithRegistrar:registrar];
}
@end
