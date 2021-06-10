import 'package:flutter/material.dart';
import 'package:flutter_okta_signin_plugin/data.dart';
import 'package:flutter_okta_signin_plugin/okta_manager.dart';
import 'package:flutter_okta_signin_plugin_example/config.dart';

import 'login_screen.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    // set configuration
    OktaManager().config(OktaConfig.configuration);
    return MaterialApp(
      title: 'Okta Login',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Okta Login'),
    );
  }
}


class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);
  static bool isSignedIn = false;
  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}
class _MyHomePageState extends State<MyHomePage> {

  static String TAG = "_MyHomePageState:: ";


  ValueNotifier<Data> data = ValueNotifier(Data(false, null));

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return LoginScreen();
  }

  Future<bool> _isSignedIn() async {
    final result = await OktaManager().isSignedIn();
    // print("$TAG _isSignedIn response $result");
    return result;
  }

  @override
  void initState() {
    super.initState();
    _isSignedIn().then((value) => {
      data.value.isSignedIn = value,
    });
  }
}
