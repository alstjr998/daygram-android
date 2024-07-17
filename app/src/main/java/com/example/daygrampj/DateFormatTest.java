package com.example.daygrampj;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatTest {
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d");

        Calendar cal = Calendar.getInstance();

        System.out.println(cal.getTime());
    }
}