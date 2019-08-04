package com.example.masterdex;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.masterdex.adapter.AdapterPokemon;
import com.example.masterdex.interfaces.PokemonListener;
import com.example.masterdex.models.Pokemon;
import com.example.masterdex.models.PokemonResposta;
import com.example.masterdex.pokeApi.PokeApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PokemonsFragment extends Fragment  implements PokemonListener
{

    //criando as referencias
    private Retrofit retrofit;
    private TextView textNomePokemon;
    private ImageView imageFotoPokemon;
    private RecyclerView recyclerPokemons;
    private AdapterPokemon pokemonAdapter;




    public PokemonsFragment()
    {
        // Required empty public constructor
    }

    // utilizado no método mostrarBotoes
    private LinearLayout MostrarBotoes;
    private boolean show;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pokemons, container, false);



        // ligando as coisas, lembrando que tudo está sendo instanciado em uma View
        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>();
        textNomePokemon = view.findViewById(R.id.textNomePokemon);
        imageFotoPokemon = view.findViewById(R.id.imageFotoPokemon);

        //SearchView

        searchView = view.findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() ==0 ||newText ==null){
                    pokemonArrayList.clear();
                    receberDados();
                }else{
                    pokemonAdapter.getFilter().filter(newText);


                }

                return false;
            }
        });

        // SetupRecyclerView

        recyclerPokemons = view.findViewById(R.id.recyclerView);
        pokemonAdapter = new AdapterPokemon(this, pokemonArrayList);
        GridLayoutManager LayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerPokemons.setLayoutManager(LayoutManager);
        recyclerPokemons.setAdapter(pokemonAdapter);

        //FloatingActionButton mostra/esconde os botões de ordem alfabética e ordem númerica, que por sua vez orenam a lista de Pokemons.
        MostrarBotoes = view.findViewById(R.id.linearLayout_id);
        FloatingActionButton floatActionButton = view.findViewById(R.id.button_mostrar_botoes_poke_home_id);
        floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                receberDados();

                if (show)
                {
                    MostrarBotoes.setVisibility(View.INVISIBLE);
                    show = false;
                } else
                    {

                    MostrarBotoes.setVisibility(View.VISIBLE);
                    show = true;
                }

            }
        });

        // retrofit em ação
        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")// url que ira ser passada para ser consumida
                .addConverterFactory(GsonConverterFactory.create())// conversor que ira converter um json em objeto
                .build();
        receberDados();
        return view;
    }
    private void receberDados()
    {
        final PokeApi service = retrofit.create(PokeApi.class);
        Call<PokemonResposta> pokemonRespostaCall = service.obterListaPokemon();
        pokemonRespostaCall.enqueue(new Callback<PokemonResposta>() {
            @Override
            public void onResponse(Call<PokemonResposta> call, Response<PokemonResposta> response) {
                if (response.isSuccessful()) {
                    PokemonResposta pokemonResposta = response.body(); //colocando na variavel os dados recuperados pelo metodo @GET
                    ArrayList<Pokemon> pokemonArrayList = pokemonResposta.getResults(); //
                    pokemonAdapter.adicionarListaPokemon(pokemonArrayList);// adicionando todos os objetos num array
                } else {
                }
            }
            @Override
            public void onFailure(Call<PokemonResposta> call, Throwable t) {
            }
        });
    }
    @Override
    public void onPokemonClicado(Pokemon pokemon)
    {
        Intent intent = new Intent(getContext(), DetalhesPokemonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("POKEMON", pokemon);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
