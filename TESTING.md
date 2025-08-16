# Test Plan

You can test nearly everything using the emulator except the phone movement feature.  Yes, the
emulator has a feature to simulate movement, but this needs a real-world test with physical movement
so we can be sure the app is calibrated to a human when they are walking or running.

To avoid annoying other people, consider setting the delivery phone number to your phone so all
text messages stay on your device.

Try this to get a table of contents:

```
grep '^#' TESTING.md | sed 's/#/    /g'

```

Right now, the app targets API leve 29, also known as Android 10, or code name "Q".  You can test
this in an emulator with hardware "Pixel 4".  Testing on a newer phone (or in general using it on
any newer phone) likely will fail.  Newer versions of Android OS have additional security features
that this test and the app don't take into consideration.


# User Interface

## Android Launcher

Confirm icon appears (not the default Android icon) on the home screen.

Launching the app shows a blank screen and the "RUOK?" logo.

Start the countdown, wait for the first alert message, confirm icon on Android home screen shows
that there is an alert/message available (usually a colored dot or number of alerts available).


## Overall Look & Feel

Run app in light mode and dark mode (set on phone, not in app), verify colors and
display look reasonable.  Click buttons, look at backgrounds, enabled/disabled
colors, ***and Notifications***.

Things to be on the lookout for during tests below:

* Confirm spacing, colors, text all appear reasonable
* App doesn't jerk or do anything jarring
* App isn't sluggish or unresponsive
* Icons and text appear crisp and clear


## App Launch and Terminations

Start the countdown and press the soft "O" button to display the launcher screen.  Tap the RUOK icon and confirm it displays "Stop" button on the home screen and the progress bar correctly displays the time remaining.  Check-in button is enabled.

Start the countdown and press the soft square button and (force) quit the app.  Tap the app's icon on the launcher page to re-launch it.  Confirm "Stop" button on home screen and and the progress bar shows the correct time remaining.  Check-in button is enabled.

Go to the app's info page outside the application.  Confirm the version number matches the github tag/branch.



# Home Screen

Toggle Movement button on and off.  Confirm text changes from "Ignore phone movement"
to "Alarm if phone stays still".

Toggle Start/Stop button on and off.  Confirm screen switches to the countdown screen
and clicking the "main" button to return to the main screen shows the correct state
of the button.

Click each button on the bottom button bar.  Confirm the screen switches and the icon for the selected screen on the bottom button bar is filled-in.



## Contact Screen

### With Contacts Permission

Uninstall and reinstall the app, if necessary, so there is no selected contact.

Grant access to contacts for RUOK:

Click to launch the contact selector.  Don't select anything and click the "<-" (back) button at the top.  Confirm main screen says "Pick a contact" and the Start button is disabled.

Click to launch the contact selector.  Select any entry and click the "<-" (back) button.  Confirm name and phone number are displayed under "Contact", the Start button is enabled, and there is no warning banner at the bottom.  Repeat to confirm the newly-selected contact is displayed.

Launch the phone's Contacts app and confirm you have at least one entry without any phone numbers.  Launch RUOK and click to launch the contact selector.  Confirm only contacts with phone numbers are displayed.


### Without Contacts Permission

Deny access to contacts for RUOK:

Click to launch the contacts selector.  Enter name and phone number, click OK.  Confirm correct name and number appear on home screen.

Click to launch the contacts selector.  Enter name and phone number, click the "<-" (back) button at the top.  Confirm name and number on home screen remain unchanged.

Click to launch the contacts selector.  Clear out the name field and enter a phone number, click OK.  Confirm only the number appear on home screen.

Click to launch the contacts selector.  Clear enter a name and clear out the phone number edit box, click OK.  Confirm only the name appears under "Contact", the Start button is disabled, and a warning stripe tells you to pick a contact or enter a phone number.

Click to launch the contacts selector.  Clear out the name field and the phone number field, click OK.  Confirm "Pick a contact" appears in the Contact section, the Start button is disabled, and a warning stripe tells you to pick a contact or enter a phone number.



## Location Screen

*For all tests below, keep an eye on the two sample text messages to confirm they always show the
correct location value.*

Click Location to launch the location screen.  Do nothing and click back.  Confirm the main screen displays "Pick a location".


### Text-entry

Click Location to launch the location screen.  Type in text to the text box and click the back button.  Confirm text entered shows under "Location" on main screen.

Repeat but click "OK", confirm shows under "Location".

Confirm text other than "Pick a location" shows on main screen.  Click Location to launch the location screen.  Remove all text so the text-entry box is blank.  Click OK.  Confirm "Pick a location" shows on main screen.  Confirm there is no blank row in the drop-down menu on the location screen.

