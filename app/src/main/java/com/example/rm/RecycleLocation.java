package com.example.rm;

<<<<<<< HEAD
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
=======
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
>>>>>>> 458f2aa9835baff6179ff2a02a16123d0b4e9c1b
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

<<<<<<< HEAD
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RecycleLocation extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000; // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // OnRequestPermissionsResultCallback에서 수신된 결과에서 ActivityCompat.OnRequestPermissionsResultCallback를 사용한 퍼미션 요청을 구별하기 위함
    private static final int PERMISSION_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션 정의
    String[] REQUIRED_PERMISSION = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };

    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest; // 주의
    private Location location;

    private View mLayout; // snackbar 사용하기 위함.
=======
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleLocation extends AppCompatActivity {
>>>>>>> 458f2aa9835baff6179ff2a02a16123d0b4e9c1b


    // 테스트
    ListView listView;
    ArrayList<TrashMainListData> arrayList = new ArrayList<>();
    TrashAdapter2 trashAdapter2;

    // 이미지
    private final int GALLERY = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

<<<<<<< HEAD
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
=======
        btnBack = (ImageView)findViewById(R.id.btn_back);
        listView = findViewById(R.id.testlist);
>>>>>>> 458f2aa9835baff6179ff2a02a16123d0b4e9c1b

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        trashAdapter2 = new TrashAdapter2(this, arrayList);
        test();
        listView.setAdapter(trashAdapter2);

    }

<<<<<<< HEAD
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: 들어옴 ");

        mMap = googleMap;

        // 지도의 초기위치 이동
        setDefaultLocation();

        // 런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 확인합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED){
            // 2. 이미 퍼미션을 가지고 있다면
            startLocationUpdates(); // 3. 위치 업데이트 실행
        }else{
            // 2. 퍼미션 요청을 허용한 적 없다면 퍼미션 요청하기
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSION[0])){
                // 요청 진행하기 전에 퍼미션이 왜필요한지 설명
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // 사용자에게 퍼미션 요청, 요청 결과는 onRequestPermisionResult에서 수신
                        ActivityCompat.requestPermissions(RecycleLocation.this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
                    }
                }).show();
            }else{
                // 사용자가 퍼미션 거부를 한적이 없는 경우 퍼미션 요청을 바로 함.
                // 요청 결과는 onRequestPermissionResult에서 수신된다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, PERMISSION_REQUEST_CODE);
            }
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d(TAG, "onMapClick: ");
            }
        });
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if(locationList.size() > 0){
                location = locationList.get(locationList.size() -1);

                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도 :" + String.valueOf(location.getLatitude()) + "경도 :" +
                        String.valueOf(location.getLongitude());

                // 현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocation = location;
            }
        }
    };


    private String getCurrentAddress(LatLng currentPosition) {
        // 지오코더 gps를 주소로 변환

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(
                    currentPosition.latitude,
                    currentPosition.longitude,
                    1
            );
        }catch (IOException ioException){
            // 네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return  "지오코더 서비스 사용 불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this,"잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            Toast.makeText(this,"주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }else{
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    private void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        if(currentMarker != null)
        {
            currentMarker.remove();
        }

        LatLng currentLatLng =  new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
    }



    private void startLocationUpdates() {
        if(!checkLocationServicesStatus()){
            showDiologForLocationServiceSetting();

        }else{
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ){

                Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if(checkPermission()){
                mMap.setMyLocationEnabled(true);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");

        if(checkPermission()){
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if(mMap!=null){
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mFusedLocationClient != null){
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private boolean checkPermission(){

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED|| hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED ){

            Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
            return false;

        }else{
            return true;
        }

    }

    private void showDiologForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RecycleLocation.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다. 위치설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setDefaultLocation() {

        // 기본 위치
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치 정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부를 확인하세요";

        if(currentMarker != null){
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }
=======
    // 이미지
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // 이미지 업로드
    private void upLoadImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());

    }


    private void test(){
        Call<List<RetroUser>> call = RetroClient.getRetroService().getImage();
        call.enqueue(new Callback<List<RetroUser>>() {
            @Override
            public void onResponse(Call<List<RetroUser>> call, Response<List<RetroUser>> response) {
                if (response.isSuccessful()) {

                    int id = 0;
                    String title, url = null;
                    List<RetroUser> retroUser = response.body();

                    for (int i=0; i<7; i++){
                        RetroUser user = retroUser.get(i);

                        title = user.getTitle();
                        id = user.getId();
                        url = user.getUrl();
                        arrayList.add(new TrashMainListData(url, title));  // arrayList에 항목 추가
                        Log.i("category 리스트뷰 전달 성공", "title : " + title + ", body : "+ url);
                    }

                    trashAdapter2.notifyDataSetChanged();    // listview 리스트의 크기+아이템 둘 다 변경될 때 사용 (=리스트 업데이트)
                }
                else {
                    Log.e("category 리스트뷰", "리스트뷰 출력 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {

            }
        });

    }


    /*
    private void setText(){
        Call<RetroApi> call = RetroClient.getRetroService().setCategoryMain();

        call.enqueue(new Callback<RetroApi>() {
            @Override
            public void onResponse(Call<RetroApi> call, Response<RetroApi> response) {
                ArrayList<RetroWriting> retroWritings = response.body().getRetroWriting();

                String name, info, image;

                if(response.isSuccessful()) {
                    for (RetroWriting writing : retroWritings) {
                        name = writing.getTitle();
                        info = writing.getContent();
                        image = writing.getImage();
                        arrayList.add(new TrashMainListData(image, name, info));
                        Log.i("지도, 리스트뷰 테스트", "image : " + image + ", name : " + name + ", info" + info);
                    }

                    trashAdapter2.notifyDataSetChanged();

                    int totalHeight = 0;
                    for (int i = 0; i < trashAdapter2.getCount(); i++){
                        View listItem = trashAdapter2.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight + (listView.getDividerHeight() * (trashAdapter2.getCount() - 1));
                    listView.setLayoutParams(params);
                }else {
                    Log.e("리스트뷰", "리스트뷰 출력 실패");
                }
            }

            @Override
            public void onFailure(Call<RetroApi> call, Throwable t) {
                Log.e("리스트뷰", "예외, 네트워크 오류 등", t);
            }
        });


    }

     */



>>>>>>> 458f2aa9835baff6179ff2a02a16123d0b4e9c1b
}




