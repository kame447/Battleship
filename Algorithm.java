import java.awt.Point;
import java.util.Random;

public class Algorithm {
    private static Point currentLocation;
    private static MapData mapData;
    private static AllyShipManager allyShipManager;
    private static Move move;
    public static int enemyAlgorithmChecker = 0;
    public static int enemyAttackAlgorithmChecker = 0;
    public static int allyContinuousHitChecker = 0;
    public static int enemyContinuousHitChecker = 0;
    public static boolean isFinishFormation = false;
    public static boolean isNextAttack = false;
    public static boolean isRockOn = false;
    public static int[] forecastEnemyX = new int[25];
    public static int[] forecastEnemyY = new int[25];
    public static int numberOfRockOn = 0;
    public static int surchPattern = 0;

    Algorithm(MapData mapData,AllyShipManager allyShipManager){
        this.mapData = mapData;
        this.allyShipManager = allyShipManager;
    }
    
    

    public static void allyAttackAlgorithm(int allyAttackX,int allyAttackY,int attackResult){

        //attackResult == 0はハズレ,attackResult == 1 は波高し,attackResult == 2はヒット,attackResult == 3は撃沈を意味する

        //こちらの攻撃がハズレのとき
        if (attackResult == 0){

            //攻撃座標とその周辺のEFを0にする

            Forecast.setEF(allyAttackX,allyAttackY,0);
            Forecast.setCircleEF(allyAttackX,allyAttackY,0);

            //連続ヒット回数を0に設定
            allyContinuousHitChecker = 0;

            
        }

        //こちらの攻撃が波高しのとき
        if(attackResult == 1){

            //攻撃座標の周辺8マスのEFを+1する

            Forecast.plusCircleEF(allyAttackX,allyAttackY,1);

            //攻撃座標のEFを0にする

            Forecast.setEF(allyAttackX,allyAttackY,0);

            //連続ヒット回数を0に設定
            allyContinuousHitChecker = 0;
        }

        //こちらの攻撃がヒットの場合
        if(attackResult == 2){

            allyContinuousHitChecker = allyContinuousHitChecker +1;

            //敵のアルゴリズムを解析開始、敵が被弾後すぐ逃げるアルゴリズムだと分かったらenemyAlgorithmCheckerを1にする
            //連続ヒット回数をenemyContiumousHitCheckerに記録し逃げるかどうか判断する

            if(allyContinuousHitChecker > 1){
                //敵のアルゴリズムが被弾後すぐ逃げないものだと判明したため連続攻撃続行
                enemyAlgorithmChecker = 2;
            }

            if(enemyAlgorithmChecker == 1){
                //攻撃座標の周辺8マスのEFを+1する

                Forecast.plusCircleEF(allyAttackX,allyAttackY,1);
            }

            if(enemyAlgorithmChecker == 0){

                //ヒットした座標のEFを最大値に設定し、連続攻撃
                for(int i = 0; i <= 4 ;i++){
                    for(int j = 0; j <= 4;j++){
                        if(Forecast.getEF(i,j) > Forecast.getEF(allyAttackX,allyAttackY)){
                            Forecast.setEF(allyAttackX,allyAttackY,Forecast.getEF(i,j)+1);
                        }
                    }
                }
                enemyAlgorithmChecker = 1;
            }


            if(enemyAlgorithmChecker == 2){

                //ヒットした座標のEFを最大値に設定し、連続攻撃
                for(int i = 0; i <= 4 ;i++){
                    for(int j = 0; j <= 4;j++){
                        if(Forecast.getEF(i,j) > Forecast.getEF(allyAttackX,allyAttackY)){
                            Forecast.setEF(allyAttackX,allyAttackY,Forecast.getEF(i,j)+1);
                        }
                    }
                }
            }
            
        }

        //こちらの攻撃が撃沈の場合
        if(attackResult == 3){

            //攻撃座標のEFを0に設定
            Forecast.setEF(allyAttackX, allyAttackY, 0);
            

        }

    }

    //enemyAttackAlgorithm実装のイメージ
    //敵から攻撃座標の指示が入る(口頭で攻撃座標のx,yを教えてもらう)
    //まずEnemyAttack.executeAttack(x,y)実行
    //その後enemyAttackAlgorithm(x,y)実行
    