On the Location screen, enter spaces at the start, end, and between other letters.  Click OK and return to the Location screen.  Confirm leading and trailing spaces were trimmed and no inter-letter spacing was changed.


### Drop-down

Click Location to launch the location screen.  Open the drop-down from the "..." left menu.  Confirm the starting list of values is "Home", "Office", and "Gym" with no blank values.

On the Location screen, enter a new value and click OK.  Return to the Location screen and confirm the value you typed appears in the text box and at the top of the drop-down menu.

Look at the drop-down menu, close the drop-down menu, and hand-type the value that appeared at the end of the drop-down menu.  Click OK.  Confirm the drop-down menu doesn't list this value at the bottom and confirm the value appears only once at the top of the drop-down menu.

Repeat, but click the bottom entry in the drop-down menu.  Close and open the drop-down menu while on the Location screen and confirm the menu items do not change positions.  Click OK and return to the Location screen.  Confirm it moves to the top.

Enter 8 values in the text box "item1" through "item8".  For each value, click OK, return to the Location screen and confirm it appears at the top of the drop-down menu and that older values are below it, and that there are never more than 6 entries.



# Countdown Screen

## Counting-down

Start the countdown, confirm top text says "Running" (green text), progress bar is counting down (not full), Check-in button is enabled, and three time numbers change over time.  The checkmark table appears at the bottom of the screen.

Click Check-In button and confirm the Progress Bar resets to full.  Verify times and count-down amount are correct.

Stop the countdown.  Confirm top text says "Idle", progress bar is disabled, and Check-in button is disabled.

Start the countdown and wait for the progress bar to reach zero.  Confirm:
* Starts full, drains over time
* Turns yellow at T-3 minutes and T-2 minutes
* Turns red at T-1 minute
* Stays empty at T-0 minutes and for all minutes after this

Confirm checkmarks appear on the table at the bottom at the right times.


## Call Contact

On the main screen, confirm there is no selected contact.  Click to the Countdown Screen and confirm the Call Contact button is disabled with a red error bar saying no contact was selected.

Select a contact on the main screen and return to the Countdown screen.  Confirm no error stripe and click the Call Contact button.  Confirm app switches to phone (full-screen) with phone on speakerphone.



# Settings Screen


## Countdown Length

Slide to select each value on the slider.  Confirm the numbers are correct (multiples of 5 between 5 and 60) and the table below shows the correct math.

Click to the Countdown Length screen. Change nothing.  Click "<--" (back) button and confirm Settings screen shows the same value.

Click to the Countdown Length screen. Change the slider value.  Click "<--" (back) button and confirm Settings screen shows the new value.

Click to the Countdown Length screen. Change the slider value.  Click "OK" button and confirm Settings screen shows the new value.

Start the countdown.  Change the length on the Countdown Length screen and click OK.  Click to the countdown screen.  Confirm countdown reset and is now using the new length.



## Sound and Alert Style

Click to the Sount Style screen.  Click each radio button and click each "Try it out" button.  Confirm each sound for each pack is played clearly.  **NB:** the emulator is bad at this, but a real phone will likely work correctly.  Run this test on a physical phone.

Click Default.  Start the countdown and let it complete.  Check the sound for every event.

Click "Chimes".  Start the countdown and let it complete.  Check the sound for every event.

Click "Amy".  Start the countdown and let it complete.  Check the sound for every event.

Click "Retro".  Start the countdown and let it complete.  Check the sound for every event.

Stop the countdown.  Click "Default".  Start the countdown.  Confirm the default sound plays for T-3 event.  Change to "Chimes" while the countdown is running.  Confirm T-2 sound plays the "Chimes" sound.


## Alert Volume Level

### System Volume Level

Click "Use system volume level".  Confirm slider is invisible.  Click "Override system volume".  Click slider appear and is enabled.

Set the phone's Media volume, Call volume and Ring volume to one-quarter to max.  Set the phone's Alarm volume half-way to max.  With "Use system volume level" selected, try all six "Try it out" buttons and confirm they play at the medium volume (this is subjective) and not the one-quarter volume (this confirms all alerts use "Alert" sound channel).

Set the phone's Alarm volume to nearly silent.  Click all six "Try it out" buttons and confirm they are much quieter.

Set the phone's Alarm volume all the way to max.  Click all six "Try it out" buttons and confirm they are all loud.


### Override

Leave phone's Alarm volume at max.  Switch to Override in RUOK and set slider all the way to the
left.  Click all six "Try it out" buttons and confirm they are all silent.

Set the phone's Alarm volume to quiet.  Switch to Override in RUOK and set slider all the way to the right.  Click all six "Try it out" buttons and confirm they are all much louder than the system's Alarm volume setting.

Repeat for every detent on the override slider: Change the detent to a new value, click "<--" (back) button, confirm percent value displayed is between 0% and 100% in 10% increments.



## Foreground on Alerts

Confirm clicking the toggle button on and off doesn't produce any errors.

Deny foreground on the permission screen and return to the App Foreground Setting screen. Confirm red banner saying this isn't usable without the permission granted.

Allow foreground on the permission screen and return to the App Foreground Setting screen.  Confirm the toggle is enabled.



## Movement Settings

Go to the Movement Settings screen.  Confirm moving the phone creates an updating bar chart that
reflects the amount of phone movement.  The lower values (no bar present) should correspond to
a phone that isn't moving.  The upper value should be for a phone under vigorous movement.  The
phone should hit the top value for the graph under a reasonable amount of movement and shouldn't
require moving the phone in exaggerated or dangerous amounts or great acceleration to reach the top of the graph.

Slide the slider and confirm it moves in increments of 5 (no rounding errors or off-by-1 values).
Lowest value should be 5, largest should be 20.

Make changes to the slider and click the back button on the top left.  Return to the screen.  The
slider should reflect the value before the last change.

Make changes to the slider and click the OK button.  Return to the screen.  The slider should
reflect the updated value.

Move the slider.  The red line on the bar chart should move in tandem from just above the lowest
value on the graph to a value about half-way up the bar chart.  Movement should be smooth as the
slider is moved.

Click on various places on the slider and confirm the slider and the red line move correctly.

Choose several different values on the slider, click OK, and confirm the Settings screen has a
summary entry for movement with the updated slider value.  For example, "Phone isn't moving below
15".



# Permissions Screen

## Permission to Send TXT Messages

On a fresh install of the app, confirm cannot send TXT messages.  Click to bring up the system dialog.  Click Deny.  Confirm app shows "Cannot send TXT messages".

Quit app, force-quit, and re-launch the app.  Confirm Permissions screen comes up first instead of home screen.

When "Can send TXT messages", confirm no ">" icon at right and confirm clicking does nothing.

On app's config screen, deny TXT message permission.  Return to RUOK.  Confirm "Cannot send TXT messages" displayed.  Click to bring up the system-provided Allow/Deny dialog.  Click Deny.  Repeat and click "Deny and don't ask again".  Click again and confirm nothing happens or changes.  Remove the "Deny and don't ask again" setting on the app's config page.

Deny sending TXT messages.  Confirm main screen has banner saying have to enable TXT messages to continue.  Confirm "Start" button is disabled.


## Permission To Make Phone Calls

On a fresh install of the app, confirm cannot make phone calls.  Click to bring up the system dialog.  Click Deny.  Confirm app shows "Cannot make phone calls".

Enable TXT messages and disable phone calls.  Quit app, force-quit, and re-launch the app.  Confirm home screen appears instead of Permissions screen (phone calls are an optional permission).

When "Can make phone calls", confirm no ">" icon at right and confirm clicking does nothing.

On app's config screen, deny phone permission.  Return to RUOK.  Confirm "Cannot make phone calls" displayed.  Click to bring up the system-provided Allow/Deny dialog.  Click Deny.  Repeat and click "Deny and don't ask again".  Click again and confirm nothing happens or changes.  Remove the "Deny and don't ask again" setting on the app's config page.

Allow TXT messages and deny phone calls.  On home screen, enter a contact.  Confirm main screen doesn't have a warning banner and confirm the Start button is enabled.  Confirm Call Contact button is disabled and there are no error banners on the screen (permission to use the phone is optional).


## Permission To Read Contacts

