package com.gopher.meidcalcollection.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gopher.meidcalcollection.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NewCalendar extends LinearLayout {
    private ImageView iv_pre;
    private ImageView iv_next;
    private TextView tv_date;
    private GridView gridView;
    private Calendar curDate = Calendar.getInstance();
    private ArrayList<Date> cells;

    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        bindControl(context);
        bindControlEvent();
        rendarCalendar();
    }

    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View inflate = inflater.inflate(R.layout.calendar_view, null);
        iv_pre = (ImageView) inflate.findViewById(R.id.iv_pre);
        iv_next = (ImageView) inflate.findViewById(R.id.iv_next);
        tv_date = (TextView) inflate.findViewById(R.id.tv_date);
        gridView = (GridView) inflate.findViewById(R.id.gv_date);
    }

    private void bindControlEvent() {
        iv_pre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                rendarCalendar();
            }
        });

        iv_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, 1);
                rendarCalendar();
            }
        });

        gridView.setAdapter(new CalendarAdapter(getContext(), cells));
    }

    private void rendarCalendar() {

        SimpleDateFormat sdf = new SimpleDateFormat("MM YYYY");
        tv_date.setText(sdf.format(curDate.getTime()));

        cells = new ArrayList<>();
        Calendar clone = (Calendar) curDate.clone();

        clone.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = clone.get(Calendar.DAY_OF_WEEK) - 1;
        clone.add(Calendar.DAY_OF_MONTH, -preDays);

        int maxCells = 6 * 7;
        while (cells.size() < maxCells) {
            cells.add(clone.getTime());
            clone.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        LayoutInflater inflater;

        public CalendarAdapter(@NonNull Context context, @NonNull ArrayList<Date> dates) {
            super(context, R.layout.calendar_textview, dates);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_view, null, false);
            }

            int day = date.getDate();
            ((TextView) convertView).setText(String.valueOf(day));

//            Calendar calendar = (Calendar) curDate.clone();
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            boolean isTheSameMonth = false;
            Date now = new Date();
            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }
            if (isTheSameMonth) {
                ((TextView) convertView).setTextColor(Color.parseColor("#000000"));
            } else {
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));
            }

            if (date.getDate() == now.getDate() && date.getMonth() == now.getMonth() && date.getYear() == now.getYear()) {
                ((TextView) convertView).setTextColor(Color.parseColor("#ff0000"));
                ((CalendarTextView) convertView).isToday = true;
            }
            return convertView;

        }
    }
}
