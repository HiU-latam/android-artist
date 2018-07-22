package com.hiulatam.hiu.hiuartist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
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
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.an.customfontview.CustomTextView;
import com.hiulatam.hiu.hiuartist.common.Config;
import com.hiulatam.hiu.hiuartist.customclass.AutoFitTextureView;
import com.hiulatam.hiu.hiuartist.dialog.VideoSaveConfirmationDialogFragment;
import com.hiulatam.hiu.hiuartist.modal.CharityItemModal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by:  Shiny Solutions
 * Created on:  11/10/17.
 */

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity - ";

    public static final String CAMERA_PREF = "camera_pref";
    public static final String ALLOW_KEY = "ALLOWED";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final int REQUEST_TAKE_VIDEO_GALLERY = 2;
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final String FRAGMENT_DIALOG = "dialog";

    private Toolbar toolbar;
    private ImageView imageViewVideoCapture, imageViewFrontCamera, image_button_settings;
    private AutoFitTextureView imageViewVideo;
    private CustomTextView customTextViewName, customTextViewMessage;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;
    private CameraCaptureSession mPreviewSession;
    private CaptureRequest.Builder mPreviewBuilder;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private String mVideoFilePath;
    private boolean mIsRecordingVideo;
    private String fileManagerString;
    private String selectedImagePath;

    private CharityItemModal charityItemModal;

    private Boolean FRONT_FACING_CAMERA = false;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        bindComponents();
        init();
        addListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:{
                for (int i = 0, len = permissions.length; i < len; i++){
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale){
                            showAlert();
                        } else if (!showRationale){
                            saveToPreferences(this, ALLOW_KEY, true);
                        }
                    }
                }
            }
            case REQUEST_VIDEO_PERMISSIONS:{
                if (grantResults.length == VIDEO_PERMISSIONS.length) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ErrorDialog.newInstance(getString(R.string.permission_request))
                                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
                            break;
                        }
                    }
                } else {
                    ErrorDialog.newInstance(getString(R.string.permission_request))
                            .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
                }
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        startBackgroundThread();
        if (imageViewVideo.isAvailable()){
            openCamera(imageViewVideo.getWidth(), imageViewVideo.getHeight());
        } else {
            imageViewVideo.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_TAKE_VIDEO_GALLERY){
                Uri selectedImageUri = data.getData();

                fileManagerString = selectedImageUri.getPath();

                selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null){
                    Toast.makeText(this, "Selected Video: " + selectedImagePath, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void bindComponents(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        imageViewVideoCapture = (ImageView) findViewById(R.id.imageViewCaptureVideo);

        imageViewVideo = (AutoFitTextureView) findViewById(R.id.imageViewVideo);

        customTextViewName = (CustomTextView) findViewById(R.id.customTextViewName);

        imageViewFrontCamera = (ImageView) findViewById(R.id.imageViewFrontCamera);

        customTextViewMessage = (CustomTextView) findViewById(R.id.customTextViewMessage);

        image_button_settings = (ImageView) findViewById(R.id.image_button_settings);
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void init(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        checkPermission();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            if (bundle.containsKey(Config.KEY_CHARITY_ITEM_MODAL)){
                charityItemModal = (CharityItemModal) bundle.getParcelable(Config.KEY_CHARITY_ITEM_MODAL);
            }
        }

        if (charityItemModal != null){
            customTextViewName.setText(String.format("FOR %S", charityItemModal.getName()));
            customTextViewMessage.setText(getString(R.string.dummy_message));
        }
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  11/10/17.
     */
    private void addListeners(){
        imageViewVideoCapture.setOnClickListener(onClickListener);
        imageViewFrontCamera.setOnClickListener(onClickListener);
        image_button_settings.setOnClickListener(onClickListener);
    }

    private void openVideoConfirmationDialog(){

        VideoSaveConfirmationDialogFragment videoSaveConfirmationDialogFragment = new VideoSaveConfirmationDialogFragment();
        videoSaveConfirmationDialogFragment.getFilePath(mVideoFilePath);
        videoSaveConfirmationDialogFragment.show(getSupportFragmentManager(), "VideoSaveConfirmationDialogFragment");

        mVideoFilePath = null;
    }

    /**
     * Created by:  Shiny Solutions
     * Created on:  12/20/17
     * @return
     */
    private boolean checkCameraHardware() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (getFromPref(this, ALLOW_KEY)){
                showSettingsAlert();
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    showAlert();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {

        }
    }

    public static Boolean getFromPref(Context context, String key){
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showSettingsAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        });
        alertDialog.show();
    }

    public static void saveToPreferences(Context context, String key, boolean allowed){
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    protected void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void startPreview(){
        Config.LogInfo(TAG + "startPreview");
        if (null == mCameraDevice || !imageViewVideo.isAvailable() || null == mPreviewSize){
            return;
        }
        try{
            closePreviewSession();
            SurfaceTexture texture = imageViewVideo.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Config.LogInfo(TAG + "startPreview - onConfigureFailed");
                    Toast.makeText(VideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height){
        if (!hasPermissionGranted(VIDEO_PERMISSIONS)){
            requestVideoPermissions();
            return;
        }
        if (isFinishing()){
            Toast.makeText(this, "Is finishing.", Toast.LENGTH_LONG).show();
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }

            Config.LogInfo(TAG + "openCamera - front facing camera: " + FRONT_FACING_CAMERA);
            String cameraId = null;
            CameraCharacteristics characteristics = null;
            if (FRONT_FACING_CAMERA){
                for(int i = 0; i < manager.getCameraIdList().length; i++){
                    cameraId = manager.getCameraIdList()[i];
                    Config.LogInfo(TAG + "openCamera - camera ID: " + cameraId);
                    characteristics = manager.getCameraCharacteristics(cameraId);
                    if (characteristics != null & characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT){
                        Config.LogInfo(TAG + "openCamera - found front facing lance ");

                        characteristics = manager.getCameraCharacteristics(cameraId);
                    }
                }
            }else {
                cameraId = manager.getCameraIdList()[0];
                characteristics = manager.getCameraCharacteristics(cameraId);
            }

            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (map == null) {
                throw new RuntimeException("Cannot get available preview/video sizes");
            }
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, mVideoSize);
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageViewVideo.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            } else {
                imageViewVideo.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            manager.openCamera(cameraId, mStateCallback, null);
        }catch (CameraAccessException e){
            Toast.makeText(this, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            finish();
        }catch (NullPointerException e){
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        }catch (InterruptedException e){
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private boolean hasPermissionGranted(String[] permissions){
        for (String permission: permissions){
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }

        }
        return true;
    }

    private void closePreviewSession(){
        if (mPreviewSession != null){
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void updatePreview(){
        Config.LogInfo(TAG + "updatePreview");
        if (null == mCameraDevice){
            return;
        }
        try{
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder){
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    private void configureTransform(int viewWidth, int viewHeight){
        if (null == imageViewVideo || null == mPreviewSize){
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation){
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max((float) viewHeight / mPreviewSize.getHeight(), (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);

        }
        imageViewVideo.setTransform(matrix);
    }

    private void requestVideoPermissions(){
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)){
            new ConfirmationDialog().show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions){
        for (String permission : permissions){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                return true;
            }
        }
        return false;
    }

    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    private void startRecordingVideo() {
        if (null == mCameraDevice || !imageViewVideo.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = imageViewVideo.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI
                            mIsRecordingVideo = true;

                            // Start recording
                            mMediaRecorder.start();
                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Config.LogInfo(TAG + "startRecordingVideo - onConfigureFailed");
                    Toast.makeText(VideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }

    private void setUpMediaRecorder() throws IOException {
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath(this);
        }
        mMediaRecorder.setOutputFile(new File(getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath() + "/" + System.currentTimeMillis() + ".aac");
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }

    private String getVideoFilePath(Context context) {
        //final File dir = context.getExternalFilesDir(null);
        //final File dir = context.getCacheDir();
        final File dir = Environment.getExternalStorageDirectory();
        String filePath = (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
        mVideoFilePath = filePath;
        Config.LogInfo(TAG + "getVideoFilePath - filePath: " + filePath);
        return filePath;
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    private void stopRecordingVideo() {
        // UI
        mIsRecordingVideo = false;
        // Stop recording
        mMediaRecorder.stop();
        mMediaRecorder.reset();

        Toast.makeText(this, "Video saved: " + mNextVideoAbsolutePath,
                Toast.LENGTH_SHORT).show();
        mNextVideoAbsolutePath = null;
        startPreview();
        openVideoConfirmationDialog();
    }

    private void openVideoGallery(){
        Config.LogInfo(TAG + "openVideoGallery");

        Intent intentVideoGallery = new Intent();
        intentVideoGallery.setType("video/*");
        intentVideoGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentVideoGallery, "Select Video"), REQUEST_TAKE_VIDEO_GALLERY);
    }

    private String getPath(Uri uri){
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else
            return null;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Config.LogInfo(TAG + "onClick");
            switch (view.getId()){
                case R.id.imageViewCaptureVideo:
                    if (mIsRecordingVideo) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                stopRecordingVideo();
                            }
                        }, 1000);


                        imageViewVideoCapture.setImageResource(R.drawable.capture_shade);
                    } else {
                        startRecordingVideo();
                        imageViewVideoCapture.setImageResource(R.drawable.record_capture_shade);
                    }

                    break;
                case R.id.imageViewFrontCamera:
                    if (FRONT_FACING_CAMERA)
                        FRONT_FACING_CAMERA = false;
                    else
                        FRONT_FACING_CAMERA = true;

                    closeCamera();
                    stopBackgroundThread();
                    startBackgroundThread();
                    if (imageViewVideo.isAvailable()){
                        openCamera(imageViewVideo.getWidth(), imageViewVideo.getHeight());
                    } else {
                        imageViewVideo.setSurfaceTextureListener(mSurfaceTextureListener);
                    }
                    break;

                case R.id.image_button_settings:
                    openVideoGallery();
                    break;
            }
        }
    };

    TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != imageViewVideo){
                configureTransform(imageViewVideo.getWidth(), imageViewVideo.getHeight());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            Toast.makeText(VideoActivity.this, "Is finishing on error while state callback.", Toast.LENGTH_LONG).show();
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            /*closeCamera();
            stopBackgroundThread();*/
            finish();
        }
    };

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "Is finishing on error dialog", Toast.LENGTH_LONG).show();
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.permission_request)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), VIDEO_PERMISSIONS,
                                    REQUEST_VIDEO_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(), "Is finishing on confirm dialog.", Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                }
                            })
                    .create();
        }

    }
}
