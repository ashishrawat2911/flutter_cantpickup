package com.example.flutter_cantpickup

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony


class CallReceiver : PhonecallReceiver() {
    override fun onIncomingCallReceived(ctx: Context, number: String?, start: java.util.Calendar) {

        val message =ctx.applicationContext.getSharedPreferences("Message",
            Context.MODE_PRIVATE
        )
        val name =ctx.applicationContext.getSharedPreferences("Name",
            Context.MODE_PRIVATE
        )
        val isActive = ctx.applicationContext.getSharedPreferences("isActive",
            Context.MODE_PRIVATE
        )
        Log.d("CallReceiver", "my Message")
        if (isActive.getBoolean("isActive", false)) {
            val telephonyManager =
                ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val clazz = Class.forName(telephonyManager.javaClass.name)
            val method = clazz.getDeclaredMethod("getITelephony")
            method.isAccessible = true
            val telephonyService: ITelephony = method.invoke(telephonyManager) as ITelephony
            telephonyService.endCall()
            val intent = Intent(ctx.getApplicationContext(), MainActivity::class.java)
            val pi = PendingIntent.getActivity(ctx.getApplicationContext(), 0, intent, 0)
            val sms = SmsManager.getDefault()
            sms.sendTextMessage(
                number,
                null,
                "Hi \n${name.getString("Name","User")} is ${message.getString(
                    "ALERT_STATUS_REASON",
                    "Busy"
                )}\n Please Try again",
                pi,
                null
            )
        }
    }

    override fun onIncomingCallAnswered(ctx: Context, number: String?, start: java.util.Calendar) {
        Log.d("CallReceiver", "my Message")
        print("asdasd")
    }

    override fun onIncomingCallEnded(
        ctx: Context,
        number: String?,
        start: java.util.Calendar?,
        end: java.util.Calendar
    ) {
        Log.d("CallReceiver", "my Message")
        print("asdasd")
    }

    override fun onOutgoingCallStarted(ctx: Context, number: String?, start: java.util.Calendar) {
        Log.d("CallReceiver", "my Message")
        print("asdasd")
    }

    override fun onOutgoingCallEnded(
        ctx: Context,
        number: String?,
        start: java.util.Calendar,
        end: java.util.Calendar
    ) {
        Log.d("CallReceiver", "my Message")
        print("asdasd")
    }

    override fun onMissedCall(ctx: Context, number: String?, start: java.util.Calendar?) {
        Log.d("CallReceiver", "my Message")
        print("asdasd")
    }


}