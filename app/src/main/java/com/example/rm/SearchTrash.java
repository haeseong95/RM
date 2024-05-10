package com.example.rm;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrash extends AppCompatActivity {

    ListView listView;
    ImageView imageView;
    ArrayList<TrashMainListData> arrayList = new ArrayList<>();  // 리스트뷰에 넣을 데이터
    TrashAdapter2 trashAdapter2;  // 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtrash);
        listView = findViewById(R.id.search_listview);
        imageView = findViewById(R.id.backButton);
        imageView.setOnClickListener(v -> finish());

        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);   // 액션바 변경
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 툴바 프로젝트 이름 제거



        // 어댑터 초기화는 하지만 , 비어 있는 리스트로 시작
        // retrofit으로 받은 값을 arrayList가 결과 받음
        trashAdapter2 = new TrashAdapter2(SearchTrash.this, arrayList);
        //listView.setAdapter(trashAdapter2); // 원래 getTrashData()안에 선언했는데 가져오기 전에 어댑터를 추가하는 거 아닌 것 가은데
        listView.setAdapter(trashAdapter2);

        listView.setClickable(true);

        getTrashData();     // 서버에서 쓰레기 데이터 가져옴 (리스트 출력은 안함)


        /*
        arrayList는 retrofit get으로 받아온 값을 받는 그릇, 이걸 listview로 굳이 출력X
        이 arraylist는 사용자가 입력을 했을 ㅒㄷ onQueryTextchagne에서 출력값만 listview로 출력하는 형식으로 가는 게 나을 듯
         */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);    // 항상 확장된 상태O
        searchView.requestFocus();  // searchview에 포커스 요청+키보드 자동O

        int screenWidth = getResources().getDisplayMetrics().widthPixels;   // 검색창-뒤로가기 버튼이 겹치지 않게 가로 길이 조절해줌
        searchView.setMaxWidth(screenWidth - 50);

        searchView.post(() ->{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);  //show_inplicit 하던가 아님 아예 0으로 처리
            Log.i("searchview 실행", "검색창 자동 실행 성공");
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {        // 검색창에 일어나는 이벤트 구현
            @Override
            public boolean onQueryTextSubmit(String str) {    // 키보드의 검색 버튼을 눌렀을 때 이벤트(=검색 업무 처리), 입력한 값을 str로 받아서 처리

                if(str.equals("")) {    // 입력값X -> 값이 없다는 알람 띄울 거임

                }

                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {    // 검색어 입력할 때마다 실행됨
                if(text.isEmpty()){
                    trashAdapter2.updateListviewItem(new ArrayList<>());
                }else {
                    filterTrash(text);  // 텍스트 입력 시 필터링
                }

                return true;
            }
        });

        return true;
    }


    // 검색창에 입력한 값을 받아와서 검색 결과를 리스트뷰에 출력
    private void filterTrash(String text){

        /*
        // 검색 텍스트가 비어 있으면 리스트를 비우고 업데이트
        if (text.isEmpty()){
            trashAdapter2.updateListviewItem(new ArrayList<>());
            return;
        }
         */

        // 검색 텍스트가 있으면 텍스트값과 비교해서 같은 데이터를 result에 추가
        ArrayList<TrashMainListData> result = new ArrayList<>();
        for(int i=0; i<arrayList.size(); i++) {
            TrashMainListData trashMainListData = arrayList.get(i);

            if(trashMainListData.getMainName().toLowerCase().contains(text.toLowerCase())) {    // 입력한 텍스트가 포함되어 있는지 확인 (대소문자 구분X)
                result.add(trashMainListData);
            }
        }

        trashAdapter2.updateListviewItem(result);   // listview 업데이트 (초기화 문제인듯)
    }


    // 리스트뷰에 출력할 데이터 서버에서 가져와 arrayList에 저장 (검색 전에 이미 만들어져 있어야 하고, 데이터를 가져오기만 하므로 화면에 출력할 필요는 없을 듯)
    private void getTrashData() {
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
                        url = user.getUrl();
                        arrayList.add(new TrashMainListData(url, title));
                        Log.i("SearchTrash.listview 성공O", "title : " + arrayList.get(i).getMainName() + ", body : "+ url);
                    }
                }
                else {Log.e("SearchTrash.listview 출력X", "");}
            }
            @Override
            public void onFailure(Call<List<RetroUser>> call, Throwable t) {Log.e("SearchTrash 네트워크 오류", "");}
        });
    }

    public void clickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Intent intent = new Intent(SearchTrash.this, TrashDetail.class);
                intent.putExtra("trashName", arrayList.get(position).);
                intent.putExtra("trashImage", Integer.toString(arrayList.get(position).trashImage));
                intent.putExtra("trashInfo", arrayList.get(position).trashInfo);
                startActivity(intent);

                 */
            }
        });
    }


}