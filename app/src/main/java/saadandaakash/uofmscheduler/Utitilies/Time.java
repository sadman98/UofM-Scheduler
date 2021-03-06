package saadandaakash.uofmscheduler.Utitilies;

import java.util.ArrayList;

import saadandaakash.uofmscheduler.Utitilies.Section.Meeting;

public class Time {

    //Times in minutes since 12:00 AM
    private double startTime = 0;
    private double endTime = 0;

    //REQUIRES: Valid strings in form X:XX AM/PM or XX:XX AM/PM
    //EFFECTS: Will create a time objects with those values
    public Time(String startTimeString, String endTimeString){

        startTime = convertToMinutes(startTimeString);
        endTime = convertToMinutes(endTimeString);

    }

    //REQUIRES: A valid string in form XX:XX AM/PM
    //EFFECTS: Will return number of minutes since midnight
    private int convertToMinutes(String timeString){

        System.out.println("Time:" + timeString + ".");

        int returnTime = 0;
        returnTime = returnTime + 60 * timeString.charAt(0);
        int index = timeString.indexOf(":") + 1;
        returnTime = returnTime + Integer.parseInt(timeString.substring(index, index + 2));

        if(timeString.contains("PM")){
            returnTime = returnTime + (12 * 60);
        }

        return returnTime;

        /*
        System.out.println("Time: " + timeString + ".");
        return 0;
        */
    }

    //REQUIRES: Two valid Time Objects
    //EFFECTS: Will return true if the times are overlapping
    public static boolean isOverlapping(Time first, Time second){
        return first.startTime < second.endTime && first.endTime > second.startTime;
    }

    //REQUIRES: Two not null sections
    //EFFECTS: Returns if the sections are overlapping
    public static boolean sectionsOverlap(Section first, Section second){

        ArrayList<Meeting> firstMeetings = first.meetings;
        ArrayList<Meeting> secondMeetings = second.meetings;

        for(Meeting firstSectionMeeting : firstMeetings){
            for(Meeting secondSectionMeeting : secondMeetings){
                String[] firstTimes = firstSectionMeeting.times.split(" - ");
                String[] secondTimes = secondSectionMeeting.times.split(" - ");
                Time t1 = new Time(firstTimes[0], firstTimes[1]);
                Time t2 = new Time(secondTimes[0], secondTimes[1]);

                if (isOverlapping(t1, t2)) {
                    return true;
                }
            }
        }

        return false;
    }

    //REQUIRES: A valid Meeting Object
    //EFFECTS: Returns the appropriate Time Objects in a arrayList
    
}
