package com.example.masterdex.view;
import android.content.Intent;
import com.example.masterdex.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    Button entrar;
    TextInputEditText emailDigitado;
    TextInputEditText senhaDigitada;
    TextView esqueciSenha;
    Button irParaCadastro;
    private ImageView botaoVoltarParaHome;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        firebaseAuth = FirebaseAuth.getInstance();
        emailDigitado = findViewById(R.id.email_login);
        senhaDigitada = findViewById(R.id.senha_login);
        entrar = findViewById(R.id.entrar_login_button);
        esqueciSenha = findViewById(R.id.login_esqueci_senha);
        irParaCadastro = findViewById(R.id.botao_ir_para_cadastro);
        botaoVoltarParaHome = findViewById(R.id.button_voltar_para_home);
        botaoVoltarParaHome.setOnClickListener(view -> voltarParaHome());
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrarNaConta();

            }
        });

        esqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaEsqueciSenha();
            }
        });

        irParaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irParaTelaDeCadastro();
            }
        });
    }

    public void voltarParaHome() {

        LoginActivity.this.onBackPressed();
    }

    private void irParaTelaDeCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void entrarNaConta() {

        android.view.inputmethod.InputMethodManager teclado = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (teclado.isAcceptingText()) {
            teclado.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        //Em caso de teclado visível, este método esconde ele

        if (emailDigitado.getText().toString().equals("")) {
            //Se não houver nada no campo de email aparecerá essa mensagem
            emailDigitado.setError("Digite um email");
        }

        if (senhaDigitada.getText().toString().equals("")) {
            //Se nãp huver nada no campo de senha aparecerá essa mensagem
            senhaDigitada.setError("Digite uma senha");
        } else {
            logar();
        }
    }

    public void irParaEsqueciSenha() {
        Intent intent = new Intent(this, EsqueciASenha.class);
        startActivity(intent);
    }

    public void irParaMain() {
        Toasty.success(getApplicationContext(),"Logado com sucesso");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void logar() {
        String email = emailDigitado.getEditableText().toString();
        String senha = senhaDigitada.getEditableText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            irParaMain();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(), "Verifique email ou senha.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
