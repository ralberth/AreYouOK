# Persisting State

Problem: start app, start timers, force-quit app.  Re-running the app doesn't know there are alarms
running or that the "Enabled" toggle was turned on.  It doesn't know how long until the alarms
will trigger, so it can't draw the progress bar correctly.

This app is a bit of a special snowflake: you can't *really* quit it if the Alarms are running.
No matter what you do, starting the app or switching to it *has* to draw the screen correctly
from some sort of persisted state.

Ways the app can quit and/or lose state:

1. **Configuration** change (like rotating phone)
2. **Background** the app (take a phone call, hit circle or square to select another app)
3. **System**-initiated termination (app is in the background, phone running out of memory)
4. **User**-initiated termination (swipe-up the app on the list of all running apps screen)
5. Phone **reboot**

Configuration changes destroy the View/UI, but the Activity and ViewModel stay as-is.  Nothing
to do, app just redraws normally for free.

Background and foreground the app: a ViewModel handles this for free when the Activity is killed
and created fresh.  Nothing to do, just happens automatically.

System-initiated termination destroys the Activity and the ViewModel object.  Use the
[SavedStateHandle](https://developer.android.com/reference/androidx/lifecycle/SavedStateHandle)
APIs to save and rehydrate your ViewModel when the system terminates an Activity and subsequently
creates a fresh Activity.

User-initiated termination has no API or way to persist state, since this usually means the user
wants to completely reset the app and ***not*** return to where it left off.  This app is a
special snowflake and won't behave like this.  Accidentally killing it will keep the Alarms running
and the UI will return to where you left off next time you open it.

If the phone reboots, never mind, start over from scratch.


## Configuration changes

Nothing to do, ViewModel persists automatically.


## Saved ViewModel State

Docs for saving state:
* https://developer.android.com/topic/libraries/architecture/saving-states
* https://developer.android.com/develop/ui/compose/state-saving
* https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate

We could use these so the app will efficiently save it's ViewModel when the system terminates
the app, but we still have to implement something different in the next section below to handle
the case when the user terminates the app.  There's no senes *right now* in building a different
and efficient way to handle only the edge case of the system terminating the app.

So, we're doing nothing with any type of state-saving API around Composables and ViewModels.


## Handling User-Termination

There are no hooks, callbacks, or APIs that fire or are guaranteed to execute or fire when the
user force-terminates the app.  So, we need to use a different design:

1. Anytime the UI or the Alarms do anything, persist the state of the whole system
2. Anytime an Activity is created, rehydrate the ViewModel from this persistent state

https://developer.android.com/training/data-storage has details on types of persistent storage.

* https://developer.android.com/training/data-storage/shared-preferences
* https://developer.android.com/develop/ui/views/components/settings/use-saved-values

the SharedPreferences object that is being used, call PreferenceManager.getDefaultSharedPreferences().


It might seem simple to just use the Coordinator as the controller for this, since a new
Controller is created when the app is created, but there's an edge case: if the system is creating
a new Activity, but it saved the ViewModel automatically, we have a valid ViewModel, but a new
Controller that thinks it has to load state from disk.  So, the trigger to re-load state from
disk has to come from the ViewModel when it detects that it's created fresh.


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
