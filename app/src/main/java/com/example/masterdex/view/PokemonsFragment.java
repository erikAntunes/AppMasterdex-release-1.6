package com.example.masterdex.view;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.masterdex.R;
import com.example.masterdex.database.FavoritosDb;
import com.example.masterdex.viewmodel.PokemonViewModel;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import com.example.masterdex.adapter.AdapterPokemon;
import com.example.masterdex.interfaces.PokemonListener;
import com.example.masterdex.models.Pokemon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import pl.droidsonroids.gif.GifImageButton;


public class PokemonsFragment extends Fragment implements PokemonListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerPokemons;
    private AdapterPokemon pokemonAdapter;
    private SwipeRefreshLayout swipe;
    private static final int LIMIT = 20;
    private int offset = 0;
    private LinearLayout MostrarBotoes;
    private boolean show;
    private SearchView searchView;
    private ImageView azButton;
    private ImageView numeroButton;
    private Context Context;
    private Boolean ascending = true;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FavoritosDb favoritosDb;
    private GifImageButton gifImageButton;

    public PokemonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemons, container, false);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
        }

        gifImageButton =view.findViewById(R.id.gif);
        gifImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),QuizActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Pokemon> pokemonArrayList = new ArrayList<>();

        searchView = view.findViewById(R.id.search_view);
        recyclerPokemons = view.findViewById(R.id.recyclerView);
        MostrarBotoes = view.findViewById(R.id.linearLayout_id);
        FloatingActionButton floatActionButton = view.findViewById(R.id.button_mostrar_botoes_poke_home_id);
        azButton = view.findViewById(R.id.az_image);
        azButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortName(ascending);
                ascending = !ascending;
            }
        });


        numeroButton = view.findViewById(R.id.numero_image);
        numeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortNumber(ascending);
                ascending = !ascending;
            }
        });

        pokemonAdapter = new AdapterPokemon(this, pokemonArrayList);

        receberDadosApi();

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

    public void sortName(Boolean asc) {
        if (asc) {
            pokemonAdapter.sortNameByAsc();
        } else {
            pokemonAdapter.sortNameByDesc();
        }
    }

    public void sortNumber(Boolean asc) {
        if (asc) {
            pokemonAdapter.sortNumberByAsc();
        } else {
            pokemonAdapter.sortNumberByDesc();
        }
    }

    private void receberDadosApi() {

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        recyclerPokemons.setLayoutManager(layoutManager);
        recyclerPokemons.setAdapter(pokemonAdapter);
        PokemonViewModel pokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);
        pokemonViewModel.atualizarPokemon(LIMIT, offset);
        pokemonViewModel.getPokemonLiveData()
                .observe(this, pokemons -> pokemonAdapter.atualizarListaPokemons(pokemons));
    }

    @Override
    public void onPokemonClicado(Pokemon pokemon) {

        if (pokemon.getNumber() == 9){

            Intent intent = new Intent(getContext(), DetalhesBlastoiseActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(getContext(), DetalhesPokemonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("POKEMON", pokemon);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        receberDadosApi();
    }
}
