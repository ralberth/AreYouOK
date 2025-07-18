# Test Plan

The following tests to be performed on a deployment of the application onto my physical phone.
Don't use an emulator, since it likely isn't using the same OS release as my Galaxy 9S.  If you
prefer, load a specific version and phone, like the Pixel 4 at API level 29.

To avoid annoying other people, consider setting the delivery phone number to your phone so all
text messages stay on your device.

Try this to get a table of contents:

```
grep '^#' TESTING.md 
```


# Basic User Interface

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

Go to the app's info page outside the application.  Confirm the version number matches the github tag/branch.



## Colors

Run app in light mode and dark mode (set on phone, not in app), verify colors and display look
reasonable.  Click buttons, look at backgrounds, enabled/disabled colors, and Notifications.


## Select Duration Screen

Click the duration from the home page to go to the "Select Duration" screen.

Slide to select each value on the slider.  Confirm the numbers are correct (multiples of 5) and the table below shows the correct math.


## Select Contact

Click to launch the contact selector.  Don't select anything and click the "<-" (back) button at the top.  
Confirm main screen says "Pick a contact".

Launch the phone's Contacts app and confirm you have at least one entry without any phone numbers.  Launch RUOK and click to launch the contact selector.  Confirm only contacts with phone numbers are displayed.

Click to launch the contact selector.  Pick a valid contact and confirm the main screen displays the correct name and phone number.  Repeat to confirm the new contact replaces the old one on display.


## Select Location

*For all tests below, keep an eye on the two sample text messages to confirm they always show the correct location value.*

Click Location to launch the location screen.  Open the drop-down from the "..." left menu.  Confirm the starting list of values is "Home", "Office", and "Gym" with no blank values.

Click Location to launch the location screen.  Do nothing and click back.  Confirm the main screen displays "Pick a location".

Click Location to launch the location screen.  Type in text to the text box and click the back button.  Confirm text entered shows under "Location" on main screen.

Repeat but click "OK", confirm shows under "Location".

Confirm text other than "Pick a location" shows on main screen.  Click Location to launch the location screen.  Remove all text so the text-entry box is blank.  Click OK.  Confirm "Pick a location" shows on main screen.  Confirm there is no blank row in the drop-down menu on the Location screen.

On the Location screen, try entering spaces at the start, end, and between other letters. Confirm the edit box will not allow any leading or trailing spaces to be entered.

On the Location screen, enter a new value and click OK.  Return to the Location screen and confirm the value you typed appears in the text box and at the top of the drop-down menu.

Look at the drop-down menu, close the drop-down menu, and hand-type the value that appeared at the end of the drop-down menu.  Click OK.  Confirm the drop-down menu doesn't list this value at the bottom and confirm the value appears only once at the top of the drop-down menu.

Repeat, but click the bottom entry in the drop-down menu.  Close and open the drop-down menu while on the Location screen and confirm the menu items do not change positions.  Click OK and return to the Location screen.  Confirm it moves to the top.

Enter 8 values in the text box "item1" through "item8".  For each value, click OK, return to the Location screen and confirm it appears at the top of the drop-down menu and that older values are below it, and that there are never more than 6 entries.



## Permissions Screen

For each permission entry on the Permission screen:

1. Click to bring up the system-provided Allow/Deny dialog.  Click Deny.  Repeat and click "Deny and don't ask again".  Click again and confirm nothing happens or changes.
2. Uninstall and re-install the app to remove the "Deny and don't ask again" setting.


Outside the app on the app's Info page under "Advanced", make sure "Display over other apps" is "Cannot bring app to foreground".  Under Permissions, confirm "Permission to bring app to foreground" is "Cannot bring app to foreground".

On Settings page under "Foreground on alerts", make sure switch is disabled and red error banner underneath is displayed.

Outside the app on the app's Info page under "Advanced", make sure "Display over other apps" is "Allowed".  Under Permissions, confirm "Permission to bring app to foreground" is "Can bring app to foreground".

On Settings page under "Foreground on alerts", make sure switch is not disabled and there is no red error banner underneath.




## Permissions and Starting the Countdown

Unstall and re-install the app to start fresh with no granted permissions.  Confirm Permissions screen
shows everything denied.

Confirm the main "Enable" toggle is disabled.

Enable phone calls on permissions screen and confirm main Enable toggle remains disabled and red text below says there are missing permissions and to pick a contact.

Enable contacts on permissions screen and confirm main Enable toggle remains disabled and red text below says there are missing permissions and to pick a contact.

Enable TXT messages on permissions screen and confirm main Enable toggle remains disabled and red text below says (only) to pick a contact.

Pick a contact and confirm main Enable toggle changes to **enabled**.

Tick the Enable toggle to turn on the countdown.  Go outside the app and deny TXT message permissions on the app.  Return to the app and confirm the toggle is on.  Tick the toggle to turn it off (it should be usable).  Confirm the toggle is now disabled and can't be used.  Confirm red text below says need permissions.



