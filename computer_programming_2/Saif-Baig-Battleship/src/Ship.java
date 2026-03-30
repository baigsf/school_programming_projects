public class Ship {
    private String name;
    private int size;
    private int hits;
    private boolean sunk;
    
    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hits = 0;
        this.sunk = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getHits() {
        return hits;
    }
    
    public boolean isSunk() {
        return sunk;
    }
    
    public void hit() {
        hits++;
        if (hits >= size) {
            sunk = true;
        }
    }
    
    public void reset() {
        hits = 0;
        sunk = false;
    }
}
