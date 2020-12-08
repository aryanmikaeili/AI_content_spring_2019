package com.company;

import com.company.players.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player94105355(0));
        players.add(new Player95103729(0));
        players.add(new Player95105549(0));
        players.add(new Player95105595(0));
        players.add(new Player95109315(0));
        players.add(new Player95109372(0));
        players.add(new Player95109434(0));
        players.add(new player95170532(0));
        players.add(new Player96100374(0));
        players.add(new Player96102278(0));
        players.add(new Player96105564(0));
        players.add(new Player96105626(0));
        players.add(new Player96105829(0));
        players.add(new Player96105972(0));
        players.add(new Player96106152(0));
        players.add(new Player96109588(0));
        players.add(new Player96109599(0));
        players.add(new Player96109725(0));
        players.add(new Player96109736(0));
        players.add(new Player96109963(0));
        players.add(new com.company.Player96101646(0,4));

        int[][] results = new int[players.size()][players.size()];
        int n = players.size();
        for(int i = 0; i < n - 1; i++)
        {
            for(int j = i + 1; j < n; j++)
            {
                Player p1 = players.get(i);
                Player p2 =players.get(j);
                p1.setCol(1);
                p2.setCol(2);
                for(int t = 0; t < 3; t++)
                {
                    Game g = new Game(p1, p2);
                    int r = g.start();
                    if(r == 1){
                        results[i][j] += 1;
                    }
                    else if(r == 2){
                        results[j][i] += 1;
                    }
                }
                p1.setCol(2);
                p2.setCol(1);
                for(int t = 0; t < 3; t++)
                {
                    Game g = new Game(p1, p2);
                    int r = g.start();
                    if(r == 2){
                        results[i][j] += 1;
                    }
                    else if(r == 1){
                        results[j][i] += 1;
                    }

                }


            }
        }
        ArrayList<Pair<String, Integer>> table = new ArrayList<>();
        for(int i = 0; i < n; i ++)
        {
            int points = 0;
            String name = players.get(i).getClass().getName().replaceAll("[^0-9]", "");
            for(int j = 0; j < n; j++)
            {
                if(i != j) {
                    if (results[i][j] > results[j][i])
                        points += 3;
                    else if(results[i][j] == results[j][i])
                        points += 1;

                }
            }
            table.add(new Pair<String, Integer>(name, points));

        }


        int a = 0;

    }

}