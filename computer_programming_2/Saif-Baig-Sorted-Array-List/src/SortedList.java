import java.util.ArrayList;

public class SortedList {
    private ArrayList<String> list;
    
    public SortedList() {
        list = new ArrayList<>();
    }
    
    public int size() {
        return list.size();
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public boolean contains(String element) {
        return binarySearch(element) >= 0;
    }
    
    public int binarySearch(String target) {
        int left = 0;
        int right = list.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int compare = list.get(mid).compareTo(target);
            
            if (compare == 0) {
                return mid;
            } else if (compare < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -(left + 1);
    }
    
    public int findInsertPosition(String element) {
        int result = binarySearch(element);
        if (result >= 0) {
            return result;
        }
        return -(result + 1);
    }
    
    public boolean add(String element) {
        if (element == null || element.isEmpty()) {
            return false;
        }
        
        int position = findInsertPosition(element);
        if (position >= 0 && position < list.size() && list.get(position).equals(element)) {
            return false;
        }
        
        list.add(position, element);
        return true;
    }
    
    public boolean remove(String element) {
        int index = binarySearch(element);
        if (index >= 0) {
            list.remove(index);
            return true;
        }
        return false;
    }
    
    public String get(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        return null;
    }
    
    public String search(String element) {
        int result = binarySearch(element);
        if (result >= 0) {
            return "Found at index " + result + ": " + list.get(result);
        } else {
            int insertPos = -(result + 1);
            return "Not found. Would be inserted at index " + insertPos;
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(i).append(": ").append(list.get(i)).append("\n");
        }
        return sb.toString();
    }
    
    public String getAllElements() {
        return list.toString();
    }
}