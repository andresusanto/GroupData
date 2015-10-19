/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package groupdata;

import java.io.Serializable;

/**
 *
 * @author Andre
 */
public class DataMessage implements Serializable{
    public int command;
    public Object content;

    DataMessage(int cmd, Object obj){
        command = cmd;
        content = obj;
    }
}