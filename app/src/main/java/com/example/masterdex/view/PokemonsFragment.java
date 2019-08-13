package com.example.masterdex.view;


import android.content.Intent;
import android.os.Bundle;

import com.example.masterdex.R;
import com.example.masterdex.viewmodel.PokemonViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterdex.adapter.AdapterPokemon;
import com.example.masterdex.interfaces.PokemonListener;
import com.example.masterdex.models.Pokemon;
import com.example.masterdex.models.PokemonResponse;
import com.example.masterdex.service.pokeApi.PokeApi;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PokemonsFragment extends Fragment implements PokemonListener, SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerPokemons;
    private AdapterPokemon pokemonAdapter;
    private SwipeRefreshLayout swipe;
    private static final int LIMIT = 20;
    private int offset = 0;
    private LinearLayout MostrarBotoes;
    private boolean show;
    private SearchView searchView;

    public PokemonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemons, container, false);

        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>();

        searchView = view.findViewById(R.id.search_view);
        swipe = view.findViewById(R.id.swipe_refresh);
        swipe.setOnRefreshListener(() -> swipe.setRefreshing(false));

        recyclerPokemons = view.findViewById(R.id.recyclerView);
        MostrarBotoes = view.findViewById(R.id.linearLayout_id);
        FloatingActionButton floatActionButton = view.findViewById(R.id.button_mostrar_botoes_poke_home_id);
        pokemonAdapter = new AdapterPokemon(this, pokemonArrayList);


        GridLayoutManager LayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerPokemons.setLayoutManager(LayoutManager);
        recyclerPokemons.setAdapter(pokemonAdapter);


        PokemonViewModel pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);
        pokemonViewModel.atualizarPokemon(LIMIT, offset);
        pokemonViewModel.getPokemonLiveData()
                .observe(this, pokemons -> pokemonAdapter.atualizarListaPokemons(pokemons));

        //SearchView

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pokemonAdapter.getFilter().filter(newText);
                return false;
            }
        });

        floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (show) {
                    MostrarBotoes.setVisibility(View.INVISIBLE);
                    show = false;
                } else {

                    MostrarBotoes.setVisibility(View.VISIBLE);
                    show = true;
                }

            }
        });

        return view;
    }

    //    private void receberDados()
//    {
//        final PokeApi service = retrofit.create(PokeApi.class);
//        Call<PokemonResponse> pokemonRespostaCall = service.obterListaPokemon();
//        pokemonRespostaCall.enqueue(new Callback<PokemonResponse>() {
//            @Override
//            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
//                if (response.isSuccessful()) {
//                    PokemonResponse pokemonResposta = response.body(); //colocando na variavel os dados recuperados pelo metodo @GET
//                    ArrayList<Pokemon> pokemonArrayList = pokemonResposta.getResults(); //
//                    pokemonAdapter.adicionarListaPokemon(pokemonArrayList);// adicionando todos os objetos num array
//                } else {
//                }
//            }
//            @Override
//            public void onFailure(Call<PokemonResponse> call, Throwable t) {
//            }
//        });
//
//
//
//    }
    @Override
    public void onPokemonClicado(Pokemon pokemon) {
        Intent intent = new Intent(getContext(), DetalhesPokemonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("POKEMON", pokemon);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {

    }
}