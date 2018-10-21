import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }


        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */

        double maxRes = Double.NEGATIVE_INFINITY;
        int depth = 5;
        Random random = new Random();
        int posState = random.nextInt(nextStates.size());
        //by the assignment description X will always play first
        int currentPlayer= Constants.CELL_X;//gameState.getNextPlayer();
        //returns value for each next state
        for(int i = 0; i< nextStates.size();i++){
            int result = aBprune(nextStates.elementAt(i),depth, maxRes, Double.POSITIVE_INFINITY, currentPlayer, deadline);
            if(result>maxRes){
                maxRes = result;
                posState = i;
            }

            double time = deadline.timeUntil()/1e+9;
            if (time < 0.2) {
                break;
            }
//            System.out.println("i: "+i+"     max:"+maxRes);

        }
//        System.out.println("pos: "+posState);
//        System.out.println("state: "+ nextStates.elementAt(posState).toMessage());


        return nextStates.elementAt(posState);//Math.round(bestPossible));//random.nextInt(nextStates.size()));
    }

    public int aBprune(GameState state, int depth, double alpha, double beta, int player, Deadline deadline){

        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);
//        System.out.println("state: "+state.toMessage());
//        System.out.println("depth: "+depth);


        double v = Double.NEGATIVE_INFINITY;





        if(depth ==0 || state.getMove().isEOG() || nextStates.size()==0){
            v = evaluate(state,player);
        }
        else if(player==Constants.CELL_X){
            v = Double.NEGATIVE_INFINITY;
            for(GameState child: nextStates){
                v = Math.max(v, aBprune(child, depth-1, alpha, beta, Constants.CELL_O, deadline));
                alpha = Math.max(alpha, v);
                if(alpha> beta){
                    break;
                }
            }
        }
        else if(player==Constants.CELL_O){
            v = Double.POSITIVE_INFINITY;
            for(GameState child: nextStates){
                v = Math.min(v, aBprune(child, depth-1, alpha, beta, Constants.CELL_X, deadline));
                beta = Math.min(beta, v);
                if(alpha>beta){
                    break;
                }
            }
        }

        return (int) v;
    }

    /**
     * if not end of game then
     * check if each winning state in the gamestate contain only x(has a possibility of x winning)
     * contain only o (x losing)
     * or contain and o (draw)
     * then wins - losses->
     * positive than more wins for a certain state
     * negative than losses for a certain state
     * **/

    public int evaluate(GameState state, int player){
        if(state.isEOG()){
            if(state.isXWin()){
                return 1000000;
            }
            else if (state.isOWin()){
                return -1000000;
            }
            else if (state.getMove().isDraw()){
                return 0;
            }
        }

        int [][]winningList = {{0,1,2,3},
                {4,5,6,7},
                {8,9,10,11},
                {12,13,14,15},
                {0,4,8,12},
                {1,5,9,13},
                {2,6,10,14},
                {3,7,11,15},
                {0,5,10,15},
                {3,6,9,12}};

        int x= 0;
        int o= 0;
        int win=0;
        int loss =0;
        for(int i= 0;i<winningList.length;i++){
            x=0;
            o=0;
            for(int j=0;j<winningList[0].length;j++){
               if(state.at(winningList[i][j])==Constants.CELL_X){
                   x+=1;
               }
               else if(state.at(winningList[i][j])== Constants.CELL_O){
                   o+=1;
               }
            }
            if(x>0 && o==0){
                if(x==1){
                    win+=10;
                }
                else if(x==2){
                    win+=100;
                }
                else{
                    win+=1000;
                }
            }
            else if(o>0 && x==0){
                if(o==1){
                    loss+=10;
                }
                else if(o==2){
                    loss+=100;
                }
                else{
                    loss+=1000;
                }
            }
            else if(x==2 && o==2){
                if(state.at(winningList[i][0])==Constants.CELL_X){
                    win+=3;
                }

                else if (state.at(winningList[i][3])==Constants.CELL_X){
                    win+=3;
                }
                else if(state.at(winningList[i][0])==Constants.CELL_O){
                    loss+=3;
                }
                else if(state.at(winningList[i][3])==Constants.CELL_X){
                    loss+=3;
                }
                else if ((state.at(winningList[i][1])) == Constants.CELL_X){
                    if((state.at(winningList[i][2]))==Constants.CELL_X){
                        win+=2;
                    }
                }
                else if ((state.at(winningList[i][1])) == Constants.CELL_O){
                    if((state.at(winningList[i][2]))==Constants.CELL_O){
                        loss+=2;
                    }
                }
                else if((state.at(5) == Constants.CELL_X)){
                    win+=3;
                }
                else if(state.at(6)== Constants.CELL_X){
                    win+=3;
                }
                else if(state.at(9)==Constants.CELL_X){
                    win+=3;
                }
                else if(state.at(10)==Constants.CELL_X){
                    win+=3;
                }
                else if((state.at(5) == Constants.CELL_O)){
                    loss+=3;
                }
                else if(state.at(6)== Constants.CELL_O){
                    loss+=3;
                }
                else if(state.at(9)==Constants.CELL_O){
                    loss+=3;
                }
                else if(state.at(10)==Constants.CELL_O){
                    loss+=3;
                }


            }
            else if(x>2 && o<2){

                if(state.at(winningList[i][0])==Constants.CELL_X){
                    win+=6;
                }

                else if (state.at(winningList[i][3])==Constants.CELL_X){
                    win+=6;
                }
                else{
                    win +=4;
                }

            }else if(o>2 && x<2){
                if(state.at(winningList[i][0])==Constants.CELL_O){
                    loss+=6;
                }

                else if (state.at(winningList[i][3])==Constants.CELL_O){
                    loss+=6;
                }
                else{
                    loss +=4;
                }
            }
            else if(x>1 && o<2){

                if(state.at(winningList[i][0])==Constants.CELL_X){
                    win+=3;
                }

                else if (state.at(winningList[i][3])==Constants.CELL_X){
                    win+=3;
                }
                else{
                    win +=2;
                }

            }
            else if(o>1 && x<2){
                if(state.at(winningList[i][0])==Constants.CELL_O){
                    loss+=3;
                }

                else if (state.at(winningList[i][3])==Constants.CELL_O){
                    loss+=3;
                }
                else{
                    loss +=2;
                }
            }
            if(x==0 && o==0){
                win+=1;
                loss+=1;
            }

        }

//        int [][]board =
//                {{0,1,2,3},
//                        {4,5,6,7},
//                        {8,9,10,11},
//                        {12,13,14,15}};
//
//        HashMap<Integer, Integer> hmap = new HashMap<>();
//        hmap.put(0,3);
//        hmap.put(1,2);
//        hmap.put(2,2);
//        hmap.put(3,3);
//        hmap.put(4,2);
//        hmap.put(5,3);
//        hmap.put(6,3);
//        hmap.put(7,2);
//        hmap.put(8,2);
//        hmap.put(9,3);
//        hmap.put(10,3);
//        hmap.put(11,2);
//        hmap.put(12,3);
//        hmap.put(13,2);
//        hmap.put(14,2);
//        hmap.put(15,3);
//
//
//        int wins = 0;
//        int losses = 0;
//        for(int i=0;i<board.length;i++){
//            int x= 0;
//            int o = 0;
//            for(int j =0; j<board[0].length;j++){
//                if(state.at(board[i][j])==Constants.CELL_X){
//                    x += hmap.get(board[i][j]);
//                }
//                else if(state.at(board[i][j])==Constants.CELL_O){
//                    o += hmap.get(board[i][j]);
//                }
//            }
//
//            wins += x-o;
//
//        }

//        for(int i=0;i<winningList.length;i++){
//            int x =0;
//            int o =0;
//            for(int j =0; j<winningList[0].length;j++){
//
//                if(state.at(winningList[i][j])==Constants.CELL_X){
//                    x += 1;
//                }
//                else if(state.at(winningList[i][j])==Constants.CELL_O){
//                    o += 1;
//                }
//
//                if(x>0 && o==0){
//                    if(x==1){
//                        wins +=1;
//                    }
//                    else if(x==2){
//                        wins +=10;
//                    }
//                    else if(x==3){
//                        wins+=100;
//                    }
//
//                }
//                else if(o>0 && x==0){
//                    if(o==1){
//                        losses +=1;
//                    }
//                    else if(o==2){
//                        losses +=10;
//                    }
//                    else if(o==3){
//                        losses+=100;
//                    }
//                }
//
//            }
//
//        }
//        int m = wins-losses;
//        System.out.println("state: "+state.toMessage());
//
//        System.out.println("points: "+m);

        return win-loss;
    }
}
