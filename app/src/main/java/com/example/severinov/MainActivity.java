package com.example.severinov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    //подключение объектов с xml файла
    private Button sign;
    private Button reg;
    private EditText mail;
    private EditText pass;

    //инициализация нужных переменных для работы с firebase
    FirebaseAuth auth=FirebaseAuth.getInstance(); //авторизация
    FirebaseDatabase db=FirebaseDatabase.getInstance(); //подключение к db
    DatabaseReference users=db.getReference("All users"); //работа с табличками dp

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //назначаем переменным конкретные объекты по id
        mail=findViewById(R.id.et_email);
        pass=findViewById(R.id.et_password);
        sign=findViewById(R.id.btn_sign_in);
        reg=findViewById(R.id.btn_registration);

        //блокируем кнопки
        reg.setEnabled(false);
        sign.setEnabled(false);

        //когда текст меняется в логине, запускается слушатель пароля и когда он меняется, кнопки разблокируются
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start,int before, int count) {
                pass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) { }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,int count, int after) { }
                    @Override
                    public void onTextChanged(CharSequence s, int start,int before, int count) {
                        reg.setEnabled(true);
                        sign.setEnabled(true);
                    }
                });
            }
        });

        // слушатель кнопки регистрации (обработка клика)
        reg.setOnClickListener(view->{

            //добавление юзера
            auth.createUserWithEmailAndPassword(mail.getText().toString(),pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                @Override
                public void onSuccess(AuthResult authResult){

                    //создаем объект класса для нового юзера
                    Add user=new Add();

                    //назначаем нужны поля
                    user.setEmail(mail.getText().toString());
                    user.setPass(pass.getText().toString());

                    //добавляем в бд
                    users.child(auth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void unused){
                            Toast.makeText(MainActivity.this,"успешно",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        // слушатель кнопки авторизации (обработка клика)
        sign.setOnClickListener(view->{

            //авторизуем юзера
            auth.signInWithEmailAndPassword(mail.getText().toString(),pass.getText().toString()).addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task){
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,"успешно",Toast.LENGTH_SHORT).show();

                        // запускаем новое окно, если авторизация успешна
                        Intent intent=new Intent(MainActivity.this,Game.class);
                        startActivity(intent);
                    }
                }
            });
        });
    }
}