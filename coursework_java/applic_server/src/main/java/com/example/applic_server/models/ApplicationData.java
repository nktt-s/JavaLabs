package com.example.applic_server.models;

//import javafx.event.ActionEvent;

import java.io.Serializable;

public class ApplicationData implements Serializable {
// Имя заявщика - Вид заявки - Время Подачи - Текст Заявки
    private Integer id;
    private String status;
    private String worker;
    private String name;
    private String type;
    private String date;
    private String text;
  

    public ApplicationData(){
        this.id = 0;
        this.status="On Wait";
        this.name="John";
        this.type="Ticket";
        this.date="01.01.2001";
        this.text="I wanna buy this new ticket on Godbilla 4";
    }

   public ApplicationData(String text){
       this.id = 0;
       this.status="On Wait";
       this.name="John";
       this.type="Ticket";
       this.date="01.01.2001";
       this.text=text;
   }

 public ApplicationData(Integer id,String status, String name, String type, String date, String text, String worker){
     this.id = id;
     this.worker = worker;
     this.status=status;
   this.name=name;
   this.type=type;
   this.date=date;
   this.text=text;
 }

    public void print(){
        System.out.println("-".repeat(38));
        System.out.println(id);
        System.out.println(status);
        System.out.println(name);
        System.out.println(type);
        System.out.println(date);
        System.out.println(text);
        System.out.println(worker);
        System.out.println("-".repeat(38));

    }
    public void set_id(Integer id){
        this.id = id;
    }
    public void set_status(String status){
        this.status = status;
    }
    public void set_worker(String worker){this.worker = worker;}
    public String get_status(){
     return status;
 }
 public String get_name(){
   return name;
 }
public Integer get_id(){return id;}
  public String get_type(){
    return type;
  }
public String get_worker(){return worker;}
  public String get_date(){
    return date;
  }
  public String get_text(){
    return text;
  }

}
