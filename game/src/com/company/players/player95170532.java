package com.company.players;

import com.company.*;

/**
 * Created by JYMH-SH on 3/7/2019.
 */
public class player95170532 extends Player{

    public player95170532(int col) {
        super(col);
    }

    @Override
    public IntPair getMove(Board board) {
        int [] values =  {0,0,0,0,0};
        int [] bestChoice = {minimax(0, 0, true, values, MIN, MAX),0};
        return new IntPair(bestChoice[0],bestChoice[1]);

    }


    static int MAX = 1000;
    static int MIN = -1000;
    static int minimax(int depth, int currNode,
                       Boolean isMax,
                       int values[], int a,
                       int b)
    {
        if (depth == 10)
            return values[currNode];

        if (isMax)
        {
            int best = MIN;

            for (int i = 0; i < 2; i++)
            {
                int val = minimax(depth + 1, 2 * currNode + i,
                        !isMax, values, a, b);
                best = Math.max(best, val);
                a = Math.max(a, best);

                if (b <= a)
                    break;
            }
            return best;
        }
        else
        {
            int best = MAX;

            for (int i = 0; i < 2; i++)
            {

                int val = minimax(depth + 1, currNode * 2 + i,
                        !isMax, values, a, b);
                best = Math.min(best, val);
                b = Math.min(b, best);

                if (b <= a)
                    break;
            }
            return best;
        }
    }

    // Driver Code
}
