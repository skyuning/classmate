package com.example.classmate;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.data.Holiday;
import com.example.classmate.utils.WindowAttr;

@WindowAttr(title = "活动详情")
public class HolidayDetailActivity extends BaseActivity {

    @ViewInject(id = R.id.holiday_detial_title)
    private TextView mTitle;

    @ViewInject(id = R.id.holiday_detail_desc)
    private TextView mDesc;

    @ViewInject(id = R.id.holiday_detail_date)
    private TextView mDate;

    @ViewInject(id = R.id.holiday_detail_icon)
    private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_detail);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        Holiday holiday = (Holiday) getIntent().getSerializableExtra("holiday");
        mTitle.setText(holiday.title);
        mDesc.setText(holiday.desc);
        mDate.setText(holiday.date);
        if (holiday.category == 1)
            mIcon.setImageResource(R.drawable.holiday_birthday);
        else if (holiday.category == 2)
            mIcon.setImageResource(R.drawable.holiday_festival);
        else if (holiday.category == 3)
            mIcon.setImageResource(R.drawable.holiday_activity);
    }

}
