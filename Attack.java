import java.util.Scanner;

public abstract class Attack {
    protected MapData mapData;
    protected AllyShipManager allyShipManager;

    public Attack(MapData mapData, AllyShipManager allyShipManager) {
        this.mapData = mapData;
        this.allyShipManager = allyShipManager;
    }

    public abstract void executeAttack(int x, int y);
}

class AllyAttack extends Attack {
    private Scanner scanner;

    public AllyAttack(MapData mapData, AllyShipManager allyShipManager, Scanner scanner) {
        super(mapData, allyShipManager);
        this.scanner = scanner;
    }

    @Override
    public void executeAttack(int x, int y) {
        if (!isAttackRangeValid(x, y, mapData)) {
            System.out.println("攻撃範囲外です。攻撃できません。");
            return;
        }

        int targetType = mapData.getMap((char) ('A' + y), x);
        
        System.out.println("攻撃結果を入力してください(1: 命中, 2: 波高し, 3: ハズレ, 4: 撃沈, 5:ミス）:");
        int feedbackOption = scanner.nextInt();
        scanner.nextLine();

        switch (feedbackOption) {
            case 1:
                System.out.println("敵船に命中しました！");
                mapData.placeEnemyShip((char) ('A' + y), x);
                Algorithm.allyAttackAlgorithm(x, y,2);
                break;
            case 2:
                System.out.println("波高しです。");
                Algorithm.allyAttackAlgorithm(x,y,1);
                break;
            case 3:
                System.out.println("ハズレです。");
                Algorithm.allyAttackAlgorithm(x,y,0);
                break;
            case 4:
                System.out.println("敵船を撃沈しました!");
                Algorithm.allyAttackAlgorithm(x,y,3);
                mapData.setMap((char) ('A' + y), x,9);
            case 5:
                System.out.println("戻ります");
            default:
                System.out.println("無効な入力です。");
                break;
            }
        
    }

    public static boolean isAttackRangeValid(int x, int y, MapData mapData) {
        int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
        int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };

        for (int i = 0; i < dx.length; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx >= 0 && nx < mapData.getWidth() && ny >= 0 && ny < mapData.getHeight()) {
                int cellType = mapData.getMap((char) ('A' + ny), nx);
                if (cellType >= MapData.TYPE_AllyShip_SSN001 && cellType <= MapData.TYPE_AllyShip_SSN004) {
                    return true;
                }
            }
        }
        return false;
    }

}

// 敵からの攻撃を管理するクラス
class EnemyAttack extends Attack {
    public EnemyAttack(MapData mapData, AllyShipManager allyShipManager) {
        super(mapData, allyShipManager);
    }

    @Override
    public void executeAttack(int x, int y) {
        int targetType = mapData.getMap((char) ('A' + y), x);

        if (targetType >= MapData.TYPE_AllyShip_SSN001 && targetType <= MapData.TYPE_AllyShip_SSN004) {
            if(allyShipManager.getShipHp(targetType) <= 1){
                System.out.println("撃沈!");
                allyShipManager.updateShipHp(targetType, allyShipManager.getShipHp(targetType) - 1);
                mapData.setMap((char) ('A' + y), x,9);
                Algorithm.enemyAttackAlgorithm(x,y,3);
            }else{
                System.out.println("命中！");
                allyShipManager.updateShipHp(targetType, allyShipManager.getShipHp(targetType) - 1);
                Algorithm.enemyAttackAlgorithm(x,y,2);
            }
            
        } else {
            if(ditectNear(x,y) == true){
                System.out.println("波高し!");
                Algorithm.enemyAttackAlgorithm(x,y,1);
            }else{
                System.out.println("ハズレ!");
                Algorithm.enemyAttackAlgorithm(x,y,0);
            }
        }
    }

    public boolean ditectNear(int x, int y){

        boolean result = false;
        boolean isFinish = false;
        if(0 <= x-1 && x-1 <= 4 && 0 <= y+1 && y+1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y+1), x-1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y+1), x-1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= x-1 && x-1<= 4){
            if(mapData.getAllyMap((char) ('A' + y), x-1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y), x-1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }
        
        if(0 <= x-1 && x-1 <= 4 && 0 <= y-1 && y-1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y-1), x-1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y-1), x-1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= y-1 && y-1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y-1), x) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y-1), x) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= x+1 && x+1 <= 4 && 0 <= y-1 && y-1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y-1), x+1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y-1), x+1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= x+1 && x+1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y), x+1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y), x+1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= x+1 && x+1<= 4 && 0 <= y+1 && y+1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y+1), x+1) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y+1), x+1) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }

        if(0 <= y+1 && y+1 <= 4){
            if(mapData.getAllyMap((char) ('A' + y+1), x) >= MapData.TYPE_AllyShip_SSN001 && mapData.getAllyMap((char) ('A' + y+1), x) <= MapData.TYPE_AllyShip_SSN004){
                result = true;
                isFinish = true;
            }
        }
        if(isFinish == false){
            result = false;
        }
        return result;
    }
}
