package com.example.masterdex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masterdex.R;
import com.example.masterdex.models.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterPerfilPopulares extends RecyclerView.Adapter<AdapterPerfilPopulares.ViewHolder> {

    private List<Pokemon> populares;

    public AdapterPerfilPopulares(List<Pokemon> populares ) {


        this.populares = new ArrayList<>(populares);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.celula_favoritos_votacao,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        final Pokemon pokemon = populares.get(position);

        String pok = pokemon.getName();
        pok = pok.substring(0, 1).toUpperCase().concat(pok.substring(1));




        viewHolder.textNomePokemon.setText(pok);


        //Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokemon.getNumber()+".png")
                //.into(viewHolder.imageFotoPokemon);
    }

    @Override
    public int getItemCount() {
        return populares.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textNomePokemon;
      //  private ImageView imageFotoPokemon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textNomePokemon = itemView.findViewById(R.id.nome_pokemon_votado);
            //imageFotoPokemon = itemView.findViewById(R.id.imageFotoPokemon);

        }
    }

    public void atualizarPopularesPerfil(List<Pokemon> favoritosListPokemon){
        populares = favoritosListPokemon;

        notifyDataSetChanged();
    }
}