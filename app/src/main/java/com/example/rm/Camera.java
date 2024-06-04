/*
package com.example.rm;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.List;

public class Camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int RESULT_CODE = 22;
    private Interpreter tflite;

    Button btn_capture;
    ImageView imageview;
    private static final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        btn_capture = findViewById(R.id.capture_btn);
        imageview = findViewById(R.id.result_image);

        Bitmap bitmap = getIntent().getParcelableExtra("picture");
        if (bitmap != null) {
            imageview.setImageBitmap(bitmap);
        }

        // 권한 체크
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("권한을 허용하지 않으면 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        // TensorFlow Lite 모델 로드
        try {
            tflite = new Interpreter(loadModelFile());
            Log.d(TAG, "Model loaded successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error loading model", e);
        }

        btn_capture.setOnClickListener(v -> {
            Log.d(TAG, "Capture button clicked");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, RESULT_CODE);
            } else {
                Log.e(TAG, "No camera app available");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Image capture successful");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                imageview.setImageBitmap(photo);
                runModelInference(photo);
            }
        } else {
            Log.d(TAG, "Cancelled or no data");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("yolov8n.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void runModelInference(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "tflite is not initialized");
            return;
        }

        ByteBuffer inputBuffer = convertBitmapToByteBuffer(bitmap);
        float[][][] output = new float[1][25200][7]; // Assuming YOLOv8 output shape

        try {
            tflite.run(inputBuffer, output);
            Log.d(TAG, "Inference result: " + output[0][0][4]); // Example output log
            drawResult(bitmap, output);
        } catch (Exception e) {
            Log.e(TAG, "Error during inference", e);
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int inputSize = 640; // Assuming YOLOv8 input size
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        int[] intValues = new int[inputSize * inputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val >> 8) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val) & 0xFF) - 127.5f) / 127.5f);
            }
        }
        byteBuffer.rewind();
        return byteBuffer;
    }

    private void drawResult(Bitmap bitmap, float[][][] output) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4];
            if (confidence > 0.5) { // Assuming confidence threshold
                float x = output[0][i][0] * bitmap.getWidth();
                float y = output[0][i][1] * bitmap.getHeight();
                float w = output[0][i][2] * bitmap.getWidth();
                float h = output[0][i][3] * bitmap.getHeight();
                canvas.drawRect(x - w / 2, y - h / 2, x + w / 2, y + h / 2, paint);
            }
        }

        runOnUiThread(() -> imageview.setImageBitmap(mutableBitmap));
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}






*/

package com.example.rm;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.List;

public class Camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int RESULT_CODE = 22;
    private Interpreter tflite;

    Button btn_capture;
    ImageView imageview;
    private static final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        btn_capture = findViewById(R.id.capture_btn);
        imageview = findViewById(R.id.result_image);

        Bitmap bitmap = getIntent().getParcelableExtra("picture");
        if (bitmap != null) {
            imageview.setImageBitmap(bitmap);
        }

        // 권한 체크
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("권한을 허용하지 않으면 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        // TensorFlow Lite 모델 로드
        try {
            tflite = new Interpreter(loadModelFile());
            Log.d(TAG, "Model loaded successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error loading model", e);
        }

        btn_capture.setOnClickListener(v -> {
            Log.d(TAG, "Capture button clicked");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.e(TAG, "No camera app available");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Image capture successful");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                imageview.setImageBitmap(photo);
                runModelInference(photo);
            }
        } else {
            Log.d(TAG, "Cancelled or no data");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("yolov8n_float32.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void runModelInference(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "tflite is not initialized");
            return;
        }

        ByteBuffer inputBuffer = convertBitmapToByteBuffer(bitmap);
        float[][][] output = new float[1][25200][7]; // YOLOv8 모델의 출력 형태 가정

        try {
            tflite.run(inputBuffer, output);
            Log.d(TAG, "Inference result: " + output[0][0][4]); // 예시 출력 로그
            drawResult(bitmap, output);
        } catch (Exception e) {
            Log.e(TAG, "Error during inference", e);
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int inputSize = 640; // YOLOv8 모델의 입력 크기 가정
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        int[] intValues = new int[inputSize * inputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val >> 8) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val) & 0xFF) - 127.5f) / 127.5f);
            }
        }
        byteBuffer.rewind();
        return byteBuffer;
    }

    private void drawResult(Bitmap bitmap, float[][][] output) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4];
            if (confidence > 0.5) { // 신뢰도 임계값 가정
                float x = output[0][i][0] * bitmap.getWidth();
                float y = output[0][i][1] * bitmap.getHeight();
                float w = output[0][i][2] * bitmap.getWidth();
                float h = output[0][i][3] * bitmap.getHeight();
                canvas.drawRect(x - w / 2, y - h / 2, x + w / 2, y + h / 2, paint);
            }
        }

        runOnUiThread(() -> imageview.setImageBitmap(mutableBitmap));
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}

/*
package com.example.rm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.List;

public class Camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int RESULT_CODE = 22;
    private Interpreter tflite;

    Button btn_capture;
    ImageView imageview;
    TextView resultTextView;
    private static final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        btn_capture = findViewById(R.id.capture_btn);
        imageview = findViewById(R.id.result_image);
        resultTextView = findViewById(R.id.resultTextView);

        Bitmap bitmap = getIntent().getParcelableExtra("picture");
        if (bitmap != null) {
            imageview.setImageBitmap(bitmap);
        }

        // 권한 체크
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("권한을 허용하지 않으면 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        // TensorFlow Lite 모델 로드
        try {
            tflite = new Interpreter(loadModelFile());
            Log.d(TAG, "Model loaded successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error loading model", e);
        }

        btn_capture.setOnClickListener(v -> {
            Log.d(TAG, "Capture button clicked");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.e(TAG, "No camera app available");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "Image capture successful");
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                imageview.setImageBitmap(photo);
                runModelInference(photo);
            }
        } else {
            Log.d(TAG, "Cancelled or no data");
            Toast.makeText(this, "사진촬영이 취소되었거나 적합한 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void runModelInference(Bitmap bitmap) {
        if (tflite == null) {
            Log.e(TAG, "tflite is not initialized");
            return;
        }

        ByteBuffer inputBuffer = convertBitmapToByteBuffer(bitmap);
        float[][][] output = new float[1][25200][7]; // YOLOv8 모델의 출력 형태 가정

        try {
            tflite.run(inputBuffer, output);
            logAndDisplayInferenceResult(output);
            drawResult(bitmap, output);
        } catch (Exception e) {
            Log.e(TAG, "Error during inference", e);
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int inputSize = 320; // YOLOv8 모델의 입력 크기 가정
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);
        int[] intValues = new int[inputSize * inputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val >> 8) & 0xFF) - 127.5f) / 127.5f);
                byteBuffer.putFloat((((val) & 0xFF) - 127.5f) / 127.5f);
            }
        }
        byteBuffer.rewind();
        return byteBuffer;
    }

    private void logAndDisplayInferenceResult(float[][][] output) {
        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4];
            if (confidence > 0.5) { // 신뢰도 임계값 가정
                float x = output[0][i][0];
                float y = output[0][i][1];
                float w = output[0][i][2];
                float h = output[0][i][3];
                @SuppressLint("DefaultLocale") String result = String.format("Object %d: [x=%.2f, y=%.2f, w=%.2f, h=%.2f, confidence=%.2f]", i, x, y, w, h, confidence);
                Log.d(TAG, result);
                resultBuilder.append(result).append("\n");
            }
        }

        final String resultText = resultBuilder.toString();
        runOnUiThread(() -> resultTextView.setText(resultText));
    }

    private void drawResult(Bitmap bitmap, float[][][] output) {
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(20);

        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4];
            if (confidence > 0.5) { // 신뢰도 임계값 가정
                float x = output[0][i][0] * bitmap.getWidth();
                float y = output[0][i][1] * bitmap.getHeight();
                float w = output[0][i][2] * bitmap.getWidth();
                float h = output[0][i][3] * bitmap.getHeight();
                canvas.drawRect(x - w / 2, y - h / 2, x + w / 2, y + h / 2, paint);
                canvas.drawText(String.format("%.2f", confidence), x - w / 2, y - h / 2 - 10, textPaint);
            }
        }

        runOnUiThread(() -> imageview.setImageBitmap(mutableBitmap));
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}
*/