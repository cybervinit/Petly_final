package com.example.username.petly;

import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.*;
import android.widget.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.example.username.myapplication.R;


/**
 * This class allows the user to create an alarm with a notification message, to be set at a specific time on a specific date.
 */
public class PetManagementActivity extends AppCompatActivity {


    private Button btnSetDate, btnSetAlarm, btnReset, btnClearAllAlarms, btnEditMsg;
    private int yearChosen, monthChosen, dayChosen;
    private int minuteTime = 0;
    private int hourTime = 0;
    private EditText etNotificationMsgInput;
    private AlarmManager alarmManager;
    private PendingIntent broadcast;
    private String notificationMessage, timeFormat;
    private Calendar cal, proper;
    private Date changeDate, properDate;
    private DatePickerDialog dialog;
    private AlertDialog alertDialog;
    public static String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    private ArrayList<PendingIntent> listOfPendingIntents = new ArrayList<>();
    private RelativeLayout relativeLayout;

    /**
     * Method that opens a TimerPickerDialog or a DatePickerDialog.
     *
     * @param id - Identifier, signalling which Dialog to be opened.
     * @return - The Dialog.
     */
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new TimePickerDialog(PetManagementActivity.this, TimePickerListener, hourTime, minuteTime,
                    false);
        } else if (id == 1) {

            proper = Calendar.getInstance();
            properDate = proper.getTime();
            dialog = new DatePickerDialog(this, DatePickerListener, proper.get(Calendar.YEAR), proper.get(Calendar.MONTH), proper.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return dialog;
        }
        return new TimePickerDialog(PetManagementActivity.this, TimePickerListener, hourTime, minuteTime,
                false);
    }


    /**
     * Method that is the TimePickerDialog onClickListener. Sets hour and minute variables.
     */
    protected TimePickerDialog.OnTimeSetListener TimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            hourTime = hourOfDay;
            minuteTime = minute;

        }
    };

    /**
     * Method that is the DatePickerDialog onClickListener. Sets the yar, month, and day variables. Calls another method which
     * sets the calender to these specific times.
     */
    protected DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yearChosen = year;
            monthChosen = monthOfYear;
            dayChosen = dayOfMonth;
            figureOutDateChanges();

        }
    };


    /**
     * Method that figures out whether the time was AM or PM, to eliminate the need of 24-hr timings.
     *
     * @return AM or PM.
     */
    private String findAMorPM(int hourOfDay) {
        if (hourOfDay < 12) {
            return "AM";
        }
        return "PM";
    }


    /**
     * Method that fixes the minute, by adding 0 in front if the minute is less then 10.
     *
     * @param minute - The minute value.
     * @return - The properly formatted minute.
     */
    private String findMinute(int minute) {
        if (minute > 9) {
            return ":";
        }
        return ":0";
    }

    /**
     * Method that properly formats the hour so that 24-hr is eliminated.
     *
     * @param hourOfDay - The hour vlaue.
     * @return - The peroply formatted hour.
     */
    private String getHourMessage(int hourOfDay) {
        if (hourOfDay == 0 || hourOfDay == 12) {
            return String.valueOf(12);
        } else if (hourOfDay < 12) {
            return String.valueOf(hourOfDay);
        }
        return String.valueOf(hourOfDay - 12);

    }

    /**
     * Method that stores the calendar date values into a specific variable. Creates alarm, and also alerts the user
     * when this has been done.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleCalendarAndAlarm() {


        timeFormat = getHourMessage(hourTime) + findMinute(minuteTime) + minuteTime
                + findAMorPM(hourTime);


        int dayOutput = cal.get(Calendar.DAY_OF_MONTH);
        int monthOutput = cal.get(Calendar.MONTH) + 1;
        int yearOutput = cal.get(Calendar.YEAR);
        Toast.makeText(PetManagementActivity.this, "Alarm is set for: " + "(" + months[monthOutput - 1] + " " + dayOutput + "," + yearOutput + ") at " + timeFormat, Toast.LENGTH_LONG).show();


        SharedPreferences sharedPreferences = getSharedPreferences("NotiMessage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("msg", notificationMessage);
        editor.commit();
        NotificationHolder.notificationMessagesAL.add(new NotificationHolder(notificationMessage, cal, AlarmReceiver.number_of_notifications));
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);


    }


    @Override
    /**
     * Method that initializes all of the buttons, as well as creates their onClickListeners. Also disables buttons from the
     * start to aid the user. (So they know what to click first).
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_management);
        setTitle("Create Reminder");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnSetAlarm = (Button) findViewById(R.id.button);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnEditMsg = (Button) findViewById(R.id.btnEditMsg);
        btnClearAllAlarms = (Button) findViewById(R.id.btnClearAllAlarms);
        relativeLayout = (RelativeLayout) findViewById(R.id.layoutId);

        btnReset.setEnabled(true);
        btnSetAlarm.setEnabled(false);
        btnSetDate.setEnabled(false);
        btnEditMsg.setEnabled(false);
        btnClearAllAlarms.setEnabled(false);


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Notification Message")
                .setMessage("Enter your notification message here:");
        etNotificationMsgInput = new EditText(this);
        etNotificationMsgInput.setFilters( new InputFilter[] { new InputFilter.LengthFilter(200) } );
        builder.setView(etNotificationMsgInput);

        /**
         * Method that sets the AlertDialog positive button. Allows them to save their message they want to be displayed
         * when the notification is created.
         */
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (etNotificationMsgInput.getText().length() <= 0) {
                    Toast.makeText(PetManagementActivity.this, "Please Enter a Message. Do not leave blank.", Toast.LENGTH_SHORT).show();
                } else {
                    notificationMessage = etNotificationMsgInput.getText().toString();
                    btnEditMsg.setEnabled(false);
                    btnSetAlarm.setEnabled(true);
                    btnReset.setEnabled(false);
                    Toast.makeText(PetManagementActivity.this, "Notification Message Set", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });


        /**
         * Method that sets the AlertDialog negative button. Allows the user to cancel their message,
         * or is useful if they by mistake clicked btnEditMsg.
         */
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etNotificationMsgInput.getText().length() <= 0) {
                    Toast.makeText(PetManagementActivity.this, "Please Enter a Message. Do not leave blank.", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();


            }
        });

        alertDialog = builder.create();


        /**
         * Method that is the edit message button's onClickListener. Shows an alert dialog so the user can
         * enter their message.
         */
        btnEditMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.show();
            }
        });


        /**
         * Method that is the set alarm button's onClickListener. Initializes the calender object, as well as
         * calls for the TimePickerDialog to be shown.
         */
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
                notificationIntent.addCategory("android.intent.category.DEFAULT");

                final int alarmId = (int) System.currentTimeMillis();
                broadcast = PendingIntent.getBroadcast(PetManagementActivity.this, alarmId
                        , notificationIntent, 0);
                listOfPendingIntents.add(broadcast);


                cal = Calendar.getInstance();

                btnSetAlarm.setEnabled(false);
                btnSetDate.setEnabled(true);
                showDialog(0);


            }
        });

        /**
         * Method that is the reset/add new alarm onClickListener. Creates a new alarm, by resetting everything.
         */
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnReset.setEnabled(false);
                btnEditMsg.setEnabled(true);

            }
        });

        /**
         * Method that is the set date button's onClickListener. Sets the date.
         */
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSetDate.setEnabled(false);
                showDialog(1);
                btnReset.setEnabled(true);
                btnClearAllAlarms.setEnabled(true);


            }
        });

        /**
         * Method that is the clear all alarms onClickListener. This method clears all pending notifications.
         */
        btnClearAllAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllAlarms();
                NotificationHolder.notificationMessagesAL.clear();
                NotificationHolder.notificationDetails.clear();
                Toast.makeText(PetManagementActivity.this, "All Alarms Cleared", Toast.LENGTH_SHORT).show();

            }
        });


    }

    /**
     * Method that deletes all alarms PendingIntent objects from an array list.
     */
    private void deleteAllAlarms() {
        for (PendingIntent x : listOfPendingIntents) {
            alarmManager.cancel(x);
        }

    }

    /**
     * Method that sets the calendars information. Hour, minute, second, year, month, date. Calls the handleCalendarAndAlarm() method
     * to finally create the alarm.
     */
    private void figureOutDateChanges() {
        cal.set(Calendar.HOUR_OF_DAY, hourTime);
        cal.set(Calendar.MINUTE, minuteTime);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.YEAR, yearChosen);
        cal.set(Calendar.MONTH, monthChosen);
        cal.set(Calendar.DAY_OF_MONTH, dayChosen);

        changeDate = cal.getTime();
        properDate = proper.getTime();

        if (changeDate.getTime() < properDate.getTime()) {

            cal.add(Calendar.DAY_OF_MONTH, 1);
            changeDate = cal.getTime();
        }


        handleCalendarAndAlarm();
    }


    @Override
    /**
     * Method that allows the user to go 'back' one activity.
     * @param item - The menu item that was clicked.
     * @return - True.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }


}
