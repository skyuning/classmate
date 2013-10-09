package com.example.classmate;

import java.util.Calendar;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpClient;
import org.xframe.http.XHttpCallbacks;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.data.Holiday;
import com.example.classmate.requests.AddHolidayRequest;
import com.example.classmate.utils.WindowAttr;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

@WindowAttr(title = "添加活动")
public class AddHolidayActivity extends BaseActivity implements OnClickListener, OnDateSetListener {
    
    @ViewInject(id = R.id.subject)
    private EditText mSubjectView;

    @ViewInject(id = R.id.desc)
    private EditText mDescView;

    @ViewInject(id = R.id.date)
    private TextView mDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_holiday);
        ViewAnnotation.bind(this, this);
        
        setRightImgBtn(R.drawable.icon_ok, this);
        
        mDateView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_img_btn) {
            AddHolidayRequest request = new AddHolidayRequest(this,
                    mSubjectView.getText().toString(),
                    mDescView.getText().toString(),
                    mDateView.getText().toString());
            
            XHttpCallback callback = new XHttpCallbacks.DefaultHttpCallback() {
                @Override
                public void onSuccess(AHttpResult result) {
                    showToast("添加成功");
                    Holiday holiday = new Holiday();
                    holiday.category = 3;
                    holiday.title = mSubjectView.getText().toString();
                    holiday.desc = mDescView.getText().toString();
                    holiday.date = mDateView.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("holiday", holiday);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            };
            
            XHttpClient.sendRequest(request, callback);
        } else if (v.getId() == R.id.date) {
                DatePickerDialog dialog = new DatePickerDialog(this, this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%s-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
        mDateView.setText(date);
    }

}