    public static void enemyAttackAlgorithm(int enemyAttackX,int enemyAttackY,int attackResult){

        //attackResult == 0はハズレ,attackResult == 1 は波高し,attackResult == 2はヒット,attackResult == 3は撃沈を意味する

        if(attackResult == 0){
            //攻撃座標の周辺8マスのEFを+1する

            Forecast.plusCircleEF(enemyAttackX,enemyAttackY,1);
            System.out.println("【自軍の動作】");
            allyAction();
            enemyContinuousHitChecker = 0;

        }

        if(attackResult == 1){
            //攻撃座標の周辺8マスのEFを+1する

            Forecast.plusCircleEF(enemyAttackX,enemyAttackY,1);
            System.out.println("【自軍の動作】");
            allyAction();
            enemyContinuousHitChecker = 0;
        }

        if(attackResult == 2){
            //攻撃座標の周辺8マスのEFを+1する

            Forecast.plusCircleEF(enemyAttackX,enemyAttackY,1);

            //ここで敵のアルゴリズムを解析
            //初回の被弾結果から我々のアルゴリズムを変化させる
            //敵のアルゴリズムが命中後連続攻撃を行うものであればenemyAttackAlgorithmCheckerを1にして被弾後即逃走アルゴリズムに
            //敵が連続攻撃を行わないのであれば攻撃継続アルゴリズムに
            enemyContinuousHitChecker++;
            System.out.println("連続被弾回数:" + enemyContinuousHitChecker);

            if(enemyContinuousHitChecker > 1 || enemyAttackAlgorithmChecker == 1){
                enemyAttackAlgorithmChecker = 1;
            }else{
                isNextAttack = true;
                System.out.println("【自軍の動作】");
                System.out.println("find");
                allyAction();
            }

            if(enemyAttackAlgorithmChecker == 1){
                int moveStopper = 0;
                int moveChenger = 0;

                //逃走
                move = new Move(mapData.getAllyMap((char) ('A' + enemyAttackY), enemyAttackX),allyShipManager,mapData);
                if(moveChenger == 0){
                    if(enemyAttackY >= 1 && AllyAttack.isAttackRangeValid(enemyAttackX,enemyAttackY-1,mapData) && moveStopper == 0){
                        move.moveInDirection("n",1);
                        if(move.isMoved == true){
                            System.out.println("潜水艦を北に1移動!");
                            moveStopper = 1;
                            moveChenger = 1;
                        }else{
                            move.moveInDirection("s",1);
                            if(move.isMoved == true){
                                System.out.println("潜水艦を南に1移動!");
                                moveStopper = 1;
                                moveChenger = 1;
                            }else{
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                            }else{
                                move.moveInDirection("w",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を西に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                                }else{
                                startAttackAlgorithm();
                            }
                        }
                    }
                    }
                    if(enemyAttackY <= 3 && AllyAttack.isAttackRangeValid(enemyAttackX,enemyAttackY+1,mapData) && moveStopper == 0){
                        move.moveInDirection("n",1);
                        if(move.isMoved == true){
                            System.out.println("潜水艦を北に1移動!");
                            moveStopper = 1;
                            moveChenger = 1;
                        }else{
                            move.moveInDirection("s",1);
                            if(move.isMoved == true){
                                System.out.println("潜水艦を南に1移動!");
                                moveStopper = 1;
                                moveChenger = 1;
                            }else{
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                            }else{
                                move.moveInDirection("w",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を西に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                                }else{
                                startAttackAlgorithm();
                            }
                        }
                    }
                    }
                    }
                }else{
                    if(enemyAttackX >= 1 && AllyAttack.isAttackRangeValid(enemyAttackX-1,enemyAttackY,mapData) && moveStopper == 0){
                        move.moveInDirection("n",1);
                        if(move.isMoved == true){
                            System.out.println("潜水艦を北に1移動!");
                            moveStopper = 1;
                            moveChenger = 1;
                        }else{
                            move.moveInDirection("s",1);
                            if(move.isMoved == true){
                                System.out.println("潜水艦を南に1移動!");
                                moveStopper = 1;
                                moveChenger = 1;
                            }else{
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                            }else{
                                move.moveInDirection("w",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を西に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                                }else{
                                startAttackAlgorithm();
                            }
                        }
                    }
                    }
                    }
                    if(enemyAttackY <= 3 && AllyAttack.isAttackRangeValid(enemyAttackX+1,enemyAttackY,mapData) && moveStopper == 0){
                        move.moveInDirection("n",1);
                        if(move.isMoved == true){
                            System.out.println("潜水艦を北に1移動!");
                            moveStopper = 1;
                            moveChenger = 1;
                        }else{
                            move.moveInDirection("s",1);
                            if(move.isMoved == true){
                                System.out.println("潜水艦を南に1移動!");
                                moveStopper = 1;
                                moveChenger = 1;
                            }else{
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                            }else{
                                move.moveInDirection("w",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を西に1移動!");
                                    moveStopper = 1;
                                    moveChenger = 1;
                                }else{
                                startAttackAlgorithm();
                            }
                        }
                    }
                    }
                    }
                }
            }
                
                //陣形構築アルゴリズムを再起動
                isFinishFormation = false;
                //攻撃解除
                isNextAttack = false;
            }
        }

        if(attackResult == 3){
            //攻撃座標の周辺8マスのEFを+1する

            Forecast.plusCircleEF(enemyAttackX,enemyAttackY,1);
            System.out.println("【自軍の動作】");
            allyAction();
        }
        
    }

    //攻撃や移動など、こちらの動作を行うメソッド
    public static void allyAction(){

        int moveDirection = 0;
        int attack = 0;
        boolean isAction = false;
        if(isFinishFormation){
            attack = 1;
        }else{
                    //陣形を作れていなければ移動、作れていれば攻撃
        //二回に一回以上は攻撃
        if(isNextAttack){
            //移動の次は強制的に攻撃
            attack = 1;
            //強制攻撃の次は移動と攻撃どちらも可能
            isNextAttack = false;
        }else{
            //陣形が作られているか調べ、できていない部分に潜水艦を移動させる
            //複数の検索形態を用意
            if(surchPattern == 0){
                if(mapData.getAllyMap('B',2) == 1 ||mapData.getAllyMap('B',2) == 2 ||mapData.getAllyMap('B',2) == 3 ||mapData.getAllyMap('B',2) == 4 ){
                    if(mapData.getAllyMap('B',4) == 1 ||mapData.getAllyMap('B',4) == 2 ||mapData.getAllyMap('B',4) == 3 || mapData.getAllyMap('B',4) == 4 ){
                        if(mapData.getAllyMap('D',2) == 1 ||mapData.getAllyMap('D',2) == 2 ||mapData.getAllyMap('D',2) == 3 || mapData.getAllyMap('D',2) == 4 ){
                            if(mapData.getAllyMap('D',4) == 1 ||mapData.getAllyMap('D',4) == 2 ||mapData.getAllyMap('D',4) == 3 || mapData.getAllyMap('D',4) == 4 ){
                                isFinishFormation = true;
                                attack = 1;
                            }else{
                                moveDirection = 4;
                                // System.out.println("D4");
                            }
                        }else{
                            moveDirection = 3;
                            // System.out.println("D2");
                        }
                    }else{
                        moveDirection = 2;
                        // System.out.println("B4");
                    }   
        
                }else{
                    moveDirection = 1;
                    // System.out.println("B2");
                }
            }else{
                if(mapData.getAllyMap('D',2) == 1 ||mapData.getAllyMap('D',2) == 2 ||mapData.getAllyMap('D',2) == 3 ||mapData.getAllyMap('D',2) == 4 ){
                    if(mapData.getAllyMap('D',4) == 1 ||mapData.getAllyMap('D',4) == 2 ||mapData.getAllyMap('D',4) == 3 || mapData.getAllyMap('D',4) == 4 ){
                        if(mapData.getAllyMap('B',2) == 1 ||mapData.getAllyMap('B',2) == 2 ||mapData.getAllyMap('B',2) == 3 || mapData.getAllyMap('B',2) == 4 ){
                            if(mapData.getAllyMap('B',4) == 1 ||mapData.getAllyMap('B',4) == 2 ||mapData.getAllyMap('B',4) == 3 || mapData.getAllyMap('B',4) == 4 ){
                                isFinishFormation = true;
                                attack = 1;
                            }else{
                                moveDirection = 4;
                                // System.out.println("B4");
                            }
                        }else{
                            moveDirection = 3;
                            // System.out.println("B2");
                        }
                    }else{
                        moveDirection = 2;
                        // System.out.println("D4");
                    }   
        
                }else{
                    moveDirection = 1;
                    // System.out.println("D2");
                }
            }
            
            
            if(moveDirection == 1){
                //B2に最寄りの潜水艦を移動させる
                moveDirection = 0;
                int targetX = 0;
                int targetY = 0;
                int surchStopper;
                int moveStopper;
                int targetShip = 0;

                //検索開始
                surchStopper = 0;
    
                //B2に最も近い潜水艦を検索
                for(int x = 0;x <= 4 && surchStopper < 1;x++){

                    for(int y = 0;y <=4 && surchStopper < 1;y++){
                        //yがintだと検索できないのでアルファベット化
                        if(y == 0){
                            if(mapData.getAllyMap('A',x) == 1||mapData.getAllyMap('A',x) == 2||mapData.getAllyMap('A',x) == 3||mapData.getAllyMap('A',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('A',x);
                            }
                        }
                        if(y == 1){
                            if(mapData.getAllyMap('B',x) == 1||mapData.getAllyMap('B',x) == 2||mapData.getAllyMap('B',x) == 3||mapData.getAllyMap('B',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('B',x);
                            }
                        }
                        if(y == 2){
                            if(mapData.getAllyMap('C',x) == 1||mapData.getAllyMap('C',x) == 2||mapData.getAllyMap('C',x) == 3||mapData.getAllyMap('C',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('C',x);
                            }
                        }
        
                        if(y == 3){
                            if(mapData.getAllyMap('D',x) == 1||mapData.getAllyMap('D',x) == 2||mapData.getAllyMap('D',x) == 3||mapData.getAllyMap('D',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('D',x);
                            }
                        }
                        if(y == 4){
                            if(mapData.getAllyMap('E',x) == 1||mapData.getAllyMap('E',x) == 2||mapData.getAllyMap('E',x) == 3||mapData.getAllyMap('E',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('E',x);
                            }
                        }
                    }

                }
                moveStopper = 0;
                System.out.println("ShipType:"+targetShip);
                //適切な方向に移動
                //moveStopperは一度に複数移動してしまうのを防止するために使用
                if(targetX < 1 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",1);
                    System.out.println("潜水艦を東に1移動!");
                    moveStopper = 1;
                }
                if(targetX == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",1);
                    System.out.println("潜水艦を西に1移動!");
                    moveStopper = 1;
                }
                if(targetX > 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",2);
                    System.out.println("潜水艦を西に2移動!");
                    moveStopper = 1;
                }
                if(targetY < 1 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",1);
                    System.out.println("潜水艦を南に1移動!");
                    moveStopper = 1;
                }
                if(targetY == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",1);
                    System.out.println("潜水艦を北に1移動!");
                    moveStopper = 1;
                }
                if(targetY > 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",2);
                    System.out.println("潜水艦を北に2移動!");
                    moveStopper = 1;
                }
                
                if(moveStopper == 0){
                    //陣形はできているので攻撃
                    isFinishFormation = true;
                    moveDirection = 2;
                    
                }
                //次は攻撃するように指示
                isNextAttack = true;
            }
            if(moveDirection == 2){
                //B4に最寄りの潜水艦を移動させる
                int targetX = 0;
                int targetY = 0;
                int surchStopper;
                int moveStopper;
                int targetShip = 0;

                //検索開始
                surchStopper = 0;
    
                //B4に最も近い潜水艦を検索
                for(int x = 4;x >= 0 && surchStopper < 1;x--){

                    for(int y = 0;y <=4 && surchStopper < 1;y++){
                        //yがintだと検索できないのでアルファベット化
                        if(y == 0){
                            if(mapData.getAllyMap('A',x) == 1||mapData.getAllyMap('A',x) == 2||mapData.getAllyMap('A',x) == 3||mapData.getAllyMap('A',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('A',x);
                            }
                        }
                        if(y == 1){
                            if(mapData.getAllyMap('B',x) == 1||mapData.getAllyMap('B',x) == 2||mapData.getAllyMap('B',x) == 3||mapData.getAllyMap('B',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('B',x);
                            }
                        }
                        if(y == 2){
                            if(mapData.getAllyMap('C',x) == 1||mapData.getAllyMap('C',x) == 2||mapData.getAllyMap('C',x) == 3||mapData.getAllyMap('C',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('C',x);
                            }
                        }
        
                        if(y == 3){
                            if(mapData.getAllyMap('D',x) == 1||mapData.getAllyMap('D',x) == 2||mapData.getAllyMap('D',x) == 3||mapData.getAllyMap('D',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('D',x);
                            }
                        }
                        if(y == 4){
                            if(mapData.getAllyMap('E',x) == 1||mapData.getAllyMap('E',x) == 2||mapData.getAllyMap('E',x) == 3||mapData.getAllyMap('E',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('E',x);
                            }
                        }
                    }

                }
                moveStopper = 0;
                System.out.println("ShipType:"+targetShip);
                //適切な方向に移動
                //moveStopperは一度に複数移動してしまうのを防止するために使用
                if(targetX < 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",1);
                    System.out.println("潜水艦を東に2移動!");
                    moveStopper = 1;
                }
                if(targetX == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",1);
                    System.out.println("潜水艦を東に1移動!");
                    moveStopper = 1;
                }
                if(targetX > 3 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",2);
                    System.out.println("潜水艦を西に1移動!");
                    moveStopper = 1;
                }
                if(targetY < 1 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",1);
                    System.out.println("潜水艦を南に1移動!");
                    moveStopper = 1;
                }
                if(targetY == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",1);
                    System.out.println("潜水艦を北に1移動!");
                    moveStopper = 1;
                }
                if(targetY > 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",2);
                    System.out.println("潜水艦を北に2移動!");
                    moveStopper = 1;
                }
                
                if(moveStopper == 0){
                    //陣形はできているので攻撃
                    attack =1;
                    isFinishFormation = true;
                }
                //次は攻撃するように指示
                isNextAttack = true;
            }
            if(moveDirection == 3){
                //D2に最寄りの潜水艦を移動させる
                int targetX = 0;
                int targetY = 0;
                int surchStopper;
                int moveStopper;
                int targetShip = 0;

                //検索開始
                surchStopper = 0;
    
                //D2に最も近い潜水艦を検索
                for(int x = 0;x <= 4 && surchStopper < 1;x++){

                    for(int y = 4;y >=0 && surchStopper < 1;y--){
                        //yがintだと検索できないのでアルファベット化
                        if(y == 0){
                            if(mapData.getAllyMap('A',x) == 1||mapData.getAllyMap('A',x) == 2||mapData.getAllyMap('A',x) == 3||mapData.getAllyMap('A',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('A',x);
                            }
                        }
                        if(y == 1){
                            if(mapData.getAllyMap('B',x) == 1||mapData.getAllyMap('B',x) == 2||mapData.getAllyMap('B',x) == 3||mapData.getAllyMap('B',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('B',x);
                            }
                        }
                        if(y == 2){
                            if(mapData.getAllyMap('C',x) == 1||mapData.getAllyMap('C',x) == 2||mapData.getAllyMap('C',x) == 3||mapData.getAllyMap('C',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('C',x);
                            }
                        }
        
                        if(y == 3){
                            if(mapData.getAllyMap('D',x) == 1||mapData.getAllyMap('D',x) == 2||mapData.getAllyMap('D',x) == 3||mapData.getAllyMap('D',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('D',x);
                            }
                        }
                        if(y == 4){
                            if(mapData.getAllyMap('E',x) == 1||mapData.getAllyMap('E',x) == 2||mapData.getAllyMap('E',x) == 3||mapData.getAllyMap('E',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('E',x);
                            }
                        }
                    }

                }
                moveStopper = 0;
                System.out.println("ShipType:"+targetShip);
                //適切な方向に移動
                //moveStopperは一度に複数移動してしまうのを防止するために使用
                if(targetX < 1 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",1);
                    System.out.println("潜水艦を東に1移動!");
                    moveStopper = 1;
                }
                if(targetX == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",1);
                    System.out.println("潜水艦を西に1移動!");
                    moveStopper = 1;
                }
                if(targetX > 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",2);
                    System.out.println("潜水艦を西に2移動!");
                    moveStopper = 1;
                }
                if(targetY < 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",2);
                    System.out.println("潜水艦を南に2移動!");
                    moveStopper = 1;
                }
                if(targetY == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",1);
                    System.out.println("潜水艦を南に1移動!");
                    moveStopper = 1;
                }
                if(targetY > 3 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",1);
                    System.out.println("潜水艦を北に1移動!");
                    moveStopper = 1;
                }
                if(moveStopper == 0){
                    //陣形はできているので攻撃
                    attack =1;
                    isFinishFormation = true;
                }
                //次は攻撃するように指示
                isNextAttack = true;
            }
            if(moveDirection == 4){
                //D4に最寄りの潜水艦を移動させる
                int targetX = 0;
                int targetY = 0;
                int surchStopper;
                int moveStopper;
                int targetShip = 0;

                //検索開始
                surchStopper = 0;
    
                //D4に最も近い潜水艦を検索
                for(int x = 4;x >= 4 && surchStopper < 1;x--){

                    for(int y = 4;y >=4 && surchStopper < 1;y--){
                        //yがintだと検索できないのでアルファベット化
                        if(y == 0){
                            if(mapData.getAllyMap('A',x) == 1||mapData.getAllyMap('A',x) == 2||mapData.getAllyMap('A',x) == 3||mapData.getAllyMap('A',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('A',x);
                            }
                        }
                        if(y == 1){
                            if(mapData.getAllyMap('B',x) == 1||mapData.getAllyMap('B',x) == 2||mapData.getAllyMap('B',x) == 3||mapData.getAllyMap('B',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('B',x);
                            }
                        }
                        if(y == 2){
                            if(mapData.getAllyMap('C',x) == 1||mapData.getAllyMap('C',x) == 2||mapData.getAllyMap('C',x) == 3||mapData.getAllyMap('C',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('C',x);
                            }
                        }
        
                        if(y == 3){
                            if(mapData.getAllyMap('D',x) == 1||mapData.getAllyMap('D',x) == 2||mapData.getAllyMap('D',x) == 3||mapData.getAllyMap('D',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('D',x);
                            }
                        }
                        if(y == 4){
                            if(mapData.getAllyMap('E',x) == 1||mapData.getAllyMap('E',x) == 2||mapData.getAllyMap('E',x) == 3||mapData.getAllyMap('E',x) == 4){
                                //検索結果を記録
                                targetX = x;
                                targetY = y;
                                surchStopper =1;
                                targetShip = mapData.getAllyMap('E',x);
                            }
                        }
                    }

                }
                moveStopper = 0;
                System.out.println("ShipType:"+targetShip);
                //適切な方向に移動
                //moveStopperは一度に複数移動してしまうのを防止するために使用
                if(targetX < 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",1);
                    System.out.println("潜水艦を東に1移動!");
                    moveStopper = 1;
                }
                if(targetX == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("e",2);
                    System.out.println("潜水艦を東に2移動!");
                    moveStopper = 1;
                }
                if(targetX > 3 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("w",1);
                    System.out.println("潜水艦を西に1移動!");
                    moveStopper = 1;
                }
                if(targetY < 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",2);
                    System.out.println("潜水艦を南に2移動!");
                    moveStopper = 1;
                }
                if(targetY == 2 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("s",1);
                    System.out.println("潜水艦を南に1移動!");
                    moveStopper = 1;
                }
                if(targetY > 3 && moveStopper == 0 ){
                    move = new Move(targetShip,allyShipManager,mapData);
                    move.moveInDirection("n",1);
                    System.out.println("潜水艦を北に1移動!");
                    moveStopper = 1;
                }
                if(moveStopper == 0){
                    //陣形はできているので攻撃
                    attack =1;
                    isFinishFormation = true;
                }
                //次は攻撃するように指示
                isNextAttack = true;
            }
        }
        }

        
        if(attack ==1){
            //攻撃実行

            //EFが最大である地点を検索
            int maxEF = 0;
            int maxX = 0;
            int maxY = 0;
            int trueMaxX = 0;
            int trueMaxY = 0;
            int trueMaxEF = 0;
            boolean isRetry = true;
            boolean isMove = false;

            //ロックオンしているかどうか判断
            if(isRockOn == true){
                for(int i = 0;i <= numberOfRockOn && isRetry == true;i++){
                    if(AllyAttack.isAttackRangeValid(forecastEnemyX[i],forecastEnemyY[i],mapData)){
                        //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                        forecastEnemyX[i] = forecastEnemyX[i] + 1;
                    if( forecastEnemyY[i] == 0){
                        System.out.println("A"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 1){
                        System.out.println("B"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 2){
                        System.out.println("C"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 3){
                        System.out.println("D"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 4){
                        System.out.println("E"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    }
                }
                //ロックオンしたマスがすべて攻撃範囲外だった場合
                if(isRetry == true){

                    for(int x = 0;x <= 4;x++){
                        for(int y = 0;y <=4;y++){
                            if(Forecast.getEF(x,y) >=  maxEF){
                                //最大値を更新
                                trueMaxX = x;
                                trueMaxY = y; 
                                trueMaxEF = Forecast.getEF(x,y);
                                //攻撃可能なら記録
                                if(AllyAttack.isAttackRangeValid(x,y,mapData)){
                                    maxX = x;
                                    maxY = y;
                                    maxEF = Forecast.getEF(x,y);
                                }
                                
                                }
                            }
                        }
                        //自軍と敵が完全に同じ座標に居て攻撃できない可能性が高いときには1マス移動
                        if(Forecast.getEF(trueMaxX,trueMaxY) > 4){
                            int shipType = mapData.getMap((char) ('A' + trueMaxY), trueMaxX);
                            Move move = new Move(shipType,allyShipManager,mapData);
                            if(trueMaxX == 1 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY - 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                                move.moveInDirection("n",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を北に1移動!");
                                    isMove = true;
                                    isFinishFormation = false;
                                    isNextAttack = true;
                                }else{
                                    move.moveInDirection("s",1);
                                    if(move.isMoved == true){
                                        System.out.println("潜水艦を南に1移動!");
                                        isMove = true;
                                        isFinishFormation = false;
                                        isNextAttack = true;
                                    }else{
                                        move.moveInDirection("e",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を東に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                    }else{
                                        move.moveInDirection("w",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を西に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                        }else{
                                        
                                    }
                                }
                            }
                            }
                            }
                            if(trueMaxX == 1 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY + 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                                move.moveInDirection("s",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を南に1移動!");
                                    isMove = true;
                                    isFinishFormation = false;
                                    isNextAttack = true;
                                }else{
                                    move.moveInDirection("n",1);
                                    if(move.isMoved == true){
                                        System.out.println("潜水艦を北に1移動!");
                                        isMove = true;
                                        isFinishFormation = false;
                                        isNextAttack = true;
                                    }else{
                                        move.moveInDirection("e",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を東に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                    }else{
                                        move.moveInDirection("w",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を西に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                        }else{
                                        
                                    }
                                }
                            }
                            }
                            }
                            if(trueMaxX == 3 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    isMove = true;
                                    isFinishFormation = false;
                                    isNextAttack = true;
                                }else{
                                    move.moveInDirection("s",1);
                                    if(move.isMoved == true){
                                        System.out.println("潜水艦を南に1移動!");
                                        isMove = true;
                                        isFinishFormation = false;
                                        isNextAttack = true;
                                    }else{
                                        move.moveInDirection("n",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を北に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                    }else{
                                        move.moveInDirection("w",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を西に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                        }else{
                                        
                                    }
                                }
                            }
                            }
                            }
                            if(trueMaxX == 3 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                                move.moveInDirection("e",1);
                                if(move.isMoved == true){
                                    System.out.println("潜水艦を東に1移動!");
                                    isMove = true;
                                    isFinishFormation = false;
                                    isNextAttack = true;
                                }else{
                                    move.moveInDirection("s",1);
                                    if(move.isMoved == true){
                                        System.out.println("潜水艦を南に1移動!");
                                        isMove = true;
                                        isFinishFormation = false;
                                        isNextAttack = true;
                                    }else{
                                        move.moveInDirection("n",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を北に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                    }else{
                                        move.moveInDirection("w",1);
                                        if(move.isMoved == true){
                                            System.out.println("潜水艦を西に1移動!");
                                            isMove = true;
                                            isFinishFormation = false;
                                            isNextAttack = true;
                                        }else{
                                        
                                    }
                                }
                            }
                            }
                            }
                            
                        }
                        
                        if(isMove == false){
                             //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                        maxX = maxX + 1;
                        if( maxY == 0){
                            System.out.println("A"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 1){
                            System.out.println("B"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 2){
                            System.out.println("C"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 3){
                            System.out.println("D"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 4){
                            System.out.println("E"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        }
                }
                
            }else{
                //ロックオンしていなかった場合
                for(int x = 0;x <= 4;x++){
                    for(int y = 0;y <=4;y++){
                        if(Forecast.getEF(x,y) >=  maxEF){
                            //最大値を更新
                            trueMaxX = x;
                            trueMaxY = y; 
                            trueMaxEF = Forecast.getEF(x,y);
                            //攻撃可能なら記録
                            if(AllyAttack.isAttackRangeValid(x,y,mapData)){
                                maxX = x;
                                maxY = y;
                                maxEF = Forecast.getEF(x,y);
                            }
                            
                            }
                        }
                    }

                    if(Forecast.getEF(trueMaxX,trueMaxY) > 4){
                        int shipType = mapData.getMap((char) ('A' + trueMaxY), trueMaxX);
                        Move move = new Move(shipType,allyShipManager,mapData);
                        if(trueMaxX == 1 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY - 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を北に1移動!");
                            move.moveInDirection("n",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 1 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY + 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を南に1移動!");
                            move.moveInDirection("s",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 3 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を東に1移動!");
                            move.moveInDirection("e",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 3 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を東に1移動!");
                            move.moveInDirection("e",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        
                    }
                    
                    if(isMove == false){
                         //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                    maxX = maxX + 1;
                    if( maxY == 0){
                        System.out.println("A"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 1){
                        System.out.println("B"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 2){
                        System.out.println("C"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 3){
                        System.out.println("D"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 4){
                        System.out.println("E"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    }
    
                   
            }
            
        }    
                
        
    }

    public static void startAttackAlgorithm(){
        //攻撃実行

            //EFが最大である地点を検索
            int maxEF = 0;
            int maxX = 0;
            int maxY = 0;
            int trueMaxX = 0;
            int trueMaxY = 0;
            int trueMaxEF = 0;
            boolean isRetry = true;
            boolean isMove = false;

            //ロックオンしているかどうか判断
            if(isRockOn == true){
                for(int i = 0;i <= numberOfRockOn && isRetry == true;i++){
                    if(AllyAttack.isAttackRangeValid(forecastEnemyX[i],forecastEnemyY[i],mapData)){
                        //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                        forecastEnemyX[i] = forecastEnemyX[i] + 1;
                    if( forecastEnemyY[i] == 0){
                        System.out.println("A"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 1){
                        System.out.println("B"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 2){
                        System.out.println("C"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 3){
                        System.out.println("D"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    if( forecastEnemyY[i] == 4){
                        System.out.println("E"+ forecastEnemyX[i] +"に攻撃!");
                        isRetry = false;
                    }
                    }
                }
                //ロックオンしたマスがすべて攻撃範囲外だった場合
                if(isRetry == true){

                    for(int x = 0;x <= 4;x++){
                        for(int y = 0;y <=4;y++){
                            if(Forecast.getEF(x,y) >=  maxEF){
                                //最大値を更新
                                trueMaxX = x;
                                trueMaxY = y; 
                                trueMaxEF = Forecast.getEF(x,y);
                                //攻撃可能なら記録
                                if(AllyAttack.isAttackRangeValid(x,y,mapData)){
                                    maxX = x;
                                    maxY = y;
                                    maxEF = Forecast.getEF(x,y);
                                }
                                
                                }
                            }
                        }
                        //自軍と敵が完全に同じ座標に居て攻撃できない可能性が高いときには1マス移動
                        if(Forecast.getEF(trueMaxX,trueMaxY) > 4){
                            int shipType = mapData.getMap((char) ('A' + trueMaxY), trueMaxX);
                            Move move = new Move(shipType,allyShipManager,mapData);
                            if(trueMaxX == 1 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY - 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                                System.out.println("潜水艦を北に1移動!");
                                move.moveInDirection("n",1);
                                isMove = true;
                                isFinishFormation = false;
                                isNextAttack = true;
                            }
                            if(trueMaxX == 1 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY + 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                                System.out.println("潜水艦を南に1移動!");
                                move.moveInDirection("s",1);
                                isMove = true;
                                isFinishFormation = false;
                                isNextAttack = true;
                            }
                            if(trueMaxX == 3 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                                System.out.println("潜水艦を東に1移動!");
                                move.moveInDirection("e",1);
                                isMove = true;
                                isFinishFormation = false;
                                isNextAttack = true;
                            }
                            if(trueMaxX == 3 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                                System.out.println("潜水艦を東に1移動!");
                                move.moveInDirection("e",1);
                                isMove = true;
                                isFinishFormation = false;
                                isNextAttack = true;
                            }
                            
                        }
                        
                        if(isMove == false){
                             //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                        maxX = maxX + 1;
                        if( maxY == 0){
                            System.out.println("A"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 1){
                            System.out.println("B"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 2){
                            System.out.println("C"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 3){
                            System.out.println("D"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        if( maxY == 4){
                            System.out.println("E"+ maxX +"に攻撃!");
                            isRetry = false;
                        }
                        }
                }
                
            }else{
                //ロックオンしていなかった場合
                for(int x = 0;x <= 4;x++){
                    for(int y = 0;y <=4;y++){
                        if(Forecast.getEF(x,y) >=  maxEF){
                            //最大値を更新
                            trueMaxX = x;
                            trueMaxY = y; 
                            trueMaxEF = Forecast.getEF(x,y);
                            //攻撃可能なら記録
                            if(AllyAttack.isAttackRangeValid(x,y,mapData)){
                                maxX = x;
                                maxY = y;
                                maxEF = Forecast.getEF(x,y);
                            }
                            
                            }
                        }
                    }

                    if(Forecast.getEF(trueMaxX,trueMaxY) > 4){
                        int shipType = mapData.getMap((char) ('A' + trueMaxY), trueMaxX);
                        Move move = new Move(shipType,allyShipManager,mapData);
                        if(trueMaxX == 1 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY - 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を北に1移動!");
                            move.moveInDirection("n",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 1 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY + 1), trueMaxX) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を南に1移動!");
                            move.moveInDirection("s",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 3 && trueMaxY == 1 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を東に1移動!");
                            move.moveInDirection("e",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        if(trueMaxX == 3 && trueMaxY == 3 && mapData.getMap((char) ('A' + trueMaxY), trueMaxX + 1) != 9 && isMove == false && isFinishFormation == true){
                            System.out.println("潜水艦を東に1移動!");
                            move.moveInDirection("e",1);
                            isMove = true;
                            isFinishFormation = false;
                            isNextAttack = true;
                        }
                        
                    }
                    
                    if(isMove == false){
                         //y座標をアルファベットに変換し,xを0基準から1基準に変換して攻撃座標を出力
                    maxX = maxX + 1;
                    if( maxY == 0){
                        System.out.println("A"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 1){
                        System.out.println("B"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 2){
                        System.out.println("C"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 3){
                        System.out.println("D"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    if( maxY == 4){
                        System.out.println("E"+ maxX +"に攻撃!");
                        isRetry = false;
                    }
                    }
    
                   
            }
            
            
    }

    //敵が移動したときのアルゴリズム
    public static void enemyMoveAlgorithm(String direction,int distance){
        int[] maybeEnemyX = new int[25];
        int[] maybeEnemyY = new int[25];
        
        numberOfRockOn = 0;
        //EFが3以上の座標を記録
        for(int x = 0; x <= 4;x++){
            for(int y = 0; y <= 4;y++){
                if(Forecast.getEF(x,y) >= 4){
                    maybeEnemyX[numberOfRockOn] =x;
                    maybeEnemyY[numberOfRockOn] =y;
                    numberOfRockOn++;
                     //ロックオン開始
                    isRockOn = true;
                }
            }
        }

        //記録した座標を敵の動作をもとにずらす

        //directionの対応について
        //e(東) ,w(西) ,s(南) ,n(北)

        //東へ移動
        if(direction.equals("e")){
            for(int j = 0; j <= numberOfRockOn;j++){
                if(0 <= maybeEnemyX[j] + distance && maybeEnemyX[j] + distance <= 4){
                    forecastEnemyX[j] = maybeEnemyX[j] + distance;
                    forecastEnemyY[j] = maybeEnemyY[j];
                }
                
            }

        }

        //西へ移動
        if(direction.equals("w")){
            for(int j = 0; j <= numberOfRockOn;j++){
                if(0 <= maybeEnemyX[j] - distance && maybeEnemyX[j] - distance <= 4){
                    forecastEnemyX[j] = maybeEnemyX[j] - distance;
                    forecastEnemyY[j] = maybeEnemyY[j];
                }
                
            }
            
        }

        //南へ移動
        if(direction.equals("s")){
            for(int j = 0; j <= numberOfRockOn;j++){
                if(0 <= maybeEnemyY[j] + distance && maybeEnemyY[j] + distance <= 4){
                    forecastEnemyX[j] = maybeEnemyX[j];
                    forecastEnemyY[j] = maybeEnemyY[j] + distance;
                }
                
            }
            
        }

        if(direction.equals("n")){
            for(int j = 0; j <= numberOfRockOn;j++){
                if(0 <= maybeEnemyY[j] - distance && maybeEnemyY[j] - distance <= 4){
                    forecastEnemyX[j] = maybeEnemyX[j];
                    forecastEnemyY[j] = maybeEnemyY[j] - distance;
                }
                
            }
            
        }

       
    }

    //isFinishFormationを変更するメソッド
    public static void setIsFinishFormation(boolean is){
        isFinishFormation = is;
    }
    
    public static void setSurchPattern(int num){
        surchPattern = num;
    }
}
    

    // public void test(){
    //     // if(0 <= 4-1 && 4-1<= 4 && 0 <= 4+1 && 4+1<= 4){
    //     //     Forecast.setEF(4-1,4+1,1);
    //     // }

    //     // if(0 <= 4-1 && 4-1 <= 4){
    //     //     Forecast.setEF(4-1,4,1);
    //     // }
        
    //     // if(0 <= 4-1 && 4-1<= 4 && 0 <= 4-1 && 4-1<= 4){
    //     //     Forecast.setEF(4-1,4-1,1);
    //     // }

    //     // if(0 <= 4-1 && 4-1<= 4){
    //     //     Forecast.setEF(4,4-1,1);
    //     // }

    //     // if(0 <= 4+1 && 4+1<= 4 && 0 <= 4-1 && 4-1<= 4){
    //     //     Forecast.setEF(4+1,4-1,1);
    //     // }

    //     // if(0 <= 4+1 && 4+1 <= 4){
    //     //     Forecast.setEF(4+1,4,1);
    //     // }

    //     // if(0 <= 4+1 &&4+1 <= 4 && 0 <= 4+1 && 4+1 <= 4){
    //     //     Forecast.setEF(4+1,4+1,1);
    //     // }

    //     // if(0 <= 4+1 && 4+1 <= 4){
    //     //     Forecast.setEF(4,4+1,1);
    //     // }
    //     Forecast.setCircleEF(3,3,1);
    //     Forecast.setCircleEF(4,4,2);
    //     Forecast.plusCircleEF(3,3,1);

    // }