## Basic Countdown Screen (countdown not running)

Click on the bell on the bottom of the nav bar to the Countdown screen.  Confirm progress bar is disabled and the check-in button is disabled.

On the main screen, confirm there is no selected contact.  Click to the Countdown Screen and confirm the Call Contact button is disabled with a red error bar saying no contact was selected.

Select a contact on the main screen and return to the Countdown screen.  Confirm no error stripe and click the Call Contact button.  Confirm app switches to phone (full-screen) with phone on speakerphone.



## Basic Settings Screen

Click on the gear to bring up the Settings screen.

Set the phone's Media volume, Call volume, Ring volume, and Alarm volume half-way to max.  With "Use system volume level" selected, try all three buttons and confirm they play at the medium volume (this is subjective).

Set the phone's Alarm volume to nearly silent.  Click all three buttons on RUOK and confirm they are much quieter.

Set the phone's Alarm volume all the way to max.  Click all three buttons on RUOK and confirm they are all loud.

Leave phone's Alarm volume at max.  Switch to Override in RUOK and set slider all the way to the left.  Try all three buttons. They should be silent.

Set the phone's Alarm volume to quiet.  Switch to Override in RUOK and set slider all the way to the right.  Try all three buttons. They should be loud.  Much louder than the system's Alarm volume setting.



## Basic App Foreground Setting screen

Confirm screen appears organized and things line up nicely.

Confirm clicking the toggle button on and off doesn't produce any errors.



# Alarm Function

*These tests are about Android Alarms: the scheduled events that happen even if the app is not running.*

*Set Countdown Delay to 5 minutes for the tests in this section.*

Set "App Foreground Settings" to off so only banners are displayed.  We'll test this feature later.

Turn on and leave app in the foreground.  Confirm alarms sound at T-3 minutes, -2, -1, and 0 minutes.  Confirm checkmarks appear at each detent on teh Countdown screen.

Repeat with several different Countdown Delay slider settings.

Enable and immediately disable.  Wait 6 minutes and confirm no alarms triggered any actions.

Enable and wait for first "T-3 minutes" notification to arrive.  Disable and wait another 5 minutes.
Confirm no other alarms trigger any actions.

Enable and choose another application so RUOK? isn't visible on the screen (but still running).
Wait 5 minutes and confirm all alarms trigger actions.

Enable and quit the application.  Bring up the recent apps screen and quit RUOK? so it isn't
running at all.  Wait 5 minutes and confirm all alarms trigger notifications and send TXT msgs.

Set unlock PIN to 1234.  Choose "Power button instantly locks".  Choose option to display all notifications on lock screen.

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



## Notification Actions

Enable and wait for a notification.  Try to swipe the notification right and left.  Confirm it
disappears and nothing bad happens.

Enable and wait for a notification with the app visible on Countdown screen.  Tap the notification and
confirm the app stays in the foreground on the Countdown screen.

Enable and navigate to the Preferences screen.  Wait for a notification with the app still visible.
Tap the notification and confirm the app stays in the foreground and switches to the Countdown screen.

Enable the app, leave it on the Countdown screen, and press the "O" soft button to bring up the home
screen.  Wait for first notification banner.  Click the banner and confirm the app returns to the
foreground and displays the Countdown screen.

Enable the app, switch to the Preferences screen, and press the "O" soft button to bring up the home
screen.  Wait for first notification banner.  Click the banner and confirm the app returns to the
foreground and switches to the Countdown screen.

Enable the app and immediately press the square soft button.  Force quit RUOK and wait for the
first notification.  Tap the notification and confirm the app is re-launched and displays the
Countdown screen.



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

Change duration and confirm text message with new duration.

Change location, confirm text message with new location.

Change contact.  Confirm old contact gets switch message and new contact gets start message with correct time and location.

Wait for final unresponsive alarm to fire.  Confirm text message saying user didn't respond.

Click toggle to disable.  Confirm text message arrives saying countdown was disabled.

Click Call Contact.  Confirm three text messages sent to contact prior to making phone call to contact.



## App Foreground Enabled

Under Settings, choose Foreground on Alerts and enable foregrounding.

Start the countdown, leave the app visible and wait for the T-1 and T-0 alert times.  The app should remain in the foreground with no errors.  Check the recent apps list and confirm RUOK doesn't exist more than once.

Restart the countdown, click the "O" soft button to return to the home page.  Wait for the T-1 alert.  The app should become visible.  Hit "O" to return to home page and wait for T-0 alert.  The app should become visible.

Repeat, but instead of home screen, bring up another app and make sure RUOK foregrounds at T-1 and T-0 alerts.

Repeat, but click the square soft button and kill RUOK.  Confirm app becomes visible (launches) at T-1 and T-0 (kill it again between T-1 and T-0).



# Movements


## Movement Permissions

*(none at this API target level)*


## Movement Settings Screen


## Movement Function and Alerts

