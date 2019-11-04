package com.example.masterdex.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.masterdex.models.Pokemon;
import com.example.masterdex.repository.PokemonRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetalhesPokemonViewModel extends AndroidViewModel {

    private MutableLiveData<Pokemon> pokemonLiveData = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private PokemonRepository pokemonRepository = new PokemonRepository();

    public MutableLiveData<Pokemon> getPokemonLiveData() {
        return pokemonLiveData;
    }

    public DetalhesPokemonViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPokemonByName(String name) {
        disposable.add(
          pokemonRepository.getPokemonByName(name)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeOn(Schedulers.newThread())
          .subscribe(pokemon -> pokemonLiveData.setValue(pokemon))
        );
    }

    public void getPokemonSpecieByName(String name) {
        disposable.add(
                pokemonRepository.getPokemonSpecieByName(name)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(pokemon -> pokemonLiveData.setValue(pokemon))
        );
    }
}
