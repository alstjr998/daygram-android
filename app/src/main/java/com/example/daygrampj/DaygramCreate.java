package com.example.daygrampj;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class DaygramCreate extends AppCompatActivity {
    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daygram_edit);

        TextView tvEditPageDate = findViewById(R.id.tvEditPageDate);

        long dateToday = System.currentTimeMillis();
        Date dateForHeader = new Date(dateToday);
        Calendar calendarForHeader = Calendar.getInstance();
        calendarForHeader.setTime(dateForHeader);

        Log.i("오픈시간", String.valueOf(dateForHeader));

        int headerWeekDayNum = calendarForHeader.get(Calendar.DAY_OF_WEEK);

        String headerWeekDay = MethodForDaygram.headerWeekDaySwitch(headerWeekDayNum);

        int headerMonthNum = calendarForHeader.get(Calendar.MONTH) + 1;

        String headerMonth = MethodForDaygram.headerMonthSwitch(headerMonthNum);

        String headerDay = String.valueOf(calendarForHeader.get(Calendar.DAY_OF_MONTH));
        String headerYear = String.valueOf(calendarForHeader.get(Calendar.YEAR));

        tvEditPageDate.setText(headerWeekDay + " / " + headerMonth + " " + headerDay + " / " + headerYear);

        myDBHelper = new DaygramCreate.myDBHelper(this);

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            EditText etContent = findViewById(R.id.etContent);

            long dateNow = System.currentTimeMillis();

            String writedContent = etContent.getText().toString();
            Date writedDate = new Date(dateNow);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(writedDate);

            int weekDayNum = calendar.get(Calendar.DAY_OF_WEEK);

            String weekDay = MethodForDaygram.weekdaySwitch(weekDayNum);

            Log.i("시간", String.valueOf(writedDate));
            Log.i("요일", weekDay);


            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO daygram (date, weekDay, content) VALUES ('"
                    + writedDate + "', '" + weekDay + "', '" + writedContent + "');");

            sqlDB.close();

            Intent mainIntent = getIntent();
            mainIntent.putExtra("Date", dateNow);
            mainIntent.putExtra("WeekDay", weekDay);
            mainIntent.putExtra("Content", writedContent);
            setResult(RESULT_OK, mainIntent);

            finish();
        });
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context){
            super(context, "daygramDB", null, 1);

        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE daygram (id INTEGER PRIMARY KEY AUTOINCREMENT, date DATE, weekDay CHAR(8), content VARCHAR(1020));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS daygram");
            onCreate(db);
        }
    }

}
