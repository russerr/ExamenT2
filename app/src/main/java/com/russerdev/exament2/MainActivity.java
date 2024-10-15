package com.russerdev.exament2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.russerdev.exament2.entity.Album;
import com.russerdev.exament2.service.AlbumService;
import com.russerdev.exament2.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Al crear el spinner se crean tres objectos
    Spinner spnALbums;
    ArrayAdapter<String> adapterAlbums;
    ArrayList<String> listAlbums = new ArrayList<String>();

    Button btnFiltrar;
    TextView txtResultado;

    //Servicio retrofit de Album
    AlbumService albumService;

    //Aqui estaran toda la data
    List<Album> lstSalida ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapterAlbums = new ArrayAdapter<String>(
                this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listAlbums);
        spnALbums = findViewById(R.id.spnAlbums);
        spnALbums.setAdapter(adapterAlbums);

        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtResultado = findViewById(R.id.txtResultado);

        albumService = ConnectionRest.getConnecion().create(AlbumService.class);

        cargaAlbumes();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idAlbum = (int) spnALbums.getSelectedItemId();
                Album objAlbum = lstSalida.get(idAlbum);
                String salida = "Title : " + objAlbum.getTittle() +"\n";
                salida += "Id : " + objAlbum.getId() +"\n";
                salida += "UserId : " + objAlbum.getUserId() +"\n";
                txtResultado.setText(salida);
            }
        });
    }

    void cargaAlbumes(){
        Call<List<Album>> call =  albumService.listAlbums();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                //Las respuesta es exitosa  del servicio Rest
                if (response.isSuccessful()){
                    lstSalida = response.body();
                    for (Album obj: lstSalida){
                        listAlbums.add(obj.getTittle());
                    }
                    adapterAlbums.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                //No existe respuesta del servicio Rest

            }
        });
    }
}