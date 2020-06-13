import 'package:flutter/material.dart';
import 'package:flutter_cantpickup/add_user_details.dart';

void main() => runApp(MyApp());

//val message =ctx.applicationContext.getSharedPreferences("Message",
//    Context.MODE_PRIVATE
//)
//val name =ctx.applicationContext.getSharedPreferences("Name",
//    Context.MODE_PRIVATE
//)
//val isActive = ctx.applicationContext.getSharedPreferences("isActive",
//    Context.MODE_PRIVATE
//)
class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: AddUserDetails(),
    );
  }
}

