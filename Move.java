/* このクラスの説明
 * class Move は、潜水艦の移動を管理するクラス
 * 潜水艦の現在位置を保持し、指定された方向とステップ数に基づいて移動を行う
 * 
 * 潜水艦が移動する際の使用例
 * Move move = new Move(0, 0, mapData); // 潜水艦の初期位置を(0, 0)としてMoveのインスタンスを作成
 * move.moveInDirection("東", 3); // 潜水艦を東に3マス移動させる
 * move.moveInDirection("南", 2); // 潜水艦を南に2マス移動させる


 * 主要なメソッドと機能:
 * - moveInDirection(String direction, int steps): 指定された方向に指定されたステップ数だけ移動する
 * - canMoveTo(Point newLocation): 移動先が有効かどうかをチェックする
 * - moveTo(Point newLocation): 船を新しい位置に移動する
 * - getCurrentLocation(): 現在位置を取得する
 * 
 * 以下メモ:
 * - 移動先がマップの範囲内で、障害物や味方の船がない場合にのみ移動可能です。
 * - 敵の船に関する衝突判定は行いません。
 * - 移動先が無効な場合は移動は行われず、適切なメッセージが表示されます。
 */

import java.awt.Point;

public class Move {
    private Point currentLocation;
    private MapData mapData; // マップデータへの参照
    private AllyShipManager allyShipManager;
    int nowshipType;
    public static boolean isMoved;

    // コンストラクタを修正
    public Move(int shipType, AllyShipManager allyShipManager, MapData mapData) {
        // 指定された船の現在位置を取得
        this.allyShipManager = allyShipManager;
        this.nowshipType = shipType;
        Point shipLocation = allyShipManager.findShipLocation(shipType);

        // 指定された船の位置を初期位置として設定
        if (shipLocation != null) {
            this.currentLocation = shipLocation;
        } else {
            // 船が見つからない場合、デフォルト位置を設定
            this.currentLocation = new Point(0, 0);
        }
        this.mapData = mapData;
    }

    // 東西南北に指定されたマス数だけ移動するメソッド
    public void moveInDirection(String direction, int steps) {
        Point newLocation = null;
        isMoved = false;

        switch (direction.trim()) {
            // case "東":
            case "e":
                newLocation = new Point(currentLocation.x + steps, currentLocation.y);
                break;
            // case "西":
            case "w":
                newLocation = new Point(currentLocation.x - steps, currentLocation.y);
                break;
            // case "南":
            case "s":
                newLocation = new Point(currentLocation.x, currentLocation.y + steps);
                break;
            // case "北":
            case "n":
                newLocation = new Point(currentLocation.x, currentLocation.y - steps);
                break;
            default:
                System.out.println("無効な方向です。");
                return;
        }

        if (canMoveTo(newLocation) == true) {
            moveTo(newLocation);
            isMoved = true;
            System.out.println(newLocation);
        } else {
            System.out.println("移動できません。");
            System.out.println(newLocation);
            isMoved = false;
        }
    }

    // 移動先が有効かどうかをチェックするメソッド
    public boolean canMoveTo(Point newLocation) {
        // 移動先がマップの範囲内にあるかチェック
        if (!isInRange(newLocation)) {
            return false;
        }

        // 移動先に障害物がないかチェック
        int mapType = mapData.getMap((char) ('A' + newLocation.y), newLocation.x);
        if (mapType == MapData.TYPE_debris) {
            return false; // 障害物がある場合は移動不可
        }

        // 移動先に味方の船がないかチェック
        if (mapType >= MapData.TYPE_AllyShip_SSN001 && mapType <= MapData.TYPE_AllyShip_SSN004) {
            return false; // 味方の船がある場合は移動不可
        }

        // 敵の船に関する衝突判定は行わない
        return true; // 上記の条件をすべてクリアした場合、移動可能
    }

    // 指定された座標がマップの範囲内にあるかどうかをチェックするメソッド
    private boolean isInRange(Point point) {
        return point.x >= 0 && point.x < mapData.getWidth() && point.y >= 0 && point.y < mapData.getHeight();
    }

    public void moveTo(Point newLocation) {
        if (canMoveTo(newLocation)) {
            // 元の位置の船（および敵船）をクリア
            mapData.clearPreviousShipLocation(nowshipType);
    
            // 移動先が有効な場合、現在位置を更新
            currentLocation = newLocation;
    
            // 新しい位置に船を設定
            mapData.setMap((char) ('A' + newLocation.y), newLocation.x, nowshipType);
    
            // AllyShipManagerに位置情報を更新
            allyShipManager.moveShip(nowshipType, currentLocation);
        }
    }

    // 現在位置を取得するメソッド
    public Point getCurrentLocation() {
        return currentLocation;
    }

}