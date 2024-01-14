public class DataMemorizer {
    public static AllyShipManager allyShipManager;
    public static int[][][] mapStorage = new int[1000][5][5];
    public static int[][][] efMemorizer = new int[1000][5][5];
    public static int[][] hpStrage = new int[1000][4];

    DataMemorizer(AllyShipManager allyShipManager) {this.allyShipManager = allyShipManager;}
    
    // 現在のマップデータを取得して格納
    public static void dataMemorizer(int turn) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                mapStorage[turn][row][col] = GameMain.mapData.getMap((char)('A' + row), col);
                // 以下、値が正しく取得されているか確認するためのプログラム(基本コメントアウト)
                // System.out.println(mapStrage[turn][row][col]);
                // System.out.println(GameMain.mapData.getMap((char)('A' + row), col));
            }
        }
    }

    // 現在のEFデータを取得して格納
    public static void EFMemorizer(int turn) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                efMemorizer[turn][row][col] = Forecast.getEF(row, col);
                // 以下、値が正しく取得されているか確認するためのプログラム(基本コメントアウト)
                // System.out.println(efMemorizer[turn][row][col]);
                // System.out.println(Forecast.getEF(col, row));
            }
        }
    }

    // 現在のHPデータを取得して格納
    public static void HPMemorizer(int turn) {
        for(int i = 1; i < 5; i++) {
            hpStrage[turn][i - 1] = allyShipManager.getShipHp(i);
            // 以下、値が正しく取得されているか確認するためのプログラム(基本コメントアウト)
            // System.out.println(hpStrage[turn][i - 1]); //
        }
    }
}

class DataDisplay {
    // 特定のターンのマップデータを取得して表示
    public static void DisplayData() {
        System.out.print("何ターン目の図を表示しますか？: ");
        int turn = Input.intInput();
        System.out.println("|===MAP====|");
        MapDisplay(turn);
        System.out.println("|==========|");
        System.out.println("|===E F====|");
        EFDisplay(turn);
        System.out.println("|==========|");
        HPDisplay(turn);
    }

    public static void MapDisplay(int turn) {
        for(int i = 0; i < 5; i++) {
            System.out.print("|");
            for(int j = 0; j < 5; j++) {
                System.out.print(DataMemorizer.mapStorage[turn][i][j] + " ");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public static void EFDisplay(int turn) {
        for(int i = 0; i < 5; i++) {
            System.out.print("|");
            for(int j = 0; j < 5; j++) {
                System.out.print(DataMemorizer.efMemorizer[turn][i][j] + " ");
            }
            System.out.print("|");
            System.out.println();
        }
    }

    public static void HPDisplay(int turn) {
        for(int i = 0; i < 4; i++) {
            System.out.println("Ship" + i + ": " + DataMemorizer.hpStrage[turn][i]);
        }
    }
}

class GetPastData {
    public static int getPastMapData(int turn, int i, int j) {
        return DataMemorizer.mapStorage[turn][i][j];
    }

    public static int getPastEFData(int turn, int i, int j) {
        return DataMemorizer.efMemorizer[turn][i][j];
    }

    public static int getPastHPData(int turn, int i) {
        return DataMemorizer.hpStrage[turn][i];
    }
}

class MapStorage {
    private static int[][][] mapDataForTurn = new int[1000][5][5]; // 3次元配列でマップデータを格納
    // 現在のターンのマップデータを格納するメソッド
    public static void storeMapData(int currentTurn, int[][] data) {
        mapDataForTurn[currentTurn] = data;
    }

    // 指定されたターンのマップデータを取得するメソッド
    public static int[][] getMapDataForTurn(int turn) {
        return mapDataForTurn[turn];
    }
}