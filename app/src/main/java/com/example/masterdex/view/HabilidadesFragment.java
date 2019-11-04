package com.example.masterdex.view;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.masterdex.R;
import com.example.masterdex.adapter.AdapterHabilidades;


/**
 * A simple {@link Fragment} subclass.
 */
public class HabilidadesFragment extends Fragment {

    private AdapterHabilidades adapterHabilidades;
    private RecyclerView recyclerView;

    public HabilidadesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habilidades, container, false);

        return view;
    }
}
