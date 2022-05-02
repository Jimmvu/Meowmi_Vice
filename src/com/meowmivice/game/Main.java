package com.meowmivice.game;
import com.apps.util.Prompter;

import java.awt.*;
import java.util.Scanner;

class Main {
    public static void main(String[] args){

        try {
            Game meowmi = new Game(new Prompter(new Scanner(System.in)));
            meowmi.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}