import java.awt.Point;

public class Display {
    private MapData mapData;
    private AllyShipManager allyShipManager;

    public Display(MapData mapData, AllyShipManager allyShipManager) {
        this.mapData = mapData;
        this.allyShipManager = allyShipManager;
    }

    public void printMap() {
        mapData.printMap();
    }

    public void displayAllShipsLocation() {
        displayShipLocation(MapData.TYPE_AllyShip_SSN001);
        displayShipLocation(MapData.TYPE_AllyShip_SSN002);
        displayShipLocation(MapData.TYPE_AllyShip_SSN003);
        displayShipLocation(MapData.TYPE_AllyShip_SSN004);
        allyShipManager.displayAllShipsHp();
    }

    private void displayShipLocation(int shipType) {
        Point location = allyShipManager.findShipLocation(shipType);
        if (location != null) {
            char row = (char) ('A' + location.y);
            int col = location.x + 1;
            System.out.println("船 " + shipType + " の現在位置: " + row + col);
        } else {
            System.out.println("船 " + shipType + " はマップ上に存在しません。");
        }
    }
}
