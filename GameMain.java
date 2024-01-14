import java.awt.Point;
import java.util.Scanner;

import javax.xml.crypto.Data;

class GameMain {
    public static MapData mapData;
    private static AllyShipManager allyShipManager;
    private Move move;
    private ShipPlacer shipPlacer;
    private Forecast forecast;
    private Algorithm algorithm;
    private DataMemorizer dataMemorizer;
    public static int roundCounter = 0;
    private Undo undo;

    public GameMain() {
        mapData = new MapData();
        allyShipManager = new AllyShipManager(mapData);
        shipPlacer = new ShipPlacer(mapData, allyShipManager);
        shipPlacer.placeShipsWithFullCoverage();
        forecast = new Forecast();
        algorithm = new Algorithm(mapData, allyShipManager);
        dataMemorizer = new DataMemorizer(allyShipManager);
        undo = new Undo(allyShipManager, mapData);
    }

    public static void main(String[] args) {
        GameMain game = new GameMain();
        Scanner scanner = new Scanner(System.in);
        int showHistory;

        while (true) {
            // 情報表示
            System.out.println("= = = = = = = = = = = = = =");
            System.out.println("= = = = = ROUND " + (roundCounter + 1) + " = = = = =");
            System.out.println("= = = = = = = = = = = = = =");
            GameMain.printMap();
            GameMain.displayAllShipsLocation();
            DataMemorizer.dataMemorizer(roundCounter);
            DataMemorizer.EFMemorizer(roundCounter);
            DataMemorizer.HPMemorizer(roundCounter);

            // 操作の決定
            System.out.print("攻撃結果入力(1)or相手の攻撃(2)or相手の移動(3)orアルゴ起動(4)\norアルゴ攻撃(5)or移動(6)orUndo(7)or履歴確認(8)終了(f): ");
            String actionDecision = scanner.nextLine();

            if (actionDecision.equals("f")) {
                System.out.println("ゲームを終了します。");
                break;
            }

            switch (actionDecision) {
                case "1":
                    // 自分からの攻撃
                    game.handleAllyAttack(scanner);
                    break;
                case "2":
                    // 敵からの攻撃
                    game.handleEnemyAttack(scanner);
                    //アルゴリズムをAlgorithm.java側から起動する
                    break;
                case "3":
                    // 敵の移動を処理する
                    handleEnemyMovement(scanner);
                    System.out.println("【自軍の動作】");
                    Algorithm.allyAction();
                    break;
                case "4":
                    // アルゴリズム起動
                    System.out.println("【自軍の動作】");
                    Algorithm.allyAction();
                    break;
                case "5":
                    // アルゴリズム攻撃
                    System.out.println("【自軍の動作】");
                    Algorithm.startAttackAlgorithm();
                    break;
                case "6":
                    // 移動処理
                    game.handleMovement(scanner);
                    break;
                case "7":
                    Undo.undoToPastRound();
                    break;
                case "8":
                    DataDisplay.DisplayData();
                    break;
                default:
                    System.out.println("有効な番号を入力してください。");
                    continue;
            }
            System.out.println("【現在のEF値】");
            Forecast.printEF();

            System.out.println("次の操作をしてください");
            roundCounter++;
            if(actionDecision == "7") roundCounter--;
        }

        scanner.close();
    }

    private void handleMovement(Scanner scanner) {
        // 船の選択
        System.out.println("操作する船を選択してください(1: SSN-001, 2: SSN-002, 3: SSN-003, 4: SSN-004):");
        int shipChoice = scanner.nextInt();
        scanner.nextLine(); // 改行文字を消費

        // 選択された船のタイプに基づいてMoveインスタンスを初期化
        int shipType = MapData.TYPE_AllyShip_SSN001 + shipChoice - 1;
        move = new Move(shipType, allyShipManager, mapData);

        System.out.println("移動方向を入力してください（東(e)、西(w)、南(s)、北(n)）。");
        Point currentLocation = move.getCurrentLocation();
        System.out.println("現在の位置: (" + currentLocation.x + ", " + currentLocation.y + ")");
        String direction = scanner.nextLine();

        System.out.println("移動ステップ数を入力してください：");
        int steps = scanner.nextInt();
        scanner.nextLine(); // 改行文字を消費

        move.moveInDirection(direction, steps);
        updateMapAndDisplay();
    }

    private void updateMapAndDisplay() {
        Point newLocation = move.getCurrentLocation();
        int shipType = mapData.getMap((char) ('A' + newLocation.y), newLocation.x);

        if (allyShipManager.isAllyShip(shipType)) {
            allyShipManager.moveShip(shipType, newLocation);
        }

        printMap();
        displayAllShipsLocation();
    }

    private static void printMap() {
        mapData.printMap();
    }

    private static void displayAllShipsLocation() {
        displayShipLocation(MapData.TYPE_AllyShip_SSN001);
        displayShipLocation(MapData.TYPE_AllyShip_SSN002);
        displayShipLocation(MapData.TYPE_AllyShip_SSN003);
        displayShipLocation(MapData.TYPE_AllyShip_SSN004);
        allyShipManager.displayAllShipsHp();
    }

    private static void displayShipLocation(int shipType) {
        Point location = allyShipManager.findShipLocation(shipType);
        if (location != null) {
            char row = (char) ('A' + location.y);
            int col = location.x + 1;
            System.out.println("船 " + shipType + " の現在位置: " + row + col);
        } else {
            System.out.println("船 " + shipType + " はマップ上に存在しません。");
        }
    }

    // 敵からの攻撃を処理するメソッド
    private void handleEnemyAttack(Scanner scanner) {
        System.out.println("敵から攻撃を受けた座標を入力してください(例:B2):");
        String target = Input.strInput();
        int x = target.charAt(1) - '1'; // 列の変換
        int y = target.charAt(0) - 'A'; // 行の変換

        // EnemyAttack インスタンスを作成し、攻撃を実行
        EnemyAttack enemyAttack = new EnemyAttack(mapData, allyShipManager);
        enemyAttack.executeAttack(x, y);

    }

    private void handleAllyAttack(Scanner scanner) {
        System.out.println("攻撃する座標を入力してください(例:A1):");
        String target = scanner.nextLine();
        int x = target.charAt(1) - '1'; // 列の変換
        int y = target.charAt(0) - 'A'; // 行の変換

        // AllyAttack インスタンスを作成し、攻撃を実行
        AllyAttack allyAttack = new AllyAttack(mapData, allyShipManager, scanner);
        allyAttack.executeAttack(x, y);
    }

    private static void handleEnemyMovement(Scanner scanner) {
        System.out.println("敵の移動方向を入力してください（東(e)、西(w)、南(s)、北(n)):");
        String direction = scanner.nextLine();

        System.out.println("移動ステップ数を入力してください：");
        int steps = scanner.nextInt();
        scanner.nextLine(); // 改行文字を消費

        // Algorithm クラスの enemyMoveAlgorithm メソッドを呼び出す
        Algorithm.enemyMoveAlgorithm(direction, steps);
    }
}