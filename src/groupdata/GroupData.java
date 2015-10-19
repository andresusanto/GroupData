/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package groupdata;

import java.util.Scanner;

/**
 *
 * @author Andre
 */
public class GroupData {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        String command;
        Scanner scn = new Scanner(System.in);
        ReplStack<String> replstack = new ReplStack<>("stack");
        
        System.out.println("DISTRIUTED STACK TESTING TOOL");
        
        while( !(command = scn.next()).equals("/exit") ) {
            if (command.equals("/push")){
                replstack.push(scn.next());
            }else if (command.equals("/pop")){
                if (replstack.size() == 0){
                    System.out.println("Empty Stack");
                }else{
                    System.out.println(replstack.pop());
                }
            }else if (command.equals("/top")){
                if (replstack.size() == 0){
                    System.out.println("Empty Stack");
                }else{
                    System.out.println(replstack.top());
                }
                
            }else if (command.equals("/empty")){
                if (replstack.size() == 0){
                    System.out.println("Empty Stack");
                }else{
                    while (replstack.size() > 0){
                        System.out.println(replstack.pop());
                    }
                }
            }else{
                System.out.println("Invalid command!");
            }
        }
        replstack.finish();
        System.out.println("Bye bye!");
    }
    
}
