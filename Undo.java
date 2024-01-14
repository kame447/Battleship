class Undo {
    public static MapData mapData;
    public static AllyShipManager allyShipManager;
    Undo(AllyShipManager allyShipManager, MapData mapData) {
                this.allyShipManager = allyShipManager;
                this.mapData = mapData;
            }

    public static void undoToPastRound() {
        System.out.println("================================================\n");
        System.out.println("▌ ▌▙ ▌▛▀▖▞▀▖ ▀▛▘▞▀▖ ▛▀▖▞▀▖▞▀▖▀▛▘ ▛▀▖▞▀▖▌ ▌▙ ▌▛▀▖");
        System.out.println("▌ ▌▌▌▌▌ ▌▌ ▌  ▌ ▌ ▌ ▙▄▘▙▄▌▚▄  ▌  ▙▄▘▌ ▌▌ ▌▌▌▌▌ ▌");
        System.out.println("▌ ▌▌▝▌▌ ▌▌ ▌  ▌ ▌ ▌ ▌  ▌ ▌▖ ▌ ▌  ▌▚ ▌ ▌▌ ▌▌▝▌▌ ▌");
        System.out.println("▝▀ ▘ ▘▀▀ ▝▀   ▘ ▝▀  ▘  ▘ ▘▝▀  ▘  ▘ ▘▝▀ ▝▀ ▘ ▘▀▀");
        System.out.println("================================================");
        System.out.print("何ターン目まで戻りますか？: ");
        int roundNumber = Input.intInput() - 1;
        undoMapData(roundNumber);
        undoEFData(roundNumber);
        undoHPData(roundNumber);
        System.out.println("Undoをどうぞ");
        Forecast.printEF();
    }

    public static void undoMapData(int turn) {

        if(turn == 0){
            turn = 1;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // 現在のマップの指定の値 = GetPastData.getPastMapData(turn, i, j);
                mapData.setMap((char)(i), j, GetPastData.getPastMapData(turn - 1, i, j));
            }
        }
    }

    public static void undoEFData(int turn) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // 現在のEFの指定の値 = GetPastData.getPastEFData(turn, i, j);
                Forecast.setEF(i, j, GetPastData.getPastEFData(turn, i, j));
            }
        }
    }

    public static void undoHPData(int turn) {
        for (int k = 1; k < 5; k++) {
            // 現在の潜水艦の指定の値 = GetPastData.getPastHPData(turn, k);
            allyShipManager.setShipHp(k, GetPastData.getPastHPData(turn, k - 1));
        }
    }
}