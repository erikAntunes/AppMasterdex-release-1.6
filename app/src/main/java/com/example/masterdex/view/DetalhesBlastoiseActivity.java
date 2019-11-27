package com.example.masterdex.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.masterdex.R;
import com.example.masterdex.database.CapturadosDb;
import com.example.masterdex.database.FavoritosDao;
import com.example.masterdex.database.FavoritosDb;
import com.example.masterdex.models.Pokemon;
import com.example.masterdex.viewmodel.DetalhesPokemonViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetalhesBlastoiseActivity extends AppCompatActivity {

    private ConstraintLayout backgroundPokemon;
    private ImageView tipoUnicoImageView;
    private Switch switchShine;
    private Switch switchBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_blastoise);

        // Show de Loader
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.azulBackground);
        pDialog.setTitleText("Carregando ...");

        pDialog.setCancelable(true);

        pDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        },1500);



        // Ajustando Cores do Background
        backgroundPokemon = findViewById(R.id.background_constraint_detalhe_blastoise_pokemon);
        backgroundPokemon.setBackground(getDrawable(R.drawable.detalhes_background_agua));

        // Ajustando a Cor da Status Bar
        String aguaColor = "#719AEE";
        changeStatusBarColor(aguaColor);

        // Ajustando Botão Voltar
        ImageView botaoVoltar = findViewById(R.id.detalhes_blastoise_voltar);
        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetalhesBlastoiseActivity.this.onBackPressed();
            }
        });

        // Ajustando Tipo do Miseravel
        tipoUnicoImageView = findViewById(R.id.detalhes_blastoise_tipo_unico_image_view);
        tipoUnicoImageView.setBackground(getDrawable(R.drawable.ic_type_water));

        // Instanciando o Miseravel
        Pokemon blastoise = new Pokemon();
        blastoise.setName("blastoise");
        blastoise.setId(9);

        // Ajustando a Imagem do Miseravel
        ImageView imagemPokemon = findViewById(R.id.detalhes_blastoise_image_view);
        Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + blastoise.getId() + ".png").into(imagemPokemon);

        switchShine = findViewById(R.id.switch_shine_blastoise_id);

        switchShine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchShine.isChecked() && switchBack.isChecked()) {

                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/" + blastoise.getId() + ".png").into(imagemPokemon);

                } else if (switchShine.isChecked()) {

                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + blastoise.getId() + ".png").into(imagemPokemon);

                } else if (switchBack.isChecked()) {
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/" + blastoise.getId() + ".png").into(imagemPokemon);

                } else
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + blastoise.getId() + ".png").into(imagemPokemon);
            }
        });

        switchBack = findViewById(R.id.switch_back_blastoise_id);

        switchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchBack.isChecked() && switchShine.isChecked()) {

                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/" + blastoise.getId() + ".png").into(imagemPokemon);

                } else if (switchBack.isChecked()) {
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/" + blastoise.getId() + ".png").into(imagemPokemon);

                } else if (switchShine.isChecked()) {
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/" + blastoise.getId() + ".png").into(imagemPokemon);
                } else
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + blastoise.getId() + ".png").into(imagemPokemon);
            }
        });
        setupSmartTab();
    }

    private void setupSmartTab() {

        ViewPagerItemAdapter blastoiseAdapter = new ViewPagerItemAdapter(ViewPagerItems.with(this)

                .add("PODER", R.layout.fragment_blastoise_status)
                .add("HABILIDADES", R.layout.fragment_blastoise_habilidades)
                .create());

        // Setup View Pager
        ViewPager viewPager = findViewById(R.id.blastoise_viewPager);
        viewPager.setAdapter(blastoiseAdapter);

        // Personalizando SmartTab
        SmartTabLayout blastoiseTabLayout = findViewById(R.id.detalhes_blastoise_smarttab);
        blastoiseTabLayout.setViewPager(viewPager);
        blastoiseAdapter.getPage(0); // PODER
        blastoiseAdapter.getPage(1); // HABILIDADES
        blastoiseTabLayout.setSelectedIndicatorColors(getColor(R.color.agua));

    }

    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }
}
