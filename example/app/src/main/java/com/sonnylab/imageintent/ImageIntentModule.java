package com.sonnylab.imageintent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    @ReactMethod
    public void getImageIntentBase64(Promise promise) {
        if (getCurrentActivity() != null) {
            Intent intent = getCurrentActivity().getIntent();
            if (intent != null) {
                String action = intent.getAction();
                String type = intent.getType();

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if (type.startsWith("image/")) {
                        encodedImage = handleSendImage(intent); // Handle single image being sent
                    }
                }
            }
        }

        if (encodedImage != null) {
            promise.resolve(encodedImage);
        } else {
            promise.reject("IMAGE_NOT_FOUND");
        }
    }

    @Nullable
    private String handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            try {
                final InputStream imageStream = getCurrentActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = encodeImage(selectedImage);

                return encodedImage;
            } catch(IOException e) {
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "ImageIntent";
    }
}