*(home screen section above has checks when do and don't have permission to read contacts)*

On a fresh install of the app, confirm cannot read contacts.  Click to bring up the system dialog.  Click Deny.  Confirm app shows "Cannot read contacts".

When "Can make read contacts", confirm no ">" icon at right and confirm clicking does nothing.

On app's config screen, deny read contacts.  Return to RUOK.  Confirm "Cannot read contacts" displayed.  Click to bring up the system-provided Allow/Deny dialog.  Click Deny.  Repeat and click "Deny and don't ask again".  Click again and confirm nothing happens or changes.  Remove the "Deny and don't ask again" setting on the app's config page.


## Permission To Bring App To Foreground

On a fresh install of the app, confirm cannot bring app to foreground.  Click to bring up the system dialog.  Click Deny.  Confirm app shows "Cannot bring app to foreground".

When "Can bring app to foreground", confirm no ">" icon at right and confirm clicking does nothing.

On app's config screen, deny bring app to foreground.  Return to RUOK.  Confirm "Cannot bring app to foreground" displayed.  Click to bring up the system-provided Allow/Deny dialog.  Click Deny.  Repeat and click "Deny and don't ask again".  Click again and confirm nothing happens or changes.  Remove the "Deny and don't ask again" setting on the app's config page.

Deny bring app to foreground.  Under Permissions, click Foreground on Alerts.  Confirm toggle is disabled with banner "Can't enable this until you enable Foreground permission".

Grant bring app to foreground.  Under Permissions, click Foreground on Alerts.  Confirm toggle is enabled and there is no warning banner.



# App Logic

## Alarms

*These tests are about Android Alarms: the scheduled events that happen even if the app is not running.*

*Set Countdown Delay to 5 minutes for the tests in this section.*

Set "App Foreground Settings" to off so only banners are displayed.  We'll test this feature later.

Turn on and leave app in the foreground.  Confirm alarms sound at T-3 minutes, 2, 1, and 0 minutes.  Confirm checkmarks appear at each detent on the Countdown screen.

Repeat with several different Countdown Delay slider settings.

Enable and immediately disable.  Wait 6 minutes and confirm no alarms triggered any actions.

Enable and wait for first "T-3 minutes" notification to arrive.  Disable and wait another 5 minutes.  Confirm no other alarms trigger any actions.

Enable and choose another application so RUOK? isn't visible on the screen (but still running). Wait 5 minutes and confirm all alarms trigger actions.

Enable and quit the application.  Bring up the recent apps screen and quit RUOK so it isn't running at all.  Wait 5 minutes and confirm all alarms trigger notifications and send TXT msgs.

Set unlock PIN to 1234.  Choose "Power button instantly locks".  Choose option to display all notifications on lock screen.

Enable and press the power side button to lock the device and turn off the display.  Do not interact with the phone.  Wait 5 minutes to confirm all alarms trigger *sounds*.  Wake-up the phone, login, and confirm at least the last Notification message is active and all TXT messages were sent.



## Notification Banners

Enable and wait for first notification at T-3 minutes.  Confirm single notification message not displayed as a pop-up over-top of the running application.  Pull-down to view the notification & confirm text and green background icon.  Continue for T-2 minute notification too.

Repeat above for T-1 notification, which should pop-up overtop of app display and use red background icon.

Wait for times-up notification, pop-up overtop of app, red background icon.

^--- each notification should appear after previous notification is removed.  Only one notification should be present at a time.

Enable app and wait for first notification to appear.  Toggle to disable the application.  The notification should disappear.

Re-enable and wait for first notification to appear.  Click Check-in button.  Confirm notification disappears.

Enable and wait for T-1 notification to appear.  Click Check-in button.  Confirm notification disappears *(T-3 adn T-1 notifications are in different Channels)*.

Enable and wait for final notification that plays continuously.  Disable and confirm notification disappears.

Enable and lock the screen.  Wait for duration of countdown and confirm each notification appears on the lock screen (and sound plays).



## Notification Actions

Start the countdown and wait for a notification.  Try to swipe the notification right and left.  Confirm it disappears and nothing bad happens.

Start the countdown and wait for a notification with the app visible on Countdown screen.  Tap the notification and confirm the app stays in the foreground on the Countdown screen.

Start the countdown and navigate to the Preferences screen.  Wait for a notification with the app still visible.  Tap the notification and confirm the app stays in the foreground and switches to the Countdown screen.

Start the countdown, leave it on the Countdown screen, and press the "O" soft button to bring up the home screen.  Wait for first notification banner.  Click the banner and confirm the app returns to the foreground and displays the Countdown screen.

Start the countdown, switch to the Preferences screen, and press the "O" soft button to bring up the android screen.  Wait for first notification banner.  Click the banner and confirm the app returns to the foreground and switches to the Countdown screen.

Start the countdown and immediately press the square soft button.  Force quit RUOK and wait for the first notification.  Tap the notification and confirm the app is re-launched and displays the Countdown screen.



## Sounds

Start the countdown and wait for first notification at T-3 minutes.  Confirm small beep sound (without notification pop-up being visible).  Continue for T-2 minute notification too.  Confirm beep for both.

Repeat above for T-1 notification.  Confirm 1-minute sound.

Wait for times-up notification and loud times-up sound.  Wait for more than 30 seconds and confirm sound continues to play.

Start the countdown and wait for T-1 notification to appear.  As soon as the sound starts to play, disable and confirm the sound is stopped immediately before it reaches the end of the sound track.

Start the countdown and wait for final notification that plays continuously.  Disable and confirm sound stops playing immediately.

Start the countdown and lock the screen.  Wait for duration of countdown and confirm the sound plays for each notification.

Start the countdown and wait for final notification that plays continuously.  Click Check-in button and confirm sound stops playing immediately.

Switch to each other Sound Pack and let the countdown play each notification.  Confirm each sound played correctly at the correct time.


## Text Messaging

Start the countdown and confirm text message arrives explaining that the countdown has started and includes how long the whole countdown runs.

Click Check-in and confirm text message arrives saying check-in was clicked.

Change duration and confirm text message with new duration.

Change location, confirm text message with new location.

Change contact.  Confirm old contact gets switch message and new contact gets start message with
correct time and location.

Wait for final unresponsive alarm to fire.  Confirm text message saying user didn't respond.

Stop the countdown.  Confirm text message arrives saying countdown was stopped.

Click Call Contact.  Confirm three text messages sent to contact prior to making phone call to
contact.


## App Foreground Enabled

Under Settings, choose Foreground on Alerts and enable foregrounding.

Start the countdown, leave the app visible and wait for the T-1 and T-0 alert times.  The app should remain in the foreground with no errors.  Check the recent apps list and confirm RUOK doesn't exist more than once.

Restart the countdown, click the "O" soft button to return to the home page.  Wait for the T-1 alert.  The app should become visible.  Hit "O" to return to home page and wait for T-0 alert.  The app should become visible.

Repeat, but instead of home screen, bring up another app and make sure RUOK foregrounds at T-1 and T-0 alerts.

Repeat, but click the square soft button and kill RUOK.  Confirm app becomes visible (launches) at T-1 and T-0 (kill it again between T-1 and T-0).



# Movements

*Movements are largely subjective and need to be tested in real-world situations with a real human walking or running.  Test the basics in an emulator to confirm the settings and look & feel are ok, but reserve time to deploy onto a real phone and go for a walk/run.*

*For the tests below, enable "Movement" on the home screen so it shows "Alarm if phone stays still."

Open a stopwatch app on your computer or use another device so you can see accurate time-keeping.

Rules for how movements work:

| Seconds of no movement | Sound           | Banner       | TXT Message                  | Call Contact |
| :--------------------: | --------------- | ------------ | ---------------------------- | :----------: |
|  5                     | *none*          | *none*       | *none*                       | *no*         |
| 10                     | "no movement"   | "10 seconds" | "hasn't moved in 10 seconds" | *no*         |
| 15                     | "no movement"   | "15 seconds" | "hasn't moved in 15 seconds" | *no*         |
| 20                     | "call in 5 sec" | "20 seconds" | "hasn't moved in 20 seconds" | *no*         |
| 25                     | *none*          | "25 seconds" | three "calling you now!"     | **YES**      |
| 30, 35, 40, ...        | *none*          | "n seconds"  | *none*                       | *no*         |


Set the movement threshold to 20. Turn on the countdown and start the stopwatch at the same instant.  Leave the phone untouched and wait.  Using the table above:

1. Confirm the right sounds play at exactly the right times.
2. Confirm notification banner appears and is updated (instead of new banners appearing).
3. Confirm TXT messages are sent correctly as well.
4. At 25 seconds, confirm multiple TXT messages to contact and phone calls the contact automatically with speakerphone enabled.
5. Confirm number of seconds on banner continues to increase every 5 seconds past 25 seconds.


**Test resets:** Start the countdown with the phone on a table.  After 10 seconds, confirm "hasn't moved in 10 seconds" TXT message.  Shake the phone briefly and wait another 10 seconds.  Confirm 2nd TXT message also says "hasn't moved in 10 seconds".  Confirm notification banner disappears.

**Walking test:** Start walking.  Set the threshold to a reasonable number.  Start the countdown.  Walk for more than 25 seconds and confirm no alerts or messages.  Stop walking and stand in place (but sway slightly, you're waiting for a stoplight).  After 10 seconds, and subsequently alert sounds and TXT messages should be sent.

**No phone perms:** Start countdown and set phone down.  Wait 25 seconds for all alerts.  Confirm app sends TXT messages per above, but doesn't try to make a phone call.  App should not crash, but continue normally with the countdown *(verifies app doesn't try to make call without permissions)*.




# TO DO

Tests for certain notifications to happen even on Do Not Disturb: both notification banners and sounds.
