package com.example.applic_server.models;

import com.example.applic_server.controllers.SerMainController;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleMenu implements Runnable {
    Scanner menu_scan = new Scanner(System.in);
    boolean menu = true;

    @Override
    public void run() {
        while (menu) {
            System.out.println("--------Server--------------------------------------------------");
            System.out.println("1: Show all orders");
            System.out.println("2: Show all On Wait");
            System.out.println("3: Show all Progress");
            System.out.println("4: Show all Closed");
            System.out.println("5: Shutdown");

            System.out.print(">");
            Integer input = read_integer(1, 7, menu_scan);
            List<ApplicationData> all_applics = SerMainController.get_all_applications();
            switch (input) {
                case 1:
                    all_applics.forEach(ApplicationData::print);
                    break;
                case 2:
                    all_applics.forEach((applic) -> {
                        if (Objects.equals(applic.get_status(), "On Wait")) applic.print();
                    });
                    break;
                case 3:
                    all_applics.forEach((applic) -> {
                        if (Objects.equals(applic.get_status(), "In Progress")) applic.print();
                    });
                    break;
                case 4:
                    all_applics.forEach((applic) -> {
                        if (applic.get_status().equals("Cancelled") & applic.get_status().equals("Closed")
                            & applic.get_status().equals("Finished"))
                            applic.print();
                    });
                    break;
                case 5:
                    SerMainController.serialize_applics();
                    SerMainController.serialize_total();
                    System.exit(0);
                    break;
            }
        }
    }

    public static Integer read_integer(Integer range_1, Integer range_2, Scanner scan) {
        int input_int = range_1 - 1;
        while (input_int == range_1 - 1) {
            try {
                input_int = Integer.parseInt(scan.nextLine());
                if (input_int < range_1 || input_int > range_2) {
                    System.out.println("Sike! That's the wrong number! Try again");
                    input_int = range_1 - 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Sike! That's the wrong number! Try again");
            }
        }

        return input_int;

    }
}
