# Test Plan

The following tests to be performed on a deployment of the application onto my physical phone.
Don't use an emulator, since it likely isn't using the same OS release as my Galaxy 9S.  If you
prefer, load a specific version and phone, like the Pixel 4 at API level 29.

To avoid annoying other people, consider setting the delivery phone number to your phone so all
text messages stay on your device.  You can also set how long a minute is in `Constants.kt` to
speed up a test run.


## User Interface

Confirm spacing, colors, text all appear reasonable.

Countdown Delay is disabled when running, enabled when stopped.

Confirm icon appears (not the default Android icon) on the home screen.

Toggle "Enable", confirm progress bar:
* Starts full, "drains" over time.
* Turns yellow at T-3 minutes and T-2 minutes
* Turns red at T-1 minute
* Stays empty at T-0 minutes and for all minutes after this

Turn "Enable" off, confirm progress bar text and progress bar disappear.

Confirm Check-in button is disabled when "Enable" is off, and enabled with "Enable" toggle is on.

Enable and press the soft "O" button to display the home screen.  Tap the application icon and
confirm it displays the Enable toggle on and the progress bar correctly displaying the time
remaining.  Check-in button is enabled.

Enable and press the soft square button and (force) quit the app.  Tap the app's icon on the home
page to re-launch it.  Confirm enable toggle enabled and the progress bar showing the correct time
remaining.  Check-in button is enabled.

Enable, confirm progress bar is counting down (not full), click Check-In button and confirm the
Progress Bar resets to "full".


### Colors

Run app in light mode and dark mode (set on phone, not in app), verify colors and display look
reasonable.  Click buttons, look at backgrounds, enabled/disabled colors, and Notifications.


### Permissions

Start fresh without SMS permissions, confirm pop-up asks for permission.  If granted, continues.
If not, app quits gracefully.

Turn off Notifications and Alarms, restart the app, make sure both red banners say the perms are
needed.

Confirm if either red banner for permissions at top is present that the "Enable" toggle can't be
moved.  OK for the Countdown Delay slider to move.


## Alarms

*Set Countdown Delay to 5 minutes for the tests in this section.*

Confirm alarms sound at T-3 minutes, -2, -1, and 0 minutes.  Try with several different Countdown
Delay slider settings.

Enable and immediately disable.  Wait 6 minutes and confirm no alarms triggered any actions.

Enable and wait for first "T-3 minutes" notification to arrive.  Disable and wait another 5 minutes.
Confirm no other alarms trigger any actions.

Enable and choose another application so RUOK? isn't visible on the screen (but still running).
Wait 5 minutes and confirm all alarms trigger actions.

Enable and quit the application.  Bring up the running app screen and quit RUOK? so it isn't
running at all.  Wait 5 minutes and confirm all alarms trigger notifications and send TXT msgs.

Enable and press the power side button to lock the device and turn off the display.  Do not interact
with the phone.  Wait 5 minutes to confirm all alarms trigger *sounds*.  Wake-up the phone, login,
and confirm at lest the last Notification message is active and all TXT messages were sent.


## Notifications

Enable and wait for first notification at T-3 minutes.  Confirm single notification message not
displayed as a pop-up over-top of the running application.  Pull-down to view the notification &
confirm text and green background icon.  Continue for T-2 minute notification too.

Repeat above for T-1 notification, which should pop-up overtop of app display and use red background
icon.

Wait for times-up notification, pop-up overtop of app, red background icon.

^--- each notification should appear after previous notification is removed.  Only one notification
should be present at a time.

Enable app and wait for first notification to appear.  Toggle to disable the application.  The
notification should disappear.

Re-enable and wait for first notification to appear.  Click Check-in button.  Confirm notification
disappears.

Enable and wait for T-1 notification to appear.  Click Check-in button.  Confirm notification
disappears (T-3 adn T-1 notifications are in different Channels).

Enable and wait for final notification that plays continuously.  Disable and confirm notification
disappears.

Enable and lock the screen.  Wait for duration of countdown and confirm each notification appears on
the lock screen (and sound plays).


### Notification Actions

Enable and wait for a notification.  Try to swipe the notification right and left.  Confirm it
disappears and nothing bad happens.

Enable and wait for a notification with the app visible normally.  Tap the notification to bring up
the app and confirm it shows enabled with the right Countdown Delay, the progress bar set correctly,
and the Check-in button enabled.

Enable the app and immediately press the "O" soft-button to bring up the phone's home screen (RUOK?
backgrounded).  Wait for a notification and tap it.  Confirm the app is brought to the foreground
with all UI components displaying correctly.

Enable the app and immediately press the square soft button.  Force quit RUOK? and wait for the
first notification.  Tap the notification and confirm the app is re-launched and all UI components
display correctly.


## Sounds

Enable and wait for first notification at T-3 minutes.  Confirm small beep sound (without
notification pop-up being visible).  Continue for T-2 minute notification too.  Confirm beep for
both.

Repeat above for T-1 notification.  Confirm 1-minute sound.

Wait for times-up notification and loud times-up sound.  Wait for more than 30 seconds and confirm
sound continues to play.

Enable and wait for T-1 notification to appear.  As soon as the sound starts to play, disable and
confirm the sound is stopped immediately before it reaches the end of the sound track.

Enable and wait for final notification that plays continuously.  Disable and confirm sound stops
playing immediately.

Enable and lock the screen.  Wait for duration of countdown and confirm the sound plays for each
notification.

Enable and wait for final notification that plays continuously.  Click Check-in button and confirm
sound stops playing immediately.


## Messaging

Enable and confirm text message arrives explaining that the countdown has started and includes how
long the whole countdown runs.

Click Check-in and confirm text message arrives saying check-in was clicked.

Click toggle to disable.  Confirm text message arrives saying countdown was disabled.
