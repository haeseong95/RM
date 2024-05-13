package com.example.rm.category;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.R;
import com.example.rm.retrofit.RetroClient;
import com.example.rm.retrofit.RetroWriting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTrash extends AppCompatActivity {
    ListView listView;
    ArrayList<TrashListData> arrayList = new ArrayList<>();  // retrofit GET으로 결과를 저장할 그릇, 이걸 listview 출력X, arraylist는 사용자가 입력 시 onQueryTextchagne에서 출력값만 listview로 출력하는 형식으로 가는 게 나을 듯
    TrashListAdapter trashListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchtrash);
        listView = findViewById(R.id.search_listview);

        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);   // 액션바 변경
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // 툴바 프로젝트 이름 제거

        trashListAdapter = new TrashListAdapter(SearchTrash.this, new ArrayList<>());
        listView.setAdapter(trashListAdapter);
        listView.setClickable(true);
        getTrashData();
        clickItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // 검색창 설정
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setIconifiedByDefault(false);    // 항상 확장된 상태O
        searchView.requestFocus();  // searchview에 포커스 요청+키보드 자동O

        searchView.post(() ->{
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);  //show_inplicit 하던가 아님 아예 0으로 처리
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {        // 검색창에 일어나는 이벤트 구현
            @Override
            public boolean onQueryTextSubmit(String str) {return false;}    // 키보드의 검색 버튼을 눌렀을 때 이벤트(=검색 업무 처리), 입력한 값을 str로 받아서 처리
            @Override
            public boolean onQueryTextChange(String text) {    // 검색어 입력할 때마다 실행됨
                if(text.isEmpty()){trashListAdapter.updateListviewItem(new ArrayList<>());}
                else {filterTrash(text);}
                return true;
            }
        });

        return true;
    }

    private void filterTrash(String text){      // 검색창에 입력한 텍스트가 있으면 텍스트값과 비교해서 같은 데이터를 result에 추가
        ArrayList<TrashListData> result = new ArrayList<>();

        for(int i=0; i<arrayList.size(); i++) {
            TrashListData trashListData = arrayList.get(i);
            if(trashListData.getTrashListName().toLowerCase().contains(text.toLowerCase())) {    // 입력한 텍스트가 포함되어 있는지 확인 (대소문자 구분X)
                result.add(trashListData);
            }
        }
        trashListAdapter.updateListviewItem(result);   // listview 업데이트 (초기화 문제인듯)
    }

    private void getTrashData() {       // 리스트뷰에 출력할 데이터 서버에서 가져와 arrayList에 저장 (검색 전에 이미 만들어져 있어야 하고, 데이터를 가져오기만 하므로 화면에 출력할 필요는 없을 듯)

        Call<List<RetroWriting>> call = RetroClient.getRetroService().getSearchTrashData();
        call.enqueue(new Callback<List<RetroWriting>>() {
            @Override
            public void onResponse(Call<List<RetroWriting>> call, Response<List<RetroWriting>> response) {
                if (response.isSuccessful()) {
                    String title, url, thum = null;
                    List<RetroWriting> retroWritings = response.body();

                    for (int i=0; i<7; i++){
                        RetroWriting writing = retroWritings.get(i);
                        title = writing.getTitle();
                        url = writing.getUrl();
                        thum = writing.getThumbnailUrl();

                        arrayList.add(new TrashListData(url, title, thum));
                        Log.i("SearchTrash 데이터 가져옴O", "title : " + arrayList.get(i).getTrashListName() + ", url: " + url + "thum : " + thum);
                    }
                }
                else {Log.e("SearchTrash.listview 출력X", "");}
            }

            @Override
            public void onFailure(Call<List<RetroWriting>> call, Throwable t) {
                Log.e("SearchTrash 네트워크 오류", "", t);
            }
        });
    }

    public void clickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchTrash.this, TrashDetail.class);
                intent.putExtra("trashName", arrayList.get(position).getTrashListName());
                intent.putExtra("trashImage", arrayList.get(position).getTrashListImage());
                intent.putExtra("trashInfo", arrayList.get(position).getTrashListInfo());
                startActivity(intent);
            }
        });
    }
}