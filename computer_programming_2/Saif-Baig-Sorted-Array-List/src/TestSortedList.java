import java.util.ArrayList;

public class TestSortedList {
    public static void main(String[] args) {
        SortedList list = new SortedList();
        
        list.add("zebra");
        list.add("apple");
        list.add("mango");
        list.add("banana");
        list.add("cherry");
        
        System.out.println("=== Adding elements in unsorted order ===");
        System.out.println("Added: zebra, apple, mango, banana, cherry");
        System.out.println("\n=== List after adding (should be sorted) ===");
        System.out.println(list.toString());
        
        System.out.println("=== Binary Search Tests ===");
        System.out.println("Search 'banana': " + list.search("banana"));
        System.out.println("Search 'grape': " + list.search("grape"));
        System.out.println("Search 'zzz': " + list.search("zzz"));
        System.out.println("Search 'aaa': " + list.search("aaa"));
    }
}