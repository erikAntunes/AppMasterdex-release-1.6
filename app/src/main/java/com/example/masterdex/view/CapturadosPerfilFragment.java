package com.example.masterdex.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.example.masterdex.R;
import com.example.masterdex.adapter.AdapterPerfilCapturados;
import com.example.masterdex.database.CapturadosDao;
import com.example.masterdex.database.CapturadosDb;
import com.example.masterdex.interfaces.PokemonListener;
import com.example.masterdex.models.Pokemon;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import java.util.Objects;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import static com.example.masterdex.repository.DetalhesPokemonRepository.CAPTURADOS_DB;

public class CapturadosPerfilFragment extends Fragment implements PokemonListener {
    private RecyclerView capRv;
    private AdapterPerfilCapturados capturados;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private CapturadosDb capturadosDb;

    public CapturadosPerfilFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capturados_perfil, container, false);

        capturadosDb = Room.databaseBuilder(Objects.requireNonNull(getContext()),
                CapturadosDb.class, CAPTURADOS_DB).build();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
        }

        capRv = view.findViewById(R.id.capturados_perfil_recyclerview_id);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        buscarTudoNoRoom();
        return view;

    }

    @Override
    public void onResume() {
        buscarTudoNoRoom();
        super.onResume();
    }

    private void buscarTudoNoRoom() {
        CapturadosDao capturadosDao = capturadosDb.capturadosDao();
        capturadosDao.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(pokemon -> {

                    capturados = new AdapterPerfilCapturados(this,pokemon, getActivity());
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                    capRv.setLayoutManager(layoutManager);
                    capRv.setAdapter(capturados);
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
