package com.example.masterdex.view;
import com.example.masterdex.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;


public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";
    private TextInputEditText textEditNick;
    private TextInputEditText textEditEmail;
    private TextInputEditText textEditSenha;
    private TextInputEditText textEditConfirmarSenha;
    private ImageView botaoVoltarParaLogin;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

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

        Button cadastrar = findViewById(R.id.cadastrar_button);
        cadastrar.setOnClickListener(view -> cadastroRealizado());
        initComponents();
        botaoVoltarParaLogin.setOnClickListener(view -> irParaMain());
    }

    private void irParaMain() {

        Intent intent = new Intent(this, PerfilFragment.class);
        startActivity(intent);

    }

    private void initComponents() {

        textEditNick = findViewById(R.id.nickCadastro);
        textEditEmail = findViewById(R.id.emailCadastro);
        textEditSenha = findViewById(R.id.senhaCadastro);
        textEditConfirmarSenha = findViewById(R.id.confiSenhaCadastro);
        botaoVoltarParaLogin = findViewById(R.id.button_voltar_para_login);
    }

    public void irParaEditarPerfil() {

        Toasty.success(getApplicationContext(),"Cadastro Realizado Com Sucesso");
        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivity(intent);

    }


    public void cadastroRealizado() {

        android.view.inputmethod.InputMethodManager teclado = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (teclado.isAcceptingText()) {
            teclado.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
        //Em caso de teclado visível, este método esconde ele

        if (Objects.requireNonNull(textEditNick.getText()).toString().equals("")) {
            //Se não houver nada no campo de email aparecerá essa mensagem
            textEditNick.setError("Digite um Nick");

        }
        if (Objects.requireNonNull(textEditEmail.getText()).toString().equals("")) {
            //Se não houver nada no campo de email aparecerá essa mensagem
            textEditEmail.setError("Digite seu Email");

        }
        if (Objects.requireNonNull(textEditSenha.getText()).toString().equals("")) {
            //Se não houver nada no campo de email aparecerá essa mensagem
            textEditSenha.setError("Digite uma Senha");
        }

        if (Objects.requireNonNull(textEditConfirmarSenha.getText()).toString().equals("")) {
            //Se nãp huver nada no campo de senha aparecerá essa mensagem
            textEditConfirmarSenha.setError("Digite uma Senha");
        } else {
            cadastrar();
        }
    }


    private void cadastrar() {

        String email = textEditEmail.getEditableText().toString();
        String password = textEditSenha.getEditableText().toString();
        String displayName = textEditNick.getEditableText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");

                        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Cadastro")

                                .setContentText("Cadastro realizado com sucesso!")

                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        logar();
                                    }
                                })
                                .show();


                    } else {

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Por favor revise seus dados, talvez o usuario ja exista no sistema")
                                .show();
                    }
                });
    }

    public void logar() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String nome = textEditNick.getEditableText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            irParaEditarPerfil();
                            finish();
                        }
                    }
                });

      //  assert user != null;
      //  user.updateProfile(profileUpdates)
      //          .addOnCompleteListener(new OnCompleteListener<Void>() {
      //              @Override
      //              public void onComplete(@NonNull Task<Void> task) {
      //                  if (task.isSuccessful()) {
      //                      Log.d(TAG, "Seu cadastro foi atualizado.");
      //                      new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
      //                              .setTitleText("Cadastro")
      //                              .setContentText("Cadastro atualizado com sucesso!")
      //                              .show();
      //                  }
      //              }
      //          });
    }
}
