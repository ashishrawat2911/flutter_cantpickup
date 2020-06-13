package com.example.flutter_cantpickup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import java.util.*


abstract class PhonecallReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
            print(savedNumber)
        } else {
            val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var state = 0
            when (stateStr) {
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    state = TelephonyManager.CALL_STATE_IDLE
                    print(TelephonyManager.CALL_STATE_IDLE)
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    state = TelephonyManager.CALL_STATE_OFFHOOK
                    print(TelephonyManager.CALL_STATE_OFFHOOK)
                }
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    state = TelephonyManager.CALL_STATE_RINGING
                    print(TelephonyManager.CALL_STATE_RINGING)
                }
            }


            onCallStateChanged(context, state, number)
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected abstract fun onIncomingCallReceived(ctx: Context, number: String?, start: Calendar)

    protected abstract fun onIncomingCallAnswered(ctx: Context, number: String?, start: Calendar)
    protected abstract fun onIncomingCallEnded(
        ctx: Context,
        number: String?,
        start: Calendar?,
        end: Calendar
    )

    protected abstract fun onOutgoingCallStarted(ctx: Context, number: String?, start: Calendar)
    protected abstract fun onOutgoingCallEnded(
        ctx: Context,
        number: String?,
        start: Calendar,
        end: Calendar
    )

    protected abstract fun onMissedCall(ctx: Context, number: String?, start: Calendar?)

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    private fun onCallStateChanged(context: Context, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Calendar.getInstance()
                savedNumber = number

                onIncomingCallReceived(context, number, callStartTime)
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = java.util.Calendar.getInstance()
                    onOutgoingCallStarted(context, savedNumber, callStartTime)
                } else {
                    isIncoming = true
                    callStartTime = java.util.Calendar.getInstance()
                    onIncomingCallAnswered(context, savedNumber, callStartTime)
                }
            TelephonyManager.CALL_STATE_IDLE ->
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime)
                } else if (isIncoming) {
                    onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        java.util.Calendar.getInstance()
                    )
                } else {
                    onOutgoingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        java.util.Calendar.getInstance()
                    )
                }
        }
        lastState = state
    }

    companion object {

        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations

        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Calendar = Calendar.getInstance()
        private var isIncoming: Boolean = false
        private var savedNumber: String? =
            null  //because the passed incoming is only valid in ringing
    }
}