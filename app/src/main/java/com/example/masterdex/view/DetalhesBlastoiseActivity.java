package com.example.masterdex.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

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
import com.example.masterdex.database.FavoritosDb;
import com.example.masterdex.models.Pokemon;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.ViewPagerItems;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetalhesBlastoiseActivity extends AppCompatActivity {

    public static final String FAVORITOS_DB = "favoritos_Db";
    public static final String CAPTURADOS_DB = "capturados_Db";
    private static final String TAG = "DetalhesPokemonActivity";
    private ToggleButton botaoFavorito;
    private ToggleButton botaoCapturado;
    private FavoritosDb favoritosDb;
    private CapturadosDb capturadosDb;
    private ConstraintLayout backgroundPokemon;
    private ImageView tipoUnicoImageView;
    private ImageView tipoPrimarioImageView;
    private ImageView tipoSecundarioImageView;
    private Switch switchShine;
    private Switch switchBack;
    private boolean favoritado = false;
    private boolean capturado = false;
    private SmartTabLayout smartTabLayout;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;


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

                .add("STATS", R.layout.fragment_blastoise_status)
               // .add("HABILIDADES", R.layout.fragment_habilidades)
                .create());


        // Setup View Pager
        ViewPager viewPager = findViewById(R.id.blastoise_viewPager);
        viewPager.setAdapter(blastoiseAdapter);

        // Personalizando SmartTab
        SmartTabLayout blastoiseTabLayout = findViewById(R.id.detalhes_blastoise_smarttab);
        blastoiseTabLayout.setViewPager(viewPager);
        blastoiseAdapter.getPage(0);
        blastoiseTabLayout.setSelectedIndicatorColors(getColor(R.color.agua));

        //setupHabilidadesTab(pokemonApi, adapter.getPage(1));
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
