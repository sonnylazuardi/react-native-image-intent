package com.sonnylab.imageintent;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageIntentModule extends ReactContextBaseJavaModule {
    private ReactContext mReactContext;
    private String encodedImage;
    private String imageUrl;

    public ImageIntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,70,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private String getURIPath(Uri uriValue)
    {
        try {
            String[] mediaStoreProjection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getCurrentActivity().getContentResolver().query(uriValue, mediaStoreProjection, null, null, null);
            if (cursor != null) {
                int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String colIndexString = cursor.getString(colIndex);
                cursor.close();
                return colIndexString;
            }
        } catch(Exception e) {

        }
        return null;
    }

    @ReactMethod
    public void getImageIntentBase64(Promise promise) {
        encodedImage = null;

        checkIntent();

        if (encodedImage != null) {
            promise.resolve(encodedImage);
        } else {
            promise.reject("IMAGE_NOT_FOUND");
        }
    }

    @ReactMethod
    public void getImageIntentUrl(Promise promise) {
        imageUrl = null;

        checkIntent();

        if (imageUrl != null) {
            promise.resolve(imageUrl);
        } else {
            promise.reject("IMAGE_NOT_FOUND");
        }
    }

    private void checkIntent() {
        if (getCurrentActivity() != null) {
            Intent intent = getCurrentActivity().getIntent();
            if (intent != null) {
                String action = intent.getAction();
                String type = intent.getType();

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if (type.startsWith("image/")) {
                        handleSendImage(intent); // Handle single image being sent
                    }
                }
            }
        }
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try {
                final InputStream imageStream = getCurrentActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                encodedImage = encodeImage(selectedImage);
                imageUrl = getURIPath(imageUri);
            } catch(IOException e) {
            }
        }
    }

    @Override
    public String getName() {
        return "ImageIntent";
    }
}
