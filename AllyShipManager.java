/* このクラスの説明
 * class AllyShipManager は、ゲームのマップ上に配置された味方の船を管理するためのクラス
 *  
 * 主要なメソッドと機能:
 * - コンストラクタ (public AllyShipManager(MapData mapData)): MapDataオブジェクトを受け取り、内部で船の管理に必要なデータ構造を初期化する
 * - initializeShips():マップ上のすべての味方の船を初期化し、それぞれにHPを割り当てます。船はマップ上の特定のタイプに基づいて識別する
 * - boolean isAllyShip(int type):与えられたタイプが味方の船かどうかを判定するヘルパーメソッド
 * - updateShipHp(int shipType, int newHp):特定の船のHPを更新するメソッド
 * - moveShip(int shipType, Point newLocation):特定の船を新しい位置に移動させるメソッド
 * - Point findShipLocation(int shipType):特定の船の現在位置を探すヘルパーメソッド
 * - getShipHp(int shipType):特定の船のHPを取得するメソッド
 * - displayAllShipsHp():すべての味方船のHPを表示するメソッド
 * 
 * 使用例:
 *   - MapData mapData = new MapData();
 *   - AllyShipManager allyShipManager = new AllyShipManager(mapData);
 *   - // 船の配置、移動、HPの更新などの操作を行う
 *   - allyShipManager.displayAllShipsHp(); // すべての船のHPを表示
 * 
 */

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class AllyShipManager {
    private MapData mapData;
    private Map<Integer, Ship> ships; // 船のIDと船オブジェクトのマッピング

    public AllyShipManager(MapData mapData) {
        this.mapData = mapData;
        this.ships = new HashMap<>();
        initializeShips();
    }

    // マップ上の味方の船を初期化し、それぞれのHPと位置を設定する

    public void initializeShips() {
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                int shipType = mapData.getMap((char) ('A' + y), x);
                if (isAllyShip(shipType)) {
                    // x は列、y は行に対応
                    Point location = new Point(x, y);
                    ships.put(shipType, new Ship(3, location)); // 初期HPを3に設定し、位置を設定
                }
            }
        }
    }
    

    // 指定されたタイプが味方の船かどうかを判定する
    protected boolean isAllyShip(int type) {
        return type >= MapData.TYPE_AllyShip_SSN001 && type <= MapData.TYPE_AllyShip_SSN004;
    }

    // 船のHPを更新する
    public void updateShipHp(int shipType, int newHp) {
        if (ships.containsKey(shipType)) {
            ships.get(shipType).setHp(newHp);
        }
    }

    // 船の移動を処理する
    public void moveShip(int shipType, Point newLocation) {
        if (ships.containsKey(shipType)) {
            Ship ship = ships.get(shipType);
            // 古い位置のマップタイプを空白に設定
            Point oldLocation = ship.getLocation();
            mapData.setMap((char) ('A' + oldLocation.y), oldLocation.x, MapData.TYPE_SPACE);

            // 新しい位置に船を設定
            mapData.setMap((char) ('A' + newLocation.y), newLocation.x, shipType);
            ship.setLocation(newLocation); // 船の位置情報を更新
        }
    }

    // 船の現在位置を探す
    public Point findShipLocation(int shipType) {
        return ships.containsKey(shipType) ? ships.get(shipType).getLocation() : null;
    }

    // 特定の船のHPを取得するメソッド
    public int getShipHp(int shipType) {
        Ship ship = ships.get(shipType);
        return ship != null ? ship.getHp() : -1; // 船が存在しない場合は-1を返す
    }

    // 特定の船のHPを設定するメソッド(Undoのために後から作成)
    public void setShipHp(int shipType, int newHp) {
        if (ships.containsKey(shipType)) {
            Ship ship = ships.get(shipType);
            ship.setHp(newHp); // 船オブジェクトのHPを更新
        }
    }

    // すべての味方船のHPを表示するメソッド
    public void displayAllShipsHp() {
        for (int shipType = MapData.TYPE_AllyShip_SSN001; shipType <= MapData.TYPE_AllyShip_SSN004; shipType++) {
            System.out.println("SSN-" + (shipType - MapData.TYPE_AllyShip_SSN001 + 1) + "のHP: " + getShipHp(shipType));
        }
    }
}

class Ship {
    private int hp;
    private Point location; // java.awt.Point を使用

    public Ship(int hp, Point location) {
        this.hp = hp;
        this.location = location;
    }

    // 船のHPを取得するメソッド
    public int getHp() {
        return hp;
    }

    // 船のHPを設定するメソッド
    public void setHp(int hp) {
        this.hp = hp;
    }

    // 船の位置を取得するメソッド
    public Point getLocation() {
        return location;
    }

    // 船の位置を設定するメソッド
    public void setLocation(Point location) {
        this.location = location;
    }
}
