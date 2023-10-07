import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

class Main
{
    public static void inputArray(int[] arr) {
        System.out.println("Enter elements for array (" + arr.length + " elements): ");
        for (int i = 0; i < arr.length; ++i) {
            Scanner sc = new Scanner(System.in);
            if (sc.hasNextInt()) {
                arr[i] = sc.nextInt();
            } else {
                System.out.println("Error: non-integer value. Try again!");
                sc.next();
                --i;
            }
        }
    }

    public static void outputArray(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    public static void decreaseElements(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] -= 10;
        }
    }

    public static void calculateNegative(int[] arr) {
        int counter = 0;
        for (int element : arr) {
            if (element < 0) {
                ++counter;
            }
        }
        System.out.println("Number of negative elements: " + counter);
    }

    public static void inputList(List<Integer> arr, int len) {
        System.out.println("Enter elements for array (" + len + " elements): ");
        for (int i = 0; i < len; ++i) {
            Scanner sc = new Scanner(System.in);
            if (sc.hasNextInt()) {
                arr.add(sc.nextInt());
            } else {
                System.out.println("Error: non-integer value. Try again!");
                sc.next();
                --i;
            }
        }
    }
    
    public static void outputList(List<Integer> arr) {
        for (Integer element : arr) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void decreaseElementsInList(List<Integer> arr) {
        for (int i = 0; i < arr.size(); ++i) {
            Integer elem = arr.get(i);
            elem -= 10;
            arr.set(i, elem);
        }
    }

    public static void calculateNegativeInList(List<Integer> arr) {
        int counter = 0;
        for (int element : arr) {
            if (element < 0) {
                ++counter;
            }
        }
        System.out.println("Number of negative elements: " + counter);
    }

    public static void main(String[] args)
    {
        int[] arr1 = new int[4];
        int[] arr2 = new int[5];
        int[] arr3 = new int[4];
        System.out.println("\n\t=== Array #1 ===");
        inputArray(arr1);
        System.out.println("\n\t=== Array #2 ===");
        inputArray(arr2);
        System.out.println("\n\t=== Array #3 ===");
        inputArray(arr3);

        System.out.println("\n\t=== Array #1 ===");
        outputArray(arr1);
        decreaseElements(arr1);
        outputArray(arr1);
        calculateNegative(arr1);
        System.out.println("\n\t=== Array #2 ===");
        outputArray(arr2);
        decreaseElements(arr2);
        outputArray(arr2);
        calculateNegative(arr2);
        System.out.println("\n\t=== Array #3 ===");
        outputArray(arr3);
        decreaseElements(arr3);
        outputArray(arr3);
        calculateNegative(arr3);

        ArrayList<Integer> arrList1 = new ArrayList<>();
        ArrayList<Integer> arrList2 = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        System.out.println("\n\t=== ArrayList #1 ===");
        inputList(arrList1, 4);
        System.out.println("\n\t=== ArrayList #2 ===");
        inputList(arrList2, 5);
        System.out.println("\n\t=== LinkedList ===");
        inputList(linkedList, 4);

        System.out.println("\n\t=== ArrayList #1 ===");
        outputList(arrList1);
        decreaseElementsInList(arrList1);
        outputList(arrList1);
        calculateNegativeInList(arrList1);

        System.out.println("\n\t=== ArrayList #2 ===");
        outputList(arrList2);
        decreaseElementsInList(arrList2);
        outputList(arrList2);
        calculateNegativeInList(arrList2);

        System.out.println("\n\t=== LinkedList ===");
        outputList(linkedList);
        decreaseElementsInList(linkedList);
        outputList(linkedList);
        calculateNegativeInList(linkedList);
    }
}