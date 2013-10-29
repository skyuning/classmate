package com.example.classmate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback.AHttpResult;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.Utils;
import com.example.classmate.requests.AddNewsRequest;
import com.example.classmate.utils.WindowAttr;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@WindowAttr(title = "发布新鲜事")
public class PublishNewsActivity extends BaseActivity implements OnClickListener {

    private static final int REQUEST_TAKE_PHOTO = 11;
    private static final int REQUEST_CHOOSE_PHOTO = 12;

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
        setRightImgBtn(R.drawable.icon_ok, this);
    }

    public void submit() {
        try {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("正在发送");
            dialog.show();
            XHttpClient.sendRequest(new AddNewsRequest(this, mImgPath,
                    mNewsInfo.getText().toString()),
                    new XHttpCallbacks.DefaultHttpCallback() {

                        @Override
                        public void onPostExecute(AHttpResult result) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onSuccess(AHttpResult result) {
                            Toast.makeText(PublishNewsActivity.this, "上传成功",
                                    Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void onFaild(AHttpResult result) {
                            super.onFaild(result);
                            Toast.makeText(PublishNewsActivity.this, "上传失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "上传失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.news_photo:
            addPhoto();
            break;
            
        case R.id.right_img_btn:
            submit();
            break;

        default:
            break;
        }
    }
    
    private void addPhoto() {
        String[] items = { "拍照", "相册" };
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case 0:
                    takePhoto();
                    break;
                    
                case 1:
                    choosePhoto();
                    break;

                default:
                    break;
                }
            }
        };
        new AlertDialog.Builder(this).setTitle("选择照片").setItems(items, listener).show();
    }
    
    private void takePhoto() {
        mImgPath = Environment.getExternalStorageDirectory() + "/classmate/tmp_news_photo.png";
        File tmpPhotoFile = new File(mImgPath);
        Uri uri = Uri.fromFile(tmpPhotoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CHOOSE_PHOTO) || (requestCode == REQUEST_TAKE_PHOTO)) {
            if (resultCode != RESULT_OK)
                return;
        } else {
            return;
        }
        
        if (requestCode == REQUEST_CHOOSE_PHOTO)
            mImgPath = Utils.uri2Path(this, data.getData());
            
        try {
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(
                    new File(mImgPath)));
            if (null != bm)
                mNewsPhoto.setImageBitmap(bm);
            else
                mNewsPhoto.setImageResource(android.R.drawable.ic_menu_report_image);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
                mNewsPhoto.setImageResource(android.R.drawable.ic_menu_report_image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
