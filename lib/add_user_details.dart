import 'package:flutter/material.dart';
import 'package:flutter_cantpickup/preferences.dart';

import 'customSwitch.dart';

class AddUserDetails extends StatefulWidget {
  @override
  _AddUserDetailsState createState() => _AddUserDetailsState();
}

class _AddUserDetailsState extends State<AddUserDetails> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Add User Details"),
      ),
      body: Container(
        width: double.infinity,
        height: double.infinity,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            FutureBuilder<bool>(
                future: CantPickUpPreferences().getActiveStatus(),
                builder: (context, snapshot) {
                  print(snapshot.data);
                  return CustomSwitch(
                    value: snapshot.data ?? false,
                    onChanged: (value) {
                      setState(() {
                        print(value);
                        CantPickUpPreferences().addActiveStatus(value);
                      });
                    },
                    activeColor: Colors.green,
                    activeTextColor: Colors.white,
                  );
                })
          ],
        ),
      ),
    );
  }
}
