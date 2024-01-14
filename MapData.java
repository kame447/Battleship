/* このクラスの説明
 * class MapData は、ゲームのマップ上に何が配置されているかを記録し、保持するクラス
 * マップは二次元配列で表され、各セルには異なるタイプのオブジェクト（空白、船、障害物など）が割り当てられる
 * 
 * 主要なメソッド:
 * - fillMap(int type): マップを指定されたタイプで埋める
 * - setMap(char row, int col, int type): 指定された位置にマップタイプを設定する
 * - getMap(char row, int col): 指定された位置のマップタイプを取得する
 * - printMap(): マップの内容をターミナルに表示する
 * 
 * 以下メモ:
 * - 敵の船は的中が出たときのみ表示されるようにする
 * -味方の船との重複は考慮しない
 * - アルゴリズムで使用する評価表とは別のものとして扱う
 * - 沈没船の情報はアルゴリズムの評価表に反映され、その位置の評価は常にゼロに設定する
 */
public class MapData {
    public static final int TYPE_SPACE = 0; // 空白を表す定数
    public static final int TYPE_AllyShip_SSN001 = 1;// 船を表す定数
    public static final int TYPE_AllyShip_SSN002 = 2;
    public static final int TYPE_AllyShip_SSN003 = 3;
    public static final int TYPE_AllyShip_SSN004 = 4;
    public static final int TYPE_EnemyShip_A = 5;
    public static final int TYPE_EnemyShip_B = 6;
    public static final int TYPE_EnemyShip_C = 7;
    public static final int TYPE_EnemyShip_D = 8;
    public static final int TYPE_debris = 9; // 障害物を表す定数

    // 味方と敵の船の組み合わせを表す定数
    public static final int TYPE_AllyShip_SSN001_Enemy_A = 10;
    public static final int TYPE_AllyShip_SSN001_Enemy_B = 11;
    public static final int TYPE_AllyShip_SSN001_Enemy_C = 12;
    public static final int TYPE_AllyShip_SSN001_Enemy_D = 13;

    public static final int TYPE_AllyShip_SSN002_Enemy_A = 14;
    public static final int TYPE_AllyShip_SSN002_Enemy_B = 15;
    public static final int TYPE_AllyShip_SSN002_Enemy_C = 16;
    public static final int TYPE_AllyShip_SSN002_Enemy_D = 17;

    public static final int TYPE_AllyShip_SSN003_Enemy_A = 18;
    public static final int TYPE_AllyShip_SSN003_Enemy_B = 19;
    public static final int TYPE_AllyShip_SSN003_Enemy_C = 20;
    public static final int TYPE_AllyShip_SSN003_Enemy_D = 21;

    public static final int TYPE_AllyShip_SSN004_Enemy_A = 22;
    public static final int TYPE_AllyShip_SSN004_Enemy_B = 23;
    public static final int TYPE_AllyShip_SSN004_Enemy_C = 24;
    public static final int TYPE_AllyShip_SSN004_Enemy_D = 25;
    private int[][] maps; // マップのデータを保持する二次元配列
    private int width; // マップの幅
    private int height; // マップの高さ

    // コンストラクタ
    MapData() {
        width = 5; // 幅を5に設定
        height = 5; // 高さを5に設定
        maps = new int[height][width];

        fillMap(TYPE_SPACE); // マップをスペースで埋める
    }

    // マップを指定されたタイプで埋めるメソッド
    private void fillMap(int type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = type;
            }
        }
    }

    // 指定された位置のマップタイプを取得するメソッド
    public int getMap(char row, int col) {
        int rowIndex = row - 'A'; // アルファベットを数値インデックスに変換
        if (rowIndex < 0 || height <= rowIndex || col < 0 || width <= col) {
            return -1;
        }
        return maps[rowIndex][col];
    }

    public int getAllyMap(char row, int col) {
        int rowIndex = row - 'A'; // アルファベットを数値インデックスに変換
        int result = 0;
        if (rowIndex < 0 || height <= rowIndex || col < 0 || width <= col) {
            return -1;
        }
        if (maps[rowIndex][col] == 0) {
            result = 0;
        }
        if (maps[rowIndex][col] == 1 || maps[rowIndex][col] == 10 || maps[rowIndex][col] == 11
                || maps[rowIndex][col] == 12 || maps[rowIndex][col] == 13) {
            result = 1;
        }
        if (maps[rowIndex][col] == 2 || maps[rowIndex][col] == 14 || maps[rowIndex][col] == 15
                || maps[rowIndex][col] == 16 || maps[rowIndex][col] == 17) {
            result = 2;
        }
        if (maps[rowIndex][col] == 3 || maps[rowIndex][col] == 18 || maps[rowIndex][col] == 19
                || maps[rowIndex][col] == 20 || maps[rowIndex][col] == 21) {
            result = 3;
        }
        if (maps[rowIndex][col] == 4 || maps[rowIndex][col] == 22 || maps[rowIndex][col] == 23
                || maps[rowIndex][col] == 24 || maps[rowIndex][col] == 25) {
            result = 4;
        }
        return result;
    }

    // getMapで得た番号をGUI用に変換するメゾット
    public static String getMapExportString(int type) {
        switch (type) {
            case TYPE_SPACE:
                return "海(空白)";
            case TYPE_AllyShip_SSN001:
                return "SSN-001";
            case TYPE_AllyShip_SSN002:
                return "SSN-002";
            case TYPE_AllyShip_SSN003:
                return "SSN-003";
            case TYPE_AllyShip_SSN004:
                return "SSN-004";
            case TYPE_EnemyShip_A:
                return "敵戦艦A";
            case TYPE_EnemyShip_B:
                return "敵戦艦B";
            case TYPE_EnemyShip_C:
                return "敵戦艦C";
            case TYPE_EnemyShip_D:
                return "敵戦艦D";
            case TYPE_debris:
                return "沈没船";
            case TYPE_AllyShip_SSN001_Enemy_A:
                return "SSN-001 & 敵戦艦A";
            case TYPE_AllyShip_SSN001_Enemy_B:
                return "SSN-001 & 敵戦艦B";
            case TYPE_AllyShip_SSN001_Enemy_C:
                return "SSN-001 & 敵戦艦C";
            case TYPE_AllyShip_SSN001_Enemy_D:
                return "SSN-001 & 敵戦艦D";
            case TYPE_AllyShip_SSN002_Enemy_A:
                return "SSN-002 & 敵戦艦A";
            case TYPE_AllyShip_SSN002_Enemy_B:
                return "SSN-002 & 敵戦艦B";
            case TYPE_AllyShip_SSN002_Enemy_C:
                return "SSN-002 & 敵戦艦C";
            case TYPE_AllyShip_SSN002_Enemy_D:
                return "SSN-002 & 敵戦艦D";
            case TYPE_AllyShip_SSN003_Enemy_A:
                return "SSN-003 & 敵戦艦A";
            case TYPE_AllyShip_SSN003_Enemy_B:
                return "SSN-003 & 敵戦艦B";
            case TYPE_AllyShip_SSN003_Enemy_C:
                return "SSN-003 & 敵戦艦C";
            case TYPE_AllyShip_SSN003_Enemy_D:
                return "SSN-003 & 敵戦艦D";
            case TYPE_AllyShip_SSN004_Enemy_A:
                return "SSN-004 & 敵戦艦A";
            case TYPE_AllyShip_SSN004_Enemy_B:
                return "SSN-004 & 敵戦艦B";
            case TYPE_AllyShip_SSN004_Enemy_C:
                return "SSN-004 & 敵戦艦C";
            case TYPE_AllyShip_SSN004_Enemy_D:
                return "SSN-004 & 敵戦艦D";
            default:
                return "不明";
        }
    }

    // 指定された位置のマップタイプを設定するメソッド
    public void setMap(char row, int col, int type) {
        int rowIndex = row - 'A';
        if (rowIndex < 0 || height <= rowIndex || col < 0 || width <= col) {
            return;
        }
        maps[rowIndex][col] = type;
        // saveCurrentMap(maps[rowIndex][col]);
    }

    // マップの内容をターミナルに表示するメソッド
    public void printMap() {
        // 盤面のヘッダーを表示
        writeLogLine("【盤面】");
        writeLog("  ");
        for (int i = 0; i < width; i++) {
            writeLog(String.valueOf(i + 1) + " ");
        }
        writeLogLine("");

        System.out.print("  ───");
        for (int x = 0; x < 3; x++) {
            System.out.print("──");
        }
        System.out.println();

        // マップの内容を表示
        for (int y = 0; y < height; y++) {
            writeLog((char) ('A' + y) + "│");
            for (int x = 0; x < width; x++) {
                int cellType = getMap((char) ('A' + y), x);
                switch (cellType) {
                    case TYPE_SPACE:
                        writeLog("0 "); // 空白は0で表示
                        break;
                    case TYPE_debris:
                        writeLog("X "); // 障害物（沈没船）はXで表示
                        break;
                    case TYPE_AllyShip_SSN001:
                        writeLog("1 "); // 味方の1番の船
                        break;
                    case TYPE_AllyShip_SSN002:
                        writeLog("2 "); // 味方の2番の船
                        break;
                    case TYPE_AllyShip_SSN003:
                        writeLog("3 "); // 味方の3番の船
                        break;
                    case TYPE_AllyShip_SSN004:
                        writeLog("4 "); // 味方の4番の船
                        break;
                    case TYPE_EnemyShip_A:
                        writeLog("A "); // 敵のA番の船
                        break;
                    case TYPE_EnemyShip_B:
                        writeLog("B "); // 敵のB番の船
                        break;
                    case TYPE_EnemyShip_C:
                        writeLog("C "); // 敵のC番の船
                        break;
                    case TYPE_EnemyShip_D:
                        writeLog("D "); // 敵のD番の船
                        break;
                    // 味方と敵の船の組み合わせを表示
                    case TYPE_AllyShip_SSN001_Enemy_A:
                        writeLog("1A");
                        break;
                    case TYPE_AllyShip_SSN001_Enemy_B:
                        writeLog("1B");
                        break;
                    case TYPE_AllyShip_SSN001_Enemy_C:
                        writeLog("1C");
                        break;
                    case TYPE_AllyShip_SSN001_Enemy_D:
                        writeLog("1D");
                        break;
                    case TYPE_AllyShip_SSN002_Enemy_A:
                        writeLog("2A ");
                        break;
                    case TYPE_AllyShip_SSN002_Enemy_B:
                        writeLog("2B");
                        break;
                    case TYPE_AllyShip_SSN002_Enemy_C:
                        writeLog("2C ");
                        break;
                    case TYPE_AllyShip_SSN002_Enemy_D:
                        writeLog("2D");
                        break;
                    case TYPE_AllyShip_SSN003_Enemy_A:
                        writeLog("3A");
                        break;
                    case TYPE_AllyShip_SSN003_Enemy_B:
                        writeLog("3B");
                        break;
                    case TYPE_AllyShip_SSN003_Enemy_C:
                        writeLog("3C");
                        break;
                    case TYPE_AllyShip_SSN003_Enemy_D:
                        writeLog("3D");
                        break;
                    case TYPE_AllyShip_SSN004_Enemy_A:
                        writeLog("4A");
                        break;
                    case TYPE_AllyShip_SSN004_Enemy_B:
                        writeLog("4B");
                        break;
                    case TYPE_AllyShip_SSN004_Enemy_C:
                        writeLog("4C");
                        break;
                    case TYPE_AllyShip_SSN004_Enemy_D:
                        writeLog("4D");
                        break;
                    default:
                        writeLog("- ");
                        break;
                }
            }
            writeLogLine("");
        }
    }

    // ログ出力用のヘルパーメソッド
    private void writeLog(String line) {
        System.out.print(line);
    }

    private void writeLogLine(String line) {
        System.out.println(line);
    }

    // マップの幅を取得するメソッド
    public int getWidth() {
        return width;
    }

    // マップの高さを取得するメソッド
    public int getHeight() {
        return height;
    }

    // 敵船を配置するメソッド
    public void placeEnemyShip(char row, int col) {
        int currentType = getMap(row, col);
        int newType = determineNewTypeForEnemyShip(currentType);

        // 新しいタイプをマップに設定
        if (newType != -1) {
            setMap(row, col, newType);
        }
    }

    // 現在のマップタイプに基づいて新しいタイプを決定するメソッド
    private int determineNewTypeForEnemyShip(int currentType) {
        switch (currentType) {
            case TYPE_SPACE:
                return findNextAvailableEnemyShipType();
            case TYPE_AllyShip_SSN001:
                return findNextAvailableAllyEnemyCombination(TYPE_AllyShip_SSN001_Enemy_A, TYPE_AllyShip_SSN001_Enemy_B,
                        TYPE_AllyShip_SSN001_Enemy_C, TYPE_AllyShip_SSN001_Enemy_D);
            case TYPE_AllyShip_SSN002:
                return findNextAvailableAllyEnemyCombination(TYPE_AllyShip_SSN002_Enemy_A, TYPE_AllyShip_SSN002_Enemy_B,
                        TYPE_AllyShip_SSN002_Enemy_C, TYPE_AllyShip_SSN002_Enemy_D);
            case TYPE_AllyShip_SSN003:
                return findNextAvailableAllyEnemyCombination(TYPE_AllyShip_SSN003_Enemy_A, TYPE_AllyShip_SSN003_Enemy_B,
                        TYPE_AllyShip_SSN003_Enemy_C, TYPE_AllyShip_SSN003_Enemy_D);
            case TYPE_AllyShip_SSN004:
                return findNextAvailableAllyEnemyCombination(TYPE_AllyShip_SSN004_Enemy_A, TYPE_AllyShip_SSN004_Enemy_B,
                        TYPE_AllyShip_SSN004_Enemy_C, TYPE_AllyShip_SSN004_Enemy_D);
            default:
                return -1; // 既存の敵船や障害物がある場合は変更しない
        }
    }

    // 利用可能な次の敵船のタイプを見つけるメソッド
    private int findNextAvailableEnemyShipType() {
        if (!isTypePresentOnMap(TYPE_EnemyShip_A))
            return TYPE_EnemyShip_A;
        if (!isTypePresentOnMap(TYPE_EnemyShip_B))
            return TYPE_EnemyShip_B;
        if (!isTypePresentOnMap(TYPE_EnemyShip_C))
            return TYPE_EnemyShip_C;
        if (!isTypePresentOnMap(TYPE_EnemyShip_D))
            return TYPE_EnemyShip_D;
        return TYPE_SPACE; // すべての敵船が配置されている場合は空白を返す
    }

    // 特定のタイプがマップ上に存在するかどうかをチェックするメソッド
    private boolean isTypePresentOnMap(int type) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maps[y][x] == type) {
                    return true;
                }
            }
        }
        return false;
    }

    // 味方船と敵船の組み合わせタイプを見つけるヘルパーメソッド
    private int findNextAvailableAllyEnemyCombination(int... types) {
        for (int type : types) {
            if (!isTypePresentOnMap(type)) {
                return type;
            }
        }
        return TYPE_SPACE; // すべての組み合わせが使用中の場合は空白を返す
    }

    // 船の元の位置を空白に設定するメソッド
    public void clearPreviousShipLocation(int shipType) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                int currentType = getMap((char) ('A' + y), x);
                if (currentType == shipType) {
                    setMap((char) ('A' + y), x, MapData.TYPE_SPACE);
                } else if (isAllyEnemyCombinationType(currentType, shipType)) {
                    // 味方船と敵船の組み合わせの場合、敵船のみを残す
                    int enemyShipType = extractEnemyShipTypeFromCombination(currentType);
                    setMap((char) ('A' + y), x, enemyShipType);
                }
            }
        }
    }

    // 味方船と敵船の組み合わせタイプかどうかを判断するメソッド
    private boolean isAllyEnemyCombinationType(int mapType, int allyShipType) {
        if (mapType >= MapData.TYPE_AllyShip_SSN001_Enemy_A && mapType <= MapData.TYPE_AllyShip_SSN004_Enemy_D) {
            int baseType = (mapType - MapData.TYPE_AllyShip_SSN001_Enemy_A) / 4 + MapData.TYPE_AllyShip_SSN001;
            return baseType == allyShipType;
        }
        return false;
    }

    // 味方船と敵船の組み合わせタイプから敵船のタイプを抽出するメソッド
    private int extractEnemyShipTypeFromCombination(int combinationType) {
        int offset = (combinationType - MapData.TYPE_AllyShip_SSN001_Enemy_A) % 4;
        return MapData.TYPE_EnemyShip_A + offset;
    }

}