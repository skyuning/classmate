package com.example.classmate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.Utils;
import com.example.classmate.requests.AddNewsRequest;
import com.example.classmate.utils.WindowAttr;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@WindowAttr(title = "发布新鲜事")
public class PublishNewsActivity extends Activity implements OnClickListener {

    private static final int REQUEST_CHOOSE_PHOTO = 11;

    private String mImgPath;

    @ViewInject(id = R.id.news_photo)
    private ImageView mNewsPhoto;

    @ViewInject(id = R.id.news_info)
    private EditText mNewsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_news);
        ViewAnnotation.bind(getWindow().getDecorView(), this);

        mNewsPhoto.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_submit) {
            try {
                XHttpClient.sendRequest(
                        new AddNewsRequest(this, mImgPath, mNewsInfo.getText().toString()),
                        new XHttpCallbacks.DefaultHttpCallback() {

                            @Override
                            public void onSuccess(AHttpResult result) {
                                Toast.makeText(PublishNewsActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "上传失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.news_photo:
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
            break;

        default:
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CHOOSE_PHOTO)
                && (resultCode == Activity.RESULT_OK)) {
            mImgPath = Utils.uri2Path(this, data.getData());
            try {
                Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(
                        new File(mImgPath)));
                if (null != bm)
                    mNewsPhoto.setImageBitmap(bm);
                else
                    mNewsPhoto
                            .setImageResource(android.R.drawable.ic_menu_report_image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                mNewsPhoto
                        .setImageResource(android.R.drawable.ic_menu_report_image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
