package com.example.severinov;
//класс данных пользователя
public class Add {
    private String email;
    private String pass;
    private int score=0;
//конструктор
    public Add(){}
// геттеры сеттеры
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPass() { return pass;}
    public void setPass(String pass){this.pass = pass;}

    public int getScore(){return score;}
    public void setScore(int score){this.score=score;}

}