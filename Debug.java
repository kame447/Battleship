import java.awt.Point;
import java.util.Scanner;

public class Debug {
    private MapData mapData;
    private AllyShipManager allyShipManager;
    private Move move;
    private ShipPlacer shipPlacer;
    private Display display;

    public Debug() {
        mapData = new MapData();
        allyShipManager = new AllyShipManager(mapData);
        shipPlacer = new ShipPlacer(mapData, allyShipManager);
        shipPlacer.placeShipsWithFullCoverage();
        display = new Display(mapData, allyShipManager);
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        String input;
        int steps;

        display.printMap();
        display.displayAllShipsLocation();

        // 船の選択
        System.out.println("操作する船を選択してください(1: SSN-001, 2: SSN-002, 3: SSN-003, 4: SSN-004):");
        int shipChoice = scanner.nextInt();
        scanner.nextLine(); // 改行文字を消費

        // 選択された船のタイプに基づいてMoveインスタンスを初期化
        int shipType = MapData.TYPE_AllyShip_SSN001 + shipChoice - 1;
        move = new Move(shipType, allyShipManager, mapData);

        while (true) {
            System.out.println("移動方向を入力してください（東(e)、西(w)、南(s)、北(n)）。終了するには 'f' と入力してください。");
            Point currentLocation = move.getCurrentLocation();
            System.out.println("現在の位置: (" + currentLocation.x + ", " + currentLocation.y + ")");
            input = scanner.nextLine();

            if (input.equals("f")) {
                break;
            }

            System.out.println("移動ステップ数を入力してください：");
            if (scanner.hasNextInt()) {
                steps = scanner.nextInt();
                scanner.nextLine(); // 改行文字を消費

                move.moveInDirection(input, steps);
                updateMapAndDisplay();
            } else {
                System.out.println("無効なステップ数です。");
                scanner.nextLine(); // 無効な入力を消費
            }
        }

        scanner.close();
    }

    private void updateMapAndDisplay() {
        Point newLocation = move.getCurrentLocation();
        int shipType = mapData.getMap((char) ('A' + newLocation.y), newLocation.x);

        if (allyShipManager.isAllyShip(shipType)) {
            allyShipManager.moveShip(shipType, newLocation);
        }

        display.printMap();
        display.displayAllShipsLocation();
    }

    public static void main(String[] args) {
        Debug game = new Debug();
        game.startGame();
    }
}
