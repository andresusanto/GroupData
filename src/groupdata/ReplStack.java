/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package groupdata;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Stack;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Util;

/**
 *
 * @author Andre
 */
public class ReplStack<T> extends ReceiverAdapter{
    private JChannel channel;
    private Stack stack;
    
    private static class DataMessage implements Serializable{
        public int command;
        public Object content;
        
        DataMessage(int cmd, Object obj){
            command = cmd;
            content = obj;
        }
    }
    
    ReplStack(String channelstr) throws Exception{
        stack = new Stack<T>();
        channel = new JChannel();
        channel.setReceiver(this);
        channel.connect(channelstr);
        channel.getState(null, 10000);
    }
    
    public void finish(){
        channel.close();
    }
    
    public void push(T obj) throws Exception{
        stack.add(obj);
        
        DataMessage msgdata = new DataMessage(1, obj);
        Message msg = new Message(null, null, msgdata);
        channel.send(msg);
    }
    
    public int size(){
        return stack.size();
    }
    
    public T pop() throws Exception{
        
        DataMessage msgdata = new DataMessage(2, null);
        Message msg = new Message(null, null, msgdata);
        channel.send(msg);
        
        return (T)stack.pop();
    }
    
    public T top(){
        return (T)stack.peek();
    }
    
    public void receive(Message msg) {
        if (msg.getSrc().equals(channel.getAddress())) return;
        
        DataMessage msgdata = (DataMessage) msg.getObject();
        synchronized(stack) {
            switch (msgdata.command){
                case 1: //push
                    stack.add(msgdata.content);
                    break;
                case 2: //pop
                    stack.pop();
                    break;
            }
        }
    }
    
    public void getState(OutputStream output) throws Exception {
        synchronized(stack) {
            Util.objectToStream(stack, new DataOutputStream(output));
        }
    }
    
    public void setState(InputStream input) throws Exception {
        Stack<T> list;
        list=(Stack<T>)Util.objectFromStream(new DataInputStream(input));
        
        synchronized(stack) {
            stack.clear();
            stack.addAll(list);
        }
    }
}
