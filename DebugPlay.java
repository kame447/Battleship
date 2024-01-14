import java.awt.Point;

public class DebugPlay {
    private static ShipPlacer shipPlacer;

    public static void main(String[] args) {
        // MapDataのインスタンスを作成
        MapData mapData = new MapData();

        // AllyShipManagerのインスタンスを作成
        AllyShipManager allyShipManager = new AllyShipManager(mapData);

        // ShipPlacerを初期化
        shipPlacer = new ShipPlacer(mapData, allyShipManager);
        int attempts = shipPlacer.placeShipsWithFullCoverage();

        // マップを表示
        printMap(mapData);

        // 各船のHPを表示
        allyShipManager.displayAllShipsHp();

        // 特定の船の現在位置を取得して表示
        displayShipLocation(allyShipManager, MapData.TYPE_AllyShip_SSN001);
        displayShipLocation(allyShipManager, MapData.TYPE_AllyShip_SSN002);
        displayShipLocation(allyShipManager, MapData.TYPE_AllyShip_SSN003);
        displayShipLocation(allyShipManager, MapData.TYPE_AllyShip_SSN004);

        // 試行回数を表示
        System.out.println("船配置試行回数: " + attempts);
    }

    // マップを表示するメソッド
    public static void printMap(MapData mapData) {
        // MapDataクラスのprintMapメソッドを呼び出してマップを表示
        mapData.printMap();
    }

    // 特定の船の現在位置を表示するメソッド
    public static void displayShipLocation(AllyShipManager allyShipManager, int shipType) {
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
