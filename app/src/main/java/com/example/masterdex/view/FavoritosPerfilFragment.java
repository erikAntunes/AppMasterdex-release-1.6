package com.example.masterdex.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.masterdex.R;
import com.example.masterdex.adapter.AdapterPerfilFavoritos;
import com.example.masterdex.database.FavoritosDao;
import com.example.masterdex.database.FavoritosDb;
import com.example.masterdex.interfaces.PokemonListener;
import com.example.masterdex.models.Pokemon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.schedulers.Schedulers;
import static com.example.masterdex.repository.DetalhesPokemonRepository.FAVORITOS_DB;


public class FavoritosPerfilFragment extends Fragment implements PokemonListener {

    private RecyclerView favRv;
    private AdapterPerfilFavoritos favoritos;
    private FirebaseStorage storage;
    private FirebaseFirestore firebaseDb;
    private FirebaseUser user;
    private FavoritosDb favoritosDb;

    public FavoritosPerfilFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos_perfil, container, false);

        favoritosDb = Room.databaseBuilder(Objects.requireNonNull(getContext()),
                FavoritosDb.class, FAVORITOS_DB).build();
        user = FirebaseAuth.getInstance().getCurrentUser();
        favRv = view.findViewById(R.id.favoritos_perfil_recyclerview_id);
        storage = FirebaseStorage.getInstance();
        firebaseDb = FirebaseFirestore.getInstance();

        buscarTudoNoRoom();
        return view;
    }

    @Override
    public void onStart() {
        buscarTudoNoRoom();
        super.onStart();
    }

    @Override
    public void onResume() {
        buscarTudoNoRoom();
        super.onResume();
    }


    public void buscarTudoNoRoom() {

        FavoritosDao favoritosDao = favoritosDb.favoritosDao();
        favoritosDao.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(pokemonEncontrado -> {

                    favoritos = new AdapterPerfilFavoritos(this,pokemonEncontrado,getActivity());
                    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                    favRv.setLayoutManager(layoutManager);
                    favRv.setAdapter(favoritos);
                });
    }

    @Override
    public void onPokemonClicado(Pokemon pokemon) {

        Intent intent = new Intent(getContext(), DetalhesPokemonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("POKEMON", pokemon);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
