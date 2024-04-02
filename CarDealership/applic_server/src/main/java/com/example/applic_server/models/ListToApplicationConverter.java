package com.example.applic_server.models;

import java.util.ArrayList;
import java.util.List;

public class ListToApplicationConverter {
    public void imhere() {
//        System.out.println("I'm convector!");
    }

    public List<ApplicationData> list_to_applications(List<String> str_list) {
        int n = str_list.size() / 7;
        List<ApplicationData> app_list = new ArrayList<>();
//        System.out.println("Before: " + str_list);
        for (int i = 0; i < n; i++) {
            app_list.add(new ApplicationData(Integer.parseInt(str_list.get(i * 7)), str_list.get(i * 7 + 1),
                str_list.get(i * 7 + 2), str_list.get(i * 7 + 3), str_list.get(i * 7 + 4), str_list.get(i * 7 + 5), str_list.get(i * 7 + 6)));
        }
//        System.out.println("After: " + app_list);
        return app_list;
    }

    public List<String> app_to_list(List<ApplicationData> applic) {
        List<String> list = new ArrayList<>();
        for (ApplicationData app : applic) {
            list.add(Integer.toString(app.get_id()));
            list.add(app.get_status());
            list.add(app.get_name());
            list.add(app.get_type());
            list.add(app.get_date());
            list.add(app.get_text());
            list.add(app.get_worker());
        }
        return list;
    }

    public ApplicationData list_to_application(List<String> str_list) {
//        System.out.println("In here was str_list 6 " + str_list.get(6));
        return new ApplicationData(Integer.parseInt(str_list.get(0)), str_list.get(1), str_list.get(2),
            str_list.get(3), str_list.get(4), str_list.get(5), str_list.get(6));
    }
}
