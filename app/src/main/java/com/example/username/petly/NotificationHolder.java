package com.example.username.petly;


import java.util.*;

/**
 * This class stores the notification information the user has set, such as the time, message, and date.
 */
public class NotificationHolder {
    private String notificationMessage, time;
    private Calendar cal;
    public int identifier;
    static ArrayList<NotificationHolder> notificationMessagesAL = new ArrayList<>();
    static ArrayList<String> notificationDetails = new ArrayList<>();


    /**
     * Constructor.
     * @param notificationMessage - The message the user wishes to see once the notification arrives.
     * @param cal - Calendar object that keeps track of the time and date of the notification.
     * @param identifier - Unique number assigned to each notification created to keep track.
     */
    public NotificationHolder(String notificationMessage, Calendar cal, int identifier) {
        this.notificationMessage = notificationMessage;
        this.cal = cal;
        this.identifier = identifier;
        notificationDetails.add(setupMessage());
    }

    /**
     * Method that organises the message and time to display to the user.
     * @return The organised message with all the details.
     */
    private String setupMessage() {
        String message = "("+(cal.get(Calendar.DAY_OF_MONTH))+"/"+(cal.get(Calendar.MONTH)+1)+"/"+
                cal.get(Calendar.YEAR)+") : "+fixHour(cal.get(Calendar.HOUR_OF_DAY))+":"+
                fixMinute(cal.get(Calendar.MINUTE))+dayNight()+": "+notificationMessage;

        return message;
    }

    /**
     * Method that figures out whether the time was AM or PM, to eliminate the need of 24-hr timings.
     * @return AM or PM.
     */
    private String dayNight() {
        if (cal.get(Calendar.HOUR_OF_DAY)<12) { return " AM"; }
        return " PM";
    }

    /**
     * Method that fixes the minute, by adding 0 in front if the minute is less then 10.
     * @param i - The minute value.
     * @return - The properly formatted minute.
     */
    private String fixMinute(int i) {
        if (i>9) {
            return String.valueOf(i);
        }
        return ":0"+i;
    }

    /**
     * Method that properly formats the hour so that 24-hr is eliminated.
     * @param i - The hour vlaue.
     * @return - The peroply formatted hour.
     */
    private String fixHour(int i) {
        if (i==0 ||i==12) { return String.valueOf(12);}
        else if (i<12) { return String.valueOf(i); }
        return String.valueOf(i-12);
    }

    /**
     * Getter method.
     * @return - Time
     */
    public String getTime() {
        return time;
    }

    /**
     * Getter method.
     * @return - Identifier.
     */
    public int getIdentifier() {
        return identifier;
    }



    /**
     * Method that deletes a specific notification from the ArrayLists once the notification has went off.
     * @param number_of_notifications - The notification IDENTIFIER, helps know which notification to delete.
     */
    public static void deleteSpecificNotification(int number_of_notifications) {
        int indexToDelete = 0;
        for (int i = 0; i < notificationMessagesAL.size(); i++) {
            if (notificationMessagesAL.get(i).getIdentifier() == number_of_notifications) {
                indexToDelete = i;
                break;
            }
        }
        notificationMessagesAL.remove(indexToDelete);
        notificationDetails.remove(indexToDelete);



    }
}

