package com.example.masterdex.view;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.masterdex.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PerfilFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    Button buttonOpcoesPerfil;
    private CircleImageView fotoPerfil;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseUser user;
    private TextView nomePerfil;
    private FirebaseAuth firebaseAuth;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);




        nomePerfil = view.findViewById(R.id.nome_perfil_text_view);
        fotoPerfil = view.findViewById(R.id.foto_perfil_circle_image_view);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            nomePerfil.setText(name);
            baixarFoto();
        }




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
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.item_editar_perfil:
                Intent intent = new Intent(getContext(), EditarPerfilActivity.class);
                startActivity(intent);
                return true;

            case R.id.item_sair:
                Intent intent2 = new Intent(getContext(), LoginActivity.class);
                startActivity(intent2);
                return true;
            default:
                return false;
        }
    }

    private void baixarFoto() {



        StorageReference reference = storage.getReference("perfil/" + user.getUid());
        reference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(fotoPerfil))
                .addOnFailureListener(exception -> Toast.makeText(getActivity(),
                        "Erro ao baixar foto",
                        Toast.LENGTH_SHORT).show());




    }
}
