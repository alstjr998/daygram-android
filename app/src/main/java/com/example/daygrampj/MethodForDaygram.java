package com.example.daygrampj;

public class MethodForDaygram {

    static String weekdaySwitch(int weekDayNum){
        String weekDay = "";
        switch (weekDayNum) {
            case 1:
                weekDay = "SUN";
                break;
            case 2:
                weekDay = "MON";
                break;
            case 3:
                weekDay = "TUE";
                break;
            case 4:
                weekDay = "WED";
                break;
            case 5:
                weekDay = "THU";
                break;
            case 6:
                weekDay = "FRI";
                break;
            case 7:
                weekDay = "SAT";
                break;
        }
        return weekDay;
    }

    static String headerWeekDaySwitch(int headerWeekDayNum){
        String headerWeekDay = "";
        switch (headerWeekDayNum) {
            case 1:
                headerWeekDay = "SUNDAY";
                break;
            case 2:
                headerWeekDay = "MONDAY";
                break;
            case 3:
                headerWeekDay = "TUESDAY";
                break;
            case 4:
                headerWeekDay = "WEDNESDAY";
                break;
            case 5:
                headerWeekDay = "THURSDAY";
                break;
            case 6:
                headerWeekDay = "FRIDAY";
                break;
            case 7:
                headerWeekDay = "SATURDAY";
                break;
        }
        return headerWeekDay;
    }

    static String headerMonthSwitch(int headerMonthNum){
        String headerMonth = "";
        switch (headerMonthNum) {
            case 1:
                headerMonth = "JANUARY";
                break;
            case 2:
                headerMonth = "FEBRUARY";
                break;
            case 3:
                headerMonth = "MARCH";
                break;
            case 4:
                headerMonth = "APRIL";
                break;
            case 5:
                headerMonth = "MAY";
                break;
            case 6:
                headerMonth = "JUNE";
                break;
            case 7:
                headerMonth = "JULY";
                break;
            case 8:
                headerMonth = "AUGUST";
                break;
            case 9:
                headerMonth = "SEPTEMBER";
                break;
            case 10:
                headerMonth = "OCTOBER";
                break;
            case 11:
                headerMonth = "NOVEMBER";
                break;
            case 12:
                headerMonth = "DECEMBER";
                break;
        }
        return headerMonth;
    }

    static String capitalizeFirstLetter(String word){
        String firstLetter = word.substring(0, 1);
        String remainLetter = word.substring(1);
        firstLetter = firstLetter.toUpperCase();
        remainLetter = remainLetter.toLowerCase();
        return firstLetter + remainLetter;
    }
}
