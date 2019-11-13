package com.example.masterdex.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.masterdex.R;
import com.example.masterdex.models.BlastoiseHabilidades;
import com.example.masterdex.models.Pokemon;
import com.example.masterdex.models.SlotHabilidade;
import java.util.List;

public class AdapterBlastoiseHabilidades extends RecyclerView.Adapter<AdapterBlastoiseHabilidades.ViewHolder> {
    private List<BlastoiseHabilidades> blastoiseHabilidadesList;
    private Pokemon pokemon;

    public AdapterBlastoiseHabilidades (List<BlastoiseHabilidades> blastoiseHabilidadesList, Pokemon pokemon){
        this.blastoiseHabilidadesList = blastoiseHabilidadesList;
        this.pokemon = pokemon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habilidades_celula, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BlastoiseHabilidades habilidade = blastoiseHabilidadesList.get(position);
        holder.setupHabilidade(habilidade);

    }

    @Override
    public int getItemCount() {
        return blastoiseHabilidadesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView habilidadeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            habilidadeTextView = itemView.findViewById(R.id.habilidades_text_view);
        }

        public void setupHabilidade (BlastoiseHabilidades habilidade){

            habilidadeTextView.setText(habilidade.getName());
        }
    }
}
