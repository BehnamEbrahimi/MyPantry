package com.example.mypantry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    EditText edShopName, edTime;
    CalendarView calendarView;
    Button btnSubmitToGC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        edShopName = findViewById(R.id.edShopName);
        edTime = findViewById(R.id.edTime);
        calendarView = findViewById(R.id.calendarView);

        final int[] chosenYear = {0};
        final int[] chosenMonth = {0};
        final int[] chosenDay = {0};
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                chosenYear[0] = year;
                chosenMonth[0] = month;
                chosenDay[0] = day;
            }
        });


        btnSubmitToGC = findViewById(R.id.btnSubmitToGC);

        btnSubmitToGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edTime.getText().toString();
                DateFormat formatter = new SimpleDateFormat("hh:mm");
                try {
                    Date date = formatter.parse(str);

                    long startMillis = 0;
                    long endMillis = 0;
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.set(chosenYear[0], chosenMonth[0], chosenDay[0], date.getHours(), date.getMinutes());
                    startMillis = beginTime.getTimeInMillis();
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(chosenYear[0], chosenMonth[0], chosenDay[0], date.getHours() + 1, date.getMinutes());
                    endMillis = endTime.getTimeInMillis();
                    addEvent("Shopping Outing", edShopName.getText().toString() ,startMillis, endMillis);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void addEvent(String title, String location, long
            begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION,
                        location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
