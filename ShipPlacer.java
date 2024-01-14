
/*placeMultipleShipsRandomlyメソッド：

目的：複数の船を一度にマップ上に配置する。
使用場面：ゲーム開始時や新しいレベルで、複数の船を一括で配置する際に使用されます。
機能：異なる種類の船を順番に配置するためのループを含み、各船に対してplaceShipRandomlyメソッドを呼び出します。
placeShipRandomlyメソッド：

目的：特定の種類の船をランダムな位置に配置する。
使用場面：個々の船を特定の条件下で配置する必要がある場合に使用されます。
機能：ランダムな位置を生成し、その位置に船を配置できるかどうかをチェックした後、船を配置します。 */
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class ShipPlacer {
    private MapData mapData;
    private AllyShipManager allyShipManager;
    private static final int MAX_ATTEMPTS = 100; // 最大試行回数
    private boolean[][] attackCoverage; // 攻撃範囲のカバレッジを記録する配列
    private Random rand = new Random();

    // コンストラクタを修正
    public ShipPlacer(MapData mapData, AllyShipManager allyShipManager) {
        this.mapData = mapData;
        this.allyShipManager = allyShipManager;
        this.attackCoverage = new boolean[mapData.getHeight()][mapData.getWidth()];
    }

    // 全マスをカバーするように船を配置するメソッド
    public int placeShipsWithFullCoverage() {
        int attempts = 0;
        do {
            resetCoverage();
            placeShipsInSections();
            attempts++;
        } while (!isAllCovered() && attempts < MAX_ATTEMPTS);
        return attempts;
    }

    // グリッド分割に基づいて船を配置するメソッド
    private void placeShipsInSections() {
        int[] shipTypes = { MapData.TYPE_AllyShip_SSN001, MapData.TYPE_AllyShip_SSN002, MapData.TYPE_AllyShip_SSN003,
                MapData.TYPE_AllyShip_SSN004 };
        List<Integer> sections = getSections();

        for (int shipType : shipTypes) {
            if (!sections.isEmpty()) {
                int sectionIndex = rand.nextInt(sections.size());
                int section = sections.remove(sectionIndex);
                placeShipInSection(shipType, section);
            } else {
                placeShipRandomly(shipType);
            }
        }
    }

    // 特定のセクションに船を配置するメソッド
    private void placeShipInSection(int shipType, int section) {
        int sectionWidth = mapData.getWidth() / 2;
        int sectionHeight = mapData.getHeight() / 2;
        int x, y;

        do {
            x = section % 2 * sectionWidth + rand.nextInt(sectionWidth);
            y = section / 2 * sectionHeight + rand.nextInt(sectionHeight);
        } while (!canPlaceShip(x, y));

        // 元の船の位置を空白に設定
        clearPreviousShipLocation(shipType);

        // 新しい位置に船を配置
        mapData.setMap((char) ('A' + y), x, shipType);
        allyShipManager.initializeShips();
        markAttackRange(x, y);

        // AllyShipManagerに位置情報を更新
        allyShipManager.moveShip(shipType, new Point(x, y));
    }

    // 船をランダムに配置するメソッド
    private void placeShipRandomly(int shipType) {
        int x, y;
        do {
            x = rand.nextInt(mapData.getWidth());
            y = rand.nextInt(mapData.getHeight());
        } while (!canPlaceShip(x, y));

        // 元の船の位置を空白に設定
        clearPreviousShipLocation(shipType);

        // 新しい位置に船を配置
        mapData.setMap((char) ('A' + y), x, shipType);
        allyShipManager.initializeShips();
        markAttackRange(x, y);

        // AllyShipManagerに位置情報を更新
        allyShipManager.moveShip(shipType, new Point(x, y));
    }

    // 船の元の位置を空白に設定するメソッド
    private void clearPreviousShipLocation(int shipType) {
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (mapData.getMap((char) ('A' + y), x) == shipType) {
                    mapData.setMap((char) ('A' + y), x, MapData.TYPE_SPACE);
                }
            }
        }
    }

    // 指定された位置に船を配置できるかどうかをチェックするメソッド
    private boolean canPlaceShip(int x, int y) {
        return mapData.getMap((char) ('A' + y), x) == MapData.TYPE_SPACE;
    }

    // 船の周囲8マスと船自体を攻撃範囲としてマークするメソッド
    private void markAttackRange(int x, int y) {
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1, 0 }; // 船自体を含むため、0を追加
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1, 0 }; // 船自体を含むため、0を追加

        for (int i = 0; i < dx.length; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < mapData.getWidth() && ny >= 0 && ny < mapData.getHeight()) {
                attackCoverage[ny][nx] = true;
            }
        }
    }

    // 全マスがカバーされているかを確認するメソッド
    private boolean isAllCovered() {
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (!attackCoverage[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 攻撃範囲のカバレッジをリセットするメソッド
    private void resetCoverage() {
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                attackCoverage[y][x] = false;
            }
        }
    }

    // グリッドセクションを取得するメソッド
    private List<Integer> getSections() {
        List<Integer> sections = new ArrayList<>();
        int sectionRows = 2; // セクションの行数
        int sectionCols = 2; // セクションの列数
        for (int i = 0; i < sectionRows * sectionCols; i++) {
            sections.add(i);
        }
        return sections;
    }

}
