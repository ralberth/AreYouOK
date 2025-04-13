# Background Processing

The normal UI "foreground" app (Activity) can play sounds, interact with the user, and send
text messages just fine.  It also works when the app is running, but the user is interacting
with another app.  However, when the phone is locked or in low-power mode (or asleep), the
app doesn't run.  This means if my phone locks automatically, the app won't play alerts and
won't send text messages. BAD.

Need a way for the app to wake-up the phone (if necessary) and at least play sounds when the
phone is locked.  Ideally, it would show alerts on the lock screen too.



### Android Services

From https://developer.android.com/develop/background-work/services

Features of Android Services:

* Can perform long-running operations in the background.
* Does not provide a user interface.
* Foreground service must display a Notification.
* Foreground services continue running even when the user isn't interacting with the app.
* !!! Not sure this works when the phone is locked.  Would need to test this or find details.

Technical details of Services:

* Runs in main application's main thread: use separate thread for blocking tasks.
* You should run any blocking operations on a separate thread within the service to avoid Application Not Responding (ANR) errors.
* Consider WorkManager API instead: more flexible way to schedule things.
* Bind: allow a UI to RPC a Service
* START a service: runs indefinitely; BIND to a service, stops when app quits.

Design ideas:

* Start it explicitly when UI clicks enable (starts the count-down)
* Destroy it when UI clicks to disable.
* Notification says it's active (no buttons, just simple notification)
* Service handles playing sounds and sending the "missed checkin" text message.
* UI handles enable/disable and check-in text messages
* When UI starts, if Service is running, start the UI in enabled mode and get time-left.
* => if user quits the app while it's running, the Notification and Service remain counting down.



## Scheduled Alarms

From https://developer.android.com/develop/background-work/services/alarms/schedule

The Service idea is really for something that is executing.  We really don't want anything to
happen until T-3 minutes down to T-0 / expired time.  The UI can show a countdown progress bar,
but that's just UI decoration and doesn't really drive the logic of the application.  If the app
isn't visible to the user, there's really no reason why a countdown UI widget should be in charge
of any type of alerting or alarming.  We're a closer match to the android alarm app: don't do
or show anything until 7:00 am, then wake-up the phone, unlock if locked, and take over the
screen with a UI alarm (and play a sound).

### Key features:

* Alarms fire even if the phone is asleep!
* They are disconnected from your app.
* They can fire off PendingIntents to start other things.
* When alarm fires, it starts the target application if it is not already running.


### Ideas:

1. Turn on: add a bunch of alarms for playing sounds and texting people.
2. Turn off: cancel all scheduled alarms (read from persistent memory).
3. Need a way to persist the Intents returned from scheduling alarms.
4. Have to cancel alarms if you quit the app, re-run it, and click to stop or check-in.
5. When an Alarm alarms, it just invokes your PendingIntent, so you are free to do anything you want.

https://developer.android.com/reference/android/content/Intent

When an Intent fires, it can:

* Start an Activity in your app
* Broadcast to anything on the phone listening for its message
* Start or Bind Service to interact with background services

Developer guide to Intents: https://developer.android.com/guide/topics/intents/intents-filters

You can be explicit and directly name the Kotlin class that should receive the trigger.


## In-UI timers

From https://developer.android.com/reference/java/util/Timer and
https://developer.android.com/reference/android/os/Handler

Another idea, not probably workable:

For timing operations that are guaranteed to occur during the lifetime of your application,
consider using the Handler class in conjunction with Timer and Thread.  This approach gives
Android better control over system resources.

This way, everything lives in threads inside the single application.  As long as the app is
running, we're good.  Not sure this deals with a locked screen any better than what I have
now in rel-1.0...

Timer: single background thread, handles all scheduled alarms in the app's process.
