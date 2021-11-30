package com.rightcode.bowelography.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.AudioManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.math.MathUtils;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityCameraBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager_ai;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.Common_1Response;
import com.rightcode.bowelography.network.Response.ai_photoResponse;
import com.rightcode.bowelography.network.model.ai_auto;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;

public class CameraActivity extends BaseActivity<ActivityCameraBinding> {

    File file;
    Float a = 0f;
    Zoom zoom;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private CameraCaptureSession cameraCaptureSession;
    private Size imageDimensions;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession.CaptureCallback captureListener;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CameraDevice mCamera;


    MediaActionSound sound = new MediaActionSound();
    boolean soundOn = false;
    boolean MonoEffect = false;
    private boolean isTorchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_camera);
    }

    @Override
    protected void initActivity() {
        dataBinding.viewCamera.setSurfaceTextureListener(textureListener);
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("ai사진 분석", view -> finishWithAnim(), "RIGHT");
        dataBinding.ivTakeSound.setSelected(true);
        dataBinding.ivTakeFlash.setSelected(true);

    }

    @Override
    protected void initClickListener() {
        dataBinding.zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoom.setZoom(captureRequestBuilder, progress + 1);
                try {
                    cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        dataBinding.viewCamera.setOnClickListener(view -> {
        });
        dataBinding.ivTakePic.setOnClickListener(view -> {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!mgr.isStreamMute(AudioManager.STREAM_MUSIC)) {
                    mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                }
            } else {
                mgr.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
            try {
                takePicture();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        });
        dataBinding.ivTakeBlack.setOnClickListener(view -> {
            if (!dataBinding.ivTakeBlack.isSelected()) {
                MonoEffect = true;
                dataBinding.ivTakeBlack.setSelected(true);
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                MonoEffect = false;
                dataBinding.ivTakeBlack.setSelected(false);
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        dataBinding.ivTakeSound.setOnClickListener(view -> {
            if (!dataBinding.ivTakeSound.isSelected()) {
                dataBinding.ivTakeSound.setSelected(true);
                soundOn = true;
            } else {
                soundOn = false;
                dataBinding.ivTakeSound.setSelected(false);
            }
        });
        dataBinding.ivTakeGuide.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, PhotoGuideActivity.class);
            startActivity(intent);
            finishWithAnim();
        });
        dataBinding.ivTakeFlash.setOnClickListener(view -> {
            if (!dataBinding.ivTakeFlash.isSelected()) {
                isTorchOn = true;
                dataBinding.ivTakeFlash.setSelected(true);

            } else {
                isTorchOn = false;
                dataBinding.ivTakeFlash.setSelected(false);
            }
        });

    }
    public void aiPhoto(File photo, String photoAi) {
        MultipartBody.Part photo_body = aiTransformImageToMultipart(photo);
        callApi(NetworkManager_ai.getInstance(mContext).getApiService().ai_photo(photo_body), new ApiResponseHandler<ai_photoResponse>() {
            @Override
            public void onSuccess(ai_photoResponse result) {
                if (photoAi == null) {
                    Intent intentR = new Intent();
                    intentR.putExtra("blood", result.getBlood());
                    intentR.putExtra("shape", result.getShape());
                    intentR.putExtra("color", result.getColor());
                    intentR.putExtra("guide", photo);//사용자에게 입력받은값 넣기
                    setResult(RESULT_OK, intentR); //결과를 저장
                    hideLoading();
                    finishWithAnim();//액티비티 종료
                } else {
                    Intent intent = new Intent(mContext, ReportActivity.class);
                    intent.putExtra("blood", result.getBlood());
                    intent.putExtra("shape", result.getShape());
                    intent.putExtra("color", result.getColor());
                    intent.putExtra("photo", photo);
                    intent.putExtra("ai", true);
                    startActivity(intent);
                    hideLoading();
                    finishWithAnim();
                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(ai_photoResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

    // 리스너 콜백 함수
    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        cameraId = manager.getCameraIdList()[0];
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
        zoom = new Zoom(characteristics);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            manager.openCamera(cameraId, stateCallback, null);
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
        String photo_ai = getIntent().getStringExtra("photo");
        aiPhoto(file, photo_ai);
    }

    private void takePicture() throws CameraAccessException {
        if (mCamera == null)
            return;

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCamera.getId());
        Size[] jpegSizes ;

        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

        int width = 300;
        int height = 640;

        if (jpegSizes != null && jpegSizes.length > 0) {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(dataBinding.viewCamera.getSurfaceTexture()));
        final CaptureRequest.Builder captureBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        if (!dataBinding.ivTakeSound.isSelected()) {
            sound.play(MediaActionSound.SHUTTER_CLICK);
        }
        if (isTorchOn) {
            captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
        } else {
            captureBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        }
        showLoading();

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

        captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
            }
        };

        mCamera.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

            }
        }, mBackgroundHandler);

        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        file = new File(directory, ts + ".png");

        ImageReader.OnImageAvailableListener readerListener = reader1 -> {
            Image image ;

            image = reader1.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            try {
                save(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (image != null) {
                    image.close();
                }
            }
        };
        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

    }

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = dataBinding.viewCamera.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);

        captureRequestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);


        captureRequestBuilder.addTarget(surface);

        mCamera.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                if (mCamera == null) {
                    return;
                }

                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Configuration Changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if (mCamera == null) {
            return;
        }
        if (MonoEffect) {
            captureRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CameraMetadata.CONTROL_EFFECT_MODE_MONO);
        } else {
            captureRequestBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CameraMetadata.CONTROL_EFFECT_MODE_OFF);
        }
        zoomSet(a);


        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);

    }

    public void zoomSet(float a) {
        zoom.setZoom(captureRequestBuilder, a);
    }

    public void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;
    }

    public final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCamera = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCamera.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            mCamera.close();
            mCamera = null;
        }
    };

    public static class Zoom {
        private static final float DEFAULT_ZOOM_FACTOR = 1.0f;

        @NonNull
        private final Rect mCropRegion = new Rect();

        public final float maxZoom;

        @Nullable
        private final Rect mSensorSize;

        public final boolean hasSupport;

        public Zoom(@NonNull final CameraCharacteristics characteristics) {


            this.mSensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

            if (this.mSensorSize == null) {
                this.maxZoom = Zoom.DEFAULT_ZOOM_FACTOR;
                this.hasSupport = false;
                return;
            }

            final Float value = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);

            this.maxZoom = ((value == null) || (value < Zoom.DEFAULT_ZOOM_FACTOR))
                    ? Zoom.DEFAULT_ZOOM_FACTOR
                    : value;

            this.hasSupport = (Float.compare(this.maxZoom, Zoom.DEFAULT_ZOOM_FACTOR) > 0);
        }

        public void setZoom(@NonNull final CaptureRequest.Builder builder, final float zoom) {
            if (!this.hasSupport) {
                return;
            }

            final float newZoom = MathUtils.clamp(zoom, Zoom.DEFAULT_ZOOM_FACTOR, this.maxZoom);

            final int centerX = this.mSensorSize.width() / 2;
            final int centerY = this.mSensorSize.height() / 2;
            final int deltaX = (int) ((0.5f * this.mSensorSize.width()) / newZoom);
            final int deltaY = (int) ((0.5f * this.mSensorSize.height()) / newZoom);

            this.mCropRegion.set(centerX - deltaX,
                    centerY - deltaY,
                    centerX + deltaX,
                    centerY + deltaY);

            builder.set(CaptureRequest.SCALER_CROP_REGION, this.mCropRegion);
        }
    }
}
