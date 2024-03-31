package com.example.car_dealership_client.models;

import java.util.ArrayList;
import java.util.List;

public class ListToApplicationConverter {

  public List<ApplicationData> list_to_application(List<String> str_list){
      int n = str_list.size()/7;
      List<ApplicationData> app_list = new ArrayList<>();
//      System.out.println("Before: " + str_list);
      for (int i=0; i<n; i++){
          app_list.add(new ApplicationData(Integer.parseInt(str_list.get(i*7)), str_list.get(i*7+1),
                  str_list.get(i*7+2), str_list.get(i*7+3),str_list.get(i*7+4), str_list.get(i*7+5), str_list.get(i*7+6)));
      }
//      System.out.println("After: " + app_list);
      return app_list;
  }

  public List<String> app_to_list(ApplicationData applic){
      List<String> list = new ArrayList<>();
      list.add(Integer.toString(applic.get_id()));
      list.add(applic.get_status());
      list.add(applic.get_name());
      list.add(applic.get_type());
      list.add(applic.get_date());
      list.add(applic.get_text());
      list.add(applic.get_worker());
     return list;
  }
}
