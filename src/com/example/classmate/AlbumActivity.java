package com.example.classmate;

import org.json.JSONArray;
import org.json.JSONException;
import org.xframe.annotation.ViewAnnotation.ViewInject;
import org.xframe.http.XHttpCallback;
import org.xframe.http.XHttpCallbacks;
import org.xframe.http.XHttpClient;
import com.example.classmate.common.BaseActivity;
import com.example.classmate.common.Utils;
import com.example.classmate.requests.AddAlbumRequest;
import com.example.classmate.requests.BaseRequest;
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
    
    @ViewInject(id = R.id.photo1)
    private ImageView mPhoto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setRightImgBtn(R.drawable.icon_add, this);
        
        asyncLoadAlbum();
    }
    
    private void asyncLoadAlbum() {
        BaseRequest request = new ReadAlbumRequest(this);
        XHttpCallback callback = new XHttpCallbacks.DebugHttpCallback(this) {
            @Override
            public void onSuccess(AHttpResult result) {
                super.onSuccess(result);
                JSONArray ja = (JSONArray) result.data;
                try {
                    mPhoto1.setTag(ja.getJSONObject(0).getString("photoUrl"));
                    ImageLoader.loadImage(AlbumActivity.this, mPhoto1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        
        XHttpClient.sendRequest(request, callback);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.right_img_btn) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQ_CHOOSE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQ_CHOOSE_PHOTO) && (resultCode == RESULT_OK)) {
            String imgPath = Utils.uri2Path(this, data.getData());
            try {
                AddAlbumRequest request = new AddAlbumRequest(this, imgPath);
                XHttpClient.sendRequest(request,
                        new XHttpCallbacks.DebugHttpCallback(this));
            } catch (Exception e) {
            }
        }
    }
}
