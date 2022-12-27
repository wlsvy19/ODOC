package practice;

import java.util.Vector;

public class 게임판덮기 {
    int coverType[][][] = { 
            { { 0, 0 }, { 1, 0 }, { 0, 1 } }, { { 0, 0 }, { 0, 1 }, { 1, 1 } },
            { { 0, 0 }, { 1, 0 }, { 1, 1 } }, { { 0, 0 }, { 1, 0 }, { 1, -1 } } };

    public static void main(String[] args) {

    } // end main()

    boolean set(Vector<Vector<Integer>> board, int y, int x, int type, int delta) {
        boolean ok = true;
        
        for(int i=0; i<3; ++i) {
            int ny = y + coverType[type][i][0];
            int nx = x + coverType[type][i][1];
            
            if(ny < 0 || ny >= board.size() || nx < 0 || nx >= board.size()) {
                ok = false;
            } else if((board.ba > 1) {
                ok = false;
            }
        }
        
        
        return ok;
    }
}
