package com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.R;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.adapter.AdapterVideo;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.api.VideoService;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.helper.RetrofitConfig;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.helper.YoutubeConfig;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.listener.RecyclerItemClickListener;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.model.Item;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.model.Resultado;
import com.instagramfontes.cursoandroid.jamiltondamasceno.youtube.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    //Widgets
    private RecyclerView recyclerVideos;
    private MaterialSearchView searchView;

    private List<Item> videos = new ArrayList<>();
    private AdapterVideo adapterVideo;

    //retrofit
    private Retrofit retrofit;
    private Resultado resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializar componentes
        initComponents();

        //Configura toolbar
        configuraToolbar();

        //init Retrofit
        retrofit = RetrofitConfig.getRetrofit();


        //Recupera videos
        recuperarVideos("");

        //Configura métodos para SearchView
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recuperarVideos("");
            }
        });

    }

    private void configuraRecycleview() {
        adapterVideo = new AdapterVideo(videos, this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerVideos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Item video = videos.get(position);
                        String idVideo = video.id.videoId;
                        Intent i = new Intent(MainActivity.this, PlayerActivity.class);
                        i.putExtra("idVideo", idVideo);
                        startActivity(i);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }

    private void initComponents() {
        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);
    }

    private void configuraToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);
    }

    private void recuperarVideos(String pesquisa) {
        String q = pesquisa.replaceAll(" ", "+");
        VideoService videoService = retrofit.create(VideoService.class);
        if (q == "") {
            videoService.getVideos(
                    "snippet",
                    "20",
                    "rating",
                    q,
                    YoutubeConfig.CHAVE_YOUTUBE_API
            ).enqueue(
                    new Callback<Resultado>() {
                        @Override
                        public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                            Log.d("Resultado", "Resultado: " + response.toString());
                            if (response.isSuccessful()) {
                                resultado = response.body();
                                videos = resultado.items;
                                //Configura Recyclerview
                                configuraRecycleview();
                            }
                        }

                        @Override
                        public void onFailure(Call<Resultado> call, Throwable t) {
                            Log.d("Resultado", "Resultado: " + t.getMessage());
                        }
                    }
            );
        } else {
            videoService.getVideos(
                    "snippet",
                    "20",
                    "date",
                    q,
                    YoutubeConfig.CHAVE_YOUTUBE_API
            ).enqueue(
                    new Callback<Resultado>() {
                        @Override
                        public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                            Log.d("Resultado", "Resultado: " + response.toString());
                            if (response.isSuccessful()) {
                                resultado = response.body();
                                videos = resultado.items;
                                //Configura Recyclerview
                                configuraRecycleview();
                            }
                        }

                        @Override
                        public void onFailure(Call<Resultado> call, Throwable t) {
                            Log.d("Resultado", "Resultado: " + t.getMessage());
                        }
                    }
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);

        return true;
    }
}
