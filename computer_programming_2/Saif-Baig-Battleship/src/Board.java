import java.util.ArrayList;
import java.util.Random;

public class Board {
    public static final int SIZE = 10;
    private CellState[][] grid;
    private Ship[][] ships;
    private ArrayList<Ship> shipList;
    
    public Board() {
        grid = new CellState[SIZE][SIZE];
        ships = new Ship[SIZE][SIZE];
        shipList = new ArrayList<>();
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = CellState.BLANK;
                ships[i][j] = null;
            }
        }
    }
    
    public void placeShips() {
        initializeBoard();
        shipList.clear();
        
        int[] shipSizes = {5, 4, 3, 3, 2};
        String[] shipNames = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        
        Random random = new Random();
        
        for (int i = 0; i < shipSizes.length; i++) {
            boolean placed = false;
            int attempts = 0;
            
            while (!placed && attempts < 1000) {
                boolean horizontal = random.nextBoolean();
                int size = shipSizes[i];
                
                if (horizontal) {
                    int row = random.nextInt(SIZE);
                    int possibleStarts = SIZE - size + 1;
                    ArrayList<Integer> validStarts = new ArrayList<>();
                    
                    for (int start = 0; start < possibleStarts; start++) {
                        boolean canPlace = true;
                        for (int k = 0; k < size; k++) {
                            if (ships[row][start + k] != null) {
                                canPlace = false;
                                break;
                            }
                        }
                        if (canPlace) {
                            validStarts.add(start);
                        }
                    }
                    
                    if (!validStarts.isEmpty()) {
                        int chosenStart = validStarts.get(random.nextInt(validStarts.size()));
                        Ship ship = new Ship(shipNames[i], size);
                        shipList.add(ship);
                        
                        for (int k = 0; k < size; k++) {
                            ships[row][chosenStart + k] = ship;
                        }
                        placed = true;
                    }
                } else {
                    int col = random.nextInt(SIZE);
                    int possibleStarts = SIZE - size + 1;
                    ArrayList<Integer> validStarts = new ArrayList<>();
                    
                    for (int start = 0; start < possibleStarts; start++) {
                        boolean canPlace = true;
                        for (int k = 0; k < size; k++) {
                            if (ships[start + k][col] != null) {
                                canPlace = false;
                                break;
                            }
                        }
                        if (canPlace) {
                            validStarts.add(start);
                        }
                    }
                    
                    if (!validStarts.isEmpty()) {
                        int chosenStart = validStarts.get(random.nextInt(validStarts.size()));
                        Ship ship = new Ship(shipNames[i], size);
                        shipList.add(ship);
                        
                        for (int k = 0; k < size; k++) {
                            ships[chosenStart + k][col] = ship;
                        }
                        placed = true;
                    }
                }
                attempts++;
            }
        }
    }
    
    public CellState getCellState(int row, int col) {
        return grid[row][col];
    }
    
    public Ship getShip(int row, int col) {
        return ships[row][col];
    }
    
    public CellState fireAt(int row, int col) {
        if (grid[row][col] != CellState.BLANK) {
            return grid[row][col];
        }
        
        Ship ship = ships[row][col];
        
        if (ship != null) {
            grid[row][col] = CellState.HIT;
            ship.hit();
            return CellState.HIT;
        } else {
            grid[row][col] = CellState.MISS;
            return CellState.MISS;
        }
    }
    
    public ArrayList<Ship> getShipList() {
        return shipList;
    }
    
    public int getTotalShipCells() {
        int total = 0;
        for (Ship ship : shipList) {
            total += ship.getSize();
        }
        return total;
    }
    
    public boolean allShipsSunk() {
        for (Ship ship : shipList) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }
    
    public void reset() {
        initializeBoard();
        shipList.clear();
    }
}
