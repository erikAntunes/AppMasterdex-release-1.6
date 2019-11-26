package com.example.masterdex.view;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.masterdex.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InformativoActivity extends AppCompatActivity {

    Button irParaLogin;
    Button irParaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informativo);

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.azulBackground);
        pDialog.setTitleText("Carregando ...");

        pDialog.setCancelable(true);

        pDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        },800);

        irParaCadastro = findViewById(R.id.cadastrar_informativo_button);
        irParaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaTelaDeCadastro();
            }
        });

        irParaLogin = findViewById(R.id.login_informativo_button);
        irParaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaTelaDeLogin();
            }
        });


    }

    private void irParaTelaDeLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void irParaTelaDeCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
