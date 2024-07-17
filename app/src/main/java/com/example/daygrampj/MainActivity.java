package com.example.daygrampj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvBody;
    private RecyclerAdapter recyclerAdapter;

    private boolean existsTodayDiary = false;
    private String todayDt = "";


    private myDBHelper myDBHelper;
    private SQLiteDatabase sqlDB;

    List<Diary> contentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvTodayIs = findViewById(R.id.tvTodayIs);
        TextView tvDate = findViewById(R.id.tvDate);

        ScrollView scrollMain = findViewById(R.id.scrollMain);

        long dateToday = System.currentTimeMillis();
        Date dateForHeader = new Date(dateToday);
        Calendar calendarForHeader = Calendar.getInstance();
        calendarForHeader.setTime(dateForHeader);

        int headerWeekDayNum = calendarForHeader.get(Calendar.DAY_OF_WEEK);
        String headerWeekDay = MethodForDaygram.capitalizeFirstLetter(MethodForDaygram.headerWeekDaySwitch(headerWeekDayNum));

        int headerMonthNum = calendarForHeader.get(Calendar.MONTH) + 1;
        String headerMonth = MethodForDaygram.capitalizeFirstLetter(MethodForDaygram.headerMonthSwitch(headerMonthNum));

        String headerDay = String.valueOf(calendarForHeader.get(Calendar.DAY_OF_MONTH));

        tvTodayIs.setText("Today is " + headerWeekDay);
        tvDate.setText(headerMonth + " " + headerDay);

        myDBHelper = new MainActivity.myDBHelper(this);

        sqlDB = myDBHelper.getReadableDatabase();
        /*myDBHelper.onUpgrade(sqlDB, 0, 1);*/


        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM daygram;", null);


        SimpleDateFormat subFormatter = new SimpleDateFormat("M/d");
        Calendar today = Calendar.getInstance();
        todayDt = subFormatter.format(today.getTime());


        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String tableDate = cursor.getString(1);
                String formatDate = subFormatter.format(new Date(tableDate));

                String tableWeekDay = cursor.getString(2);

                String tableContent = cursor.getString(3);

                contentList.add(new Diary(formatDate, tableWeekDay, tableContent));

                if (formatDate.equals(todayDt)) {
                    existsTodayDiary = true;
                }
            }
        }

        sqlDB.close();
        cursor.close();

        Button btnGoToEdit = findViewById(R.id.btnGoToEdit);
        btnGoToEdit.setOnClickListener(v -> {
            if (existsTodayDiary) {
                //수정
                Intent intent = new Intent(getApplicationContext(), DaygramEdit.class);
                intent.putExtra("HasDiaryToday", existsTodayDiary);
                startActivityForResult(intent, 1);
            } else {
                //등록
                Intent intent = new Intent(getApplicationContext(), DaygramCreate.class);
                intent.putExtra("HasDiaryToday", existsTodayDiary);
                startActivityForResult(intent, 1);
            }

        });


        rvBody = findViewById(R.id.rvBody);
        recyclerAdapter = new RecyclerAdapter(this, contentList);


        rvBody.setAdapter(recyclerAdapter);
        rvBody.setLayoutManager(new LinearLayoutManager(this));

        rvBody.scrollToPosition(contentList.size());
        scrollMain.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sharedPrefs = getSharedPreferences("daygramContainer", Context.MODE_PRIVATE);
        String tempDate = sharedPrefs.getString("date", "");
        int tempId = sharedPrefs.getInt("tableId", 0);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove("date");
        editor.remove("tableId");
        editor.apply();

        myDBHelper = new MainActivity.myDBHelper(this);
        sqlDB = myDBHelper.getReadableDatabase();

        SimpleDateFormat subFormatter = new SimpleDateFormat("M/d");
        boolean existsDiary = false;

        Cursor cursor;
        if (tempDate.equals("")) {
            cursor = sqlDB.rawQuery("SELECT * FROM daygram;", null);
            if (cursor.getCount() != 0) {
                cursor.moveToLast();
                if (!cursor.getString(3).equals("")) {
                    String tableDate = cursor.getString(1);
                    String formatDate = subFormatter.format(new Date(tableDate));

                    String tableWeekDay = cursor.getString(2);

                    String tableContent = cursor.getString(3);

                    if (formatDate.equals(todayDt)) {
                        existsTodayDiary = true;
                    }

                    //foreach
                    for (Diary tmpDiary : contentList) {
                        if (tmpDiary.getDate().equals(formatDate)) {
                            existsDiary = true;
                            break;
                        }
                    }

                    if (!existsDiary) {
                        contentList.add(new Diary(formatDate, tableWeekDay, tableContent));
                    }

                }

            }
        } else {
            cursor = sqlDB.rawQuery("SELECT * FROM daygram WHERE id=" + tempId + ";", null);
            Diary compareDiary;
            cursor.moveToFirst();
            String tableContent = cursor.getString(3);
            for (int i = 0; i < contentList.size(); ++i) {
                compareDiary = contentList.get(i);
                String tempFormatDate = subFormatter.format(new Date(tempDate));
                if (compareDiary.getDate().equals(tempFormatDate)) {
                    compareDiary.setContent(tableContent);
                    contentList.set(i, compareDiary);
                    break;
                }
            }
        }


        sqlDB.close();
        cursor.close();

        rvBody = findViewById(R.id.rvBody);
        recyclerAdapter = new RecyclerAdapter(this, contentList);

        rvBody.setAdapter(recyclerAdapter);
        rvBody.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Date writedDate = new Date();
            writedDate.setTime(data.getLongExtra("Date", 0));
            String showDate = String.valueOf(writedDate.getDay());
            Log.i("표시 날짜", showDate);
            String weekDay = data.getStringExtra("WeekDay");
            String content = data.getStringExtra("Content");

            Log.i("받은 시간", String.valueOf(writedDate));
            Log.i("받은 요일", String.valueOf(weekDay));
            Log.i("받은 내용", String.valueOf(content));

        }

    }


    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
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

class Diary {

    Diary(String date, String weekDay, String content) {
        this.date = date;
        this.weekDay = weekDay;
        this.content = content;
    }

    String date;
    String weekDay;
    String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getContent() {
        return content;
    }

}