package com.example.daygrampj;

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
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;

public class DaygramEdit extends AppCompatActivity {

    private RecyclerAdapter recyclerAdapter;

    myDBHelper myDBHelper;
    SQLiteDatabase sqlDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daygram_edit);

        EditText etContent = findViewById(R.id.etContent);
        TextView tvEditPageDate = findViewById(R.id.tvEditPageDate);

        Intent intent = getIntent();
        boolean exitstTodayDiary = intent.getBooleanExtra("HasDiaryToday", false);
        int positionForSQL = intent.getIntExtra("recyclerPosition", 0);
        Calendar calendarForHeader = Calendar.getInstance();

        int tableId;
        String tableDate;
        String tableContent;
        Date todayDate;
        myDBHelper = new myDBHelper(this);

        if(exitstTodayDiary) {

            sqlDB = myDBHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM daygram;", null);

            cursor.moveToLast();
            tableId = cursor.getInt(0);
            tableDate = cursor.getString(1);
            tableContent = cursor.getString(3);
            todayDate = new Date(tableDate);

            calendarForHeader.setTime(todayDate);

            cursor.close();
            sqlDB.close();

            etContent.setText(tableContent);
        } else {

            sqlDB = myDBHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT * FROM daygram;", null);

            cursor.moveToPosition(positionForSQL);
            tableId = cursor.getInt(0);
            tableDate = cursor.getString(1);
            tableContent = cursor.getString(3);
            todayDate = new Date(tableDate);

            calendarForHeader.setTime(todayDate);

            cursor.close();
            sqlDB.close();

            etContent.setText(tableContent);

        }

        int headerWeekDayNum = calendarForHeader.get(Calendar.DAY_OF_WEEK);
        String headerWeekDay = MethodForDaygram.headerWeekDaySwitch(headerWeekDayNum);

        int headerMonthNum = calendarForHeader.get(Calendar.MONTH) + 1;
        String headerMonth = MethodForDaygram.headerMonthSwitch(headerMonthNum);

        String headerDay = String.valueOf(calendarForHeader.get(Calendar.DAY_OF_MONTH));
        String headerYear = String.valueOf(calendarForHeader.get(Calendar.YEAR));

        tvEditPageDate.setText(headerWeekDay + " / " + headerMonth + " " + headerDay + " / " + headerYear);

        String tempDate = String.valueOf(todayDate);

        Button btnEdit = findViewById(R.id.btnCreate);
        btnEdit.setOnClickListener(v -> {
            String editedContent = etContent.getText().toString();

            sqlDB = myDBHelper.getWritableDatabase();
            sqlDB.execSQL("UPDATE daygram SET content='" + editedContent + "' WHERE id=" + tableId + ";");

            sqlDB.close();

            SharedPreferences sharedPrefs = getSharedPreferences("daygramContainer", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("date", tempDate);
            editor.putInt("tableId", tableId);
            editor.apply();

            finish();
        });
    }

    public class myDBHelper extends SQLiteOpenHelper {
        public  myDBHelper(Context context){
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
