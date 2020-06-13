import 'package:shared_preferences/shared_preferences.dart';

class CantPickUpPreferences {
  static const String IS_ACTIVE = "isActive";
  static const String MESSAGE = "Message";
  static const String NAME = "Name";

  addActiveStatus(bool value) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool(IS_ACTIVE, value);
  }

  Future<bool> getActiveStatus() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return String
    return prefs.getBool(IS_ACTIVE) ?? false;
  }

  addMessage(String value) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString(MESSAGE, value);
  }

  addName(bool value) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool(NAME, value);
  }

 Future<String> getName() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    //Return String
    return prefs.getString(NAME) ;
  }
}
