package com.example.masterdex.view;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.masterdex.R;

public class BlastoiseHabilidadesFragment extends Fragment {

    private ImageView easterEgg;

    public BlastoiseHabilidadesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blastoise_habilidades, container, false);


        return view;
    }
}



