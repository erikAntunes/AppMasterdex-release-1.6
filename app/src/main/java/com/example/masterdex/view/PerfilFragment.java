package com.example.masterdex.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.example.masterdex.R;
import com.example.masterdex.adapter.PerfilViewPagerAdapter;
import com.example.masterdex.database.FavoritosDb;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private Button buttonOpcoesPerfil;
    private CircleImageView fotoPerfil;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser user;
    private TextView nomePerfil;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayoutPerfil;
    private ViewPager viewPagerPerfil;
    private FavoritosDb favoritosDb;
    private FirebaseFirestore db;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        tabLayoutPerfil = view.findViewById(R.id.tablayout_perfil_id);
        viewPagerPerfil = view.findViewById(R.id.viewpager_perfil_id);
        PerfilViewPagerAdapter perfilViewPagerAdapter = getPerfilViewPagerAdapter();
        tabLayoutPerfil.setSelected(true);
        viewPagerPerfil.setAdapter(perfilViewPagerAdapter);
        tabLayoutPerfil.setupWithViewPager(viewPagerPerfil);
        nomePerfil = view.findViewById(R.id.nome_perfil_text_view);
        fotoPerfil = view.findViewById(R.id.foto_perfil_circle_image_view);
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getUser();

        buttonOpcoesPerfil = view.findViewById(R.id.button_opcoes_perfil);
        buttonOpcoesPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(getActivity(), buttonOpcoesPerfil);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(PerfilFragment.this);
            }
        });

        return view;
    }

    @Override
    public void onResume() {

        getUser();

        super.onResume();
    }

    @Override
    public void onStart() {

        getUser();
        super.onStart();
    }

    private void getUser() {
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            nomePerfil.setText(name);
            baixarFoto();
        }
    }

    private PerfilViewPagerAdapter getPerfilViewPagerAdapter() {
        PerfilViewPagerAdapter perfilViewPagerAdapter = new PerfilViewPagerAdapter(getChildFragmentManager());

        perfilViewPagerAdapter.AddFragment(new CapturadosPerfilFragment(), "Capturados");

        perfilViewPagerAdapter.AddFragment(new FavoritosPerfilFragment(), "Favoritos");

        perfilViewPagerAdapter.AddFragment(new PopuladoresPerfilFragment(), "Populares");
        return perfilViewPagerAdapter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.item_editar_perfil:
                if (user != null){
                    Intent intent = new Intent(getContext(), EditarPerfilActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    return true;
                }

            case R.id.item_ir_para_perfil:
                if (user != null){
                    Intent intent1 = new Intent(getContext(), MainActivity.class);
                    startActivity(intent1);
                    return true;
                } else{
                    Intent intent2 = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent2);
                    return true;
                }

            case R.id.item_sair:
                signOut();
                Intent intent1 = new Intent(getContext(), MainActivity.class);
                startActivity(intent1);
            default:
                return false;
        }
    }

    private void baixarFoto() {
        StorageReference reference = storage.getReference("perfil/" + user.getUid());
        reference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(fotoPerfil));
    }

    public void signOut() {
        firebaseAuth.getInstance()
                .signOut();
    }
}
