package com.example.classmate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xframe.annotation.ViewAnnotation;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;

import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.Conf;
import com.example.classmate.common.Utils;
import com.example.classmate.requests.AddAlbumRequest;
import com.example.classmate.requests.BaseRequest;
import com.example.classmate.requests.DeleteAlbumRequest;
import com.example.classmate.requests.ReadAlbumRequest;
import com.example.classmate.utils.ImageLoader;
import com.example.classmate.utils.WindowAttr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

@WindowAttr(title = "相册")
public class AlbumActivity extends BaseActivity implements OnClickListener {

    private static int REQ_CHOOSE_PHOTO = 11;
    
    @ViewInject(id = R.id.cur_photo)
    private ImageView mCurPhoto;

    @ViewInject(id = R.id.photo0)
    private ImageView mPhoto0;
    
    @ViewInject(id = R.id.photo1)
    private ImageView mPhoto1;

    @ViewInject(id = R.id.photo2)
    private ImageView mPhoto2;

    @ViewInject(id = R.id.photo3)
    private ImageView mPhoto3;

    @ViewInject(id = R.id.photo4)
    private ImageView mPhoto4;
    
    private ImageView[] mPreviews;
    
    private JSONArray mPhotoInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ViewAnnotation.bind(this, this);
        mPreviews = new ImageView[5];
        mPreviews[0] = mPhoto0;
        mPreviews[1] = mPhoto1;
        mPreviews[2] = mPhoto2;
        mPreviews[3] = mPhoto3;
        mPreviews[4] = mPhoto4;
        
        setRightImgBtn(R.drawable.icon_delete, this);
        
        asyncLoadAlbum();
    }
    
    private void asyncLoadAlbum() {
        BaseRequest request = new ReadAlbumRequest(this);
        XHttpCallback callback = new XHttpCallbacks.DefaultHttpCallback() {
            @Override
            public void onSuccess(AHttpResult result) {
                mPhotoInfos = (JSONArray) result.data;
                try {
                    for (int i = 0; i<5; i++) {
                        String url = null;
                        if (i < mPhotoInfos.length()) {
                            url = Conf.IMAGE_ROOT + mPhotoInfos.getJSONObject(i).getString("photoUrl");
                            mPreviews[i].setTag(url);
                            ImageLoader.loadImage(AlbumActivity.this, mPreviews[i]);
                            mPreviews[i].setBackgroundDrawable(null);
                        } else {
                            mPreviews[i].setTag(null);
                            mPreviews[i].setImageResource(android.R.drawable.ic_input_add);
                            mPreviews[i].setBackgroundResource(R.drawable.photo_frame);
                        }
                        mPreviews[0].setOnClickListener(AlbumActivity.this);
                    }
                    if (mPhotoInfos.length() > 0)
                        onClick(mPreviews[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        
        XHttpClient.sendRequest(request, callback);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_img_btn) {
            mCurPhoto.setImageDrawable(null);
            String delUrl = (String) mCurPhoto.getTag();
            for (int i=0; i<mPhotoInfos.length(); i++) {
                JSONObject jo = mPhotoInfos.optJSONObject(i);
                if (jo == null)
                    continue;
                String url = Conf.IMAGE_ROOT + jo.optString("photoUrl");
                if (url.equals(delUrl)) {
                    int photoId = jo.optInt("photoId");
                    DeleteAlbumRequest request = new DeleteAlbumRequest(this, photoId);
                    XHttpClient.sendRequest(request, new XHttpCallbacks.DefaultHttpCallback() {
                        @Override
                        public void onSuccess(AHttpResult result) {
                            asyncLoadAlbum();
                        }
                    });
                }
            }
        } else {
            String url = (String) v.getTag();
            if (url != null) {
                v.setBackgroundDrawable(null);
                mCurPhoto.setTag(url);
                ImageLoader.loadImage(this, mCurPhoto);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQ_CHOOSE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQ_CHOOSE_PHOTO) && (resultCode == RESULT_OK)) {
            String imgPath = Utils.uri2Path(this, data.getData());
            try {
                AddAlbumRequest request = new AddAlbumRequest(this, imgPath);
                XHttpClient.sendRequest(request, new XHttpCallbacks.DefaultHttpCallback() {
                    @Override
                    public void onSuccess(AHttpResult result) {
                        asyncLoadAlbum();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
