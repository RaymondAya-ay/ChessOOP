package com.chess.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GameLog {

    public void createfile(){
        try{
            File file = new File("./src/com/chess/logger/data/logs.txt");
            if(file.createNewFile()){
                System.out.println(file.getName()+" File Created for Game log on "+file.getPath());
            }
            else{
                System.out.println("File already Exists");
            }

        }
        catch (IOException error){
                System.out.println("Creating File error occured");
                error.printStackTrace();
        }
    }
    public void log(String team,String piece,int CurrX, int CurrY, int DestX, int DestY){
        char letters[]={'A','B','C','D','E','F','G','H'};
        try{
            FileWriter logmove = new FileWriter("./src/com/chess/logger/data/logs.txt",true);
            logmove.write(team+" "+ piece + " moved from " + letters[CurrX]+(CurrY+1)+" to "+ letters[DestX]+(DestY+1)+"\n");
            logmove.close();
        }
        catch (IOException error){
            System.out.println("Writing on File error occured");
            error.printStackTrace();
        }
    }public void newgame(){

        try{
            FileWriter starter = new FileWriter("./src/com/chess/logger/data/logs.txt",true);
            starter.write("New game started\n");
            starter.close();
        }
        catch (IOException error){
            System.out.println("Starting new game log failed");
            error.printStackTrace();
        }
    }
}
