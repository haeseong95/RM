package com.example.rm;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private Interpreter tflite;
    private Uri photoURI;

    Button btn_capture;
    ImageView imageview;
    TextView classificationResult;
    private static final String TAG = "CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        btn_capture = findViewById(R.id.capture_btn);
        imageview = findViewById(R.id.result_image);
        classificationResult = findViewById(R.id.classification_result);

        Bitmap bitmap = getIntent().getParcelableExtra("picture");
        if (bitmap != null) {
            imageview.setImageBitmap(bitmap);
        }

        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("권한을 허용하지 않으면 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

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
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    Log.e(TAG, "Error creating image file", e);
                }
                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(this, "com.example.rm.fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else {
                Log.e(TAG, "No camera app available");
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoURI));
                if (photo != null) {
                    photo = rotateImageIfRequired(photo, photoURI);
                    imageview.setImageBitmap(photo);
                    runModelInference(photo);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error loading image", e);
            }
        } else {
            Log.d(TAG, "Cancelled or no data");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        ExifInterface ei = new ExifInterface(getContentResolver().openInputStream(selectedImage));
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private ByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("best_float32.tflite");
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

        int inputSize = 320; // YOLOv8 모델의 입력 크기 가정
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);

        ByteBuffer inputBuffer = convertBitmapToByteBuffer(resizedBitmap);
        float[][][] output = new float[1][10][2100]; // YOLO 모델의 출력 형식 (조정 필요)

        try {
            tflite.run(inputBuffer, output);
            Log.d(TAG, "Inference result: " + output[0][0][4]); // 예시 출력 로그
            drawResult(bitmap, output); // 원본 이미지를 사용하여 결과를 그림
            displayClassificationResult(output);
        } catch (Exception e) {
            Log.e(TAG, "Error during inference", e);
        }
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        int inputSize = 320; // YOLOv8 모델의 입력 크기 가정
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3); // 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[inputSize * inputSize];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat((((val >> 16) & 0xFF) - 127.5f) / 127.5f); // R
                byteBuffer.putFloat((((val >> 8) & 0xFF) - 127.5f) / 127.5f);  // G
                byteBuffer.putFloat(((val & 0xFF) - 127.5f) / 127.5f);         // B
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
            if (confidence > 0.2) { // 신뢰도 임계값을 낮춤
                float x = output[0][i][0] * bitmap.getWidth();
                float y = output[0][i][1] * bitmap.getHeight();
                float w = output[0][i][2] * bitmap.getWidth();
                float h = output[0][i][3] * bitmap.getHeight();
                canvas.drawRect(x - w / 2, y - h / 2, x + w / 2, y + h / 2, paint);
            }
        }

        runOnUiThread(() -> imageview.setImageBitmap(mutableBitmap));
    }

    private void displayClassificationResult(float[][][] output) {
        StringBuilder resultBuilder = new StringBuilder();
        String[] labels = {"배터리", "금속", "기타", "종이", "플라스틱", "유리"}; // 모델의 클래스 레이블 (적절히 수정)
        Map<String, Integer> labelCount = new HashMap<>();
        float confidenceThreshold = 0.2f; // 신뢰도 임계값을 더 낮춤

        for (String label : labels) {
            labelCount.put(label, 0);
        }

        Log.d(TAG, "Output length: " + output[0].length);
        for (int i = 0; i < output[0].length; i++) {
            float confidence = output[0][i][4];
            Log.d(TAG, "Object " + i + " confidence: " + confidence);

            if (confidence > confidenceThreshold) { // 신뢰도 임계값 조정
                float maxClassScore = output[0][i][5];
                int classIndex = 0;
                for (int j = 6; j < output[0][i].length; j++) {
                    if (output[0][i][j] > maxClassScore && j - 6 < labels.length) {
                        maxClassScore = output[0][i][j];
                        classIndex = j - 6; // 클래스 인덱스 계산 (조정 필요)
                    }
                }

                if (classIndex < labels.length) {
                    String label = labels[classIndex];
                    labelCount.put(label, labelCount.get(label) + 1);
                }
            }
        }

        // 다수결 방식으로 최종 레이블 결정
        String finalLabel = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : labelCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                finalLabel = entry.getKey();
            }
        }

        if (finalLabel != null) {
            resultBuilder.append("쓰레기 종류 : ").append(finalLabel).append("\n");
        } else {
            resultBuilder.append("객체를 인식하지 못했습니다.\n");
        }

        String resultText = resultBuilder.toString();
        Log.d(TAG, "Classification result: " + resultText);

        // UI 스레드에서 텍스트 설정
        runOnUiThread(() -> {
            if (classificationResult != null) {
                Log.d(TAG, "TextView 업데이트");
                classificationResult.setText(resultText);
            } else {
                Log.e(TAG, "TextView가 null입니다.");
            }
        });
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


