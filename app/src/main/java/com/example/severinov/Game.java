package com.example.severinov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.Vector;

public class Game extends AppCompatActivity {
    //подключение объектов с xml файла
    private Button check;
    private EditText cor;
    private TextView incor;
    private TextView total;
    //счетчик прогресса
    private int score;

    //переменные для работы с firebase
    FirebaseDatabase db = FirebaseDatabase.getInstance(); // подключение к бд
    FirebaseUser data = FirebaseAuth.getInstance().getCurrentUser(); //получение пользователя
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("All users").child(data.getUid()); //работа с конкретным полем бд
    String id=ref.getKey(); // получение id пользователя

    //предварительный рандом задания до нажатия кнопки
    int r = 9;
    Random ra = new Random();
    int in = ra.nextInt(r);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //назначаем переменным конкретные объекты по id
        cor=findViewById(R.id.zadanie);
        incor=findViewById(R.id.incor);
        check = findViewById(R.id.check);
        total = findViewById(R.id.total);

        //подключение к конкретному полю бд
        DatabaseReference getscore = db.getReference().child("All users").child(id).child("score");

        //получение данных из этого поля
        getscore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
            score=Integer.valueOf(snapshot.getValue().toString());
            total.setText("Прогресс: "+String.valueOf(score));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(Game.this, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            }
        });

        //массивы(вектора) заданий
        Vector task = new Vector<String>();
        Vector task_correct = new Vector<String>();

        ////заполняем массивы(вектора) заданий

        //неверные ответы
        String i0="У Наташы жил котишка, рыжий плутишка",i1="В высокай трове съёжился и дрожит маленький ёжик", i2="В саду расли бальшые кусты душистово шиповника",i3="Трищат сароки в частом лесу",i4="Начался чястый дождик",i5="Ведро с щюкой паставили в чулан",i6="Малышь испугался чючела и закречал",i7="Чюжой катёнок забрёл в чулан",i8="На стале стаяли чайник и чяшки",i9="Цветы на клумби долго неприживались";
        task.add(i0);task.add(i1);task.add(i2);task.add(i3);task.add(i4);task.add(i5);task.add(i6);task.add(i7);task.add(i8);task.add(i9);

        //верные ответы
        String c0="У Наташи жил котишка, рыжий плутишка",c1="В высокой траве съёжился и дрожит маленький ёжик", c2="В саду росли большие кусты душистого шиповника",c3="Трещат сороки в частом лесу",c4="Начался частый дождик",c5="Ведро с щукой поставили в чулан",c6="Малыш испугался чучела и закричал",c7="Чужой котёнок забрёл в чулан",c8="На столе стояли чайник и чашки",c9="Цветы на клумбе долго не приживались";
        task_correct.add(c0);task_correct.add(c1);task_correct.add(c2);task_correct.add(c3);task_correct.add(c4);task_correct.add(c5);task_correct.add(c6);task_correct.add(c7);task_correct.add(c8);task_correct.add(c9);

        // выводим первое задание (до нажатия кнопки) в TextView
        incor.setText(task.get(in).toString());

        //Игровая логика
        check.setOnClickListener(view -> {

            //проверка правильности
            if (cor.getText().toString().equals(task_correct.get(in).toString())) {

                //проверка есть ли еще задания
                if (r !=1) {
                    Toast.makeText(Game.this, "ВЕРНО", Toast.LENGTH_SHORT).show();
                    score++;
                    total.setText("Прогресс: "+String.valueOf(score));
                    //обновляем счет пользователя
                    ref.child("score").setValue(score);

                    //удаляем старое задание из массивов заданий
                    task.remove(in);
                    task_correct.remove(in);
                    r--;

                    //рандомим новое задание
                    in = ra.nextInt(r);
                    incor.setText(task.get(in).toString());
                    cor.setText("");
                }else {
                    //игра пройдена
                    incor.setText("");
                    cor.setText("");
                    Toast.makeText(Game.this, "Задания закончились, новые будут скоро", Toast.LENGTH_SHORT).show();
                }
            } else {
                //ответ неверный
                total.setText("Прогресс: "+String.valueOf(score));
                score--;
                //обновляем счет пользователя
                ref.child("score").setValue(score);
                Toast.makeText(Game.this, "НЕВЕРНО", Toast.LENGTH_SHORT).show();
            }
        });
    }
}