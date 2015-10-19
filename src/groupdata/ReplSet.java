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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.util.Util;

/**
 *
 * @author Andre
 */
public class ReplSet<T> extends ReceiverAdapter{
    private JChannel channel;
    private Set<T> set;
    
    ReplSet(String channelstr) throws Exception{
        set = new HashSet<T>();
        
        channel = new JChannel();
        channel.setReceiver(this);
        channel.connect(channelstr);
        channel.getState(null, 10000);
    }
    
    public void finish(){
        channel.close();
    }
    
    public boolean add(T obj) throws Exception{
        if (set.add(obj)){
            DataMessage msgdata = new DataMessage(1, obj);
            Message msg = new Message(null, null, msgdata);
            channel.send(msg);
            return true;
        }else{
            return false;
        }
        
    }
    
    public boolean contains(T obj){
        return set.contains(obj);
    }
    
    public void printall(){
        for (Iterator<T> it = set.iterator(); it.hasNext(); ) {
            T f = it.next();
            System.out.println(f);
        }
    }

    public boolean remove(T obj) throws Exception{
        if (set.remove(obj)){
            DataMessage msgdata = new DataMessage(2, obj);
            Message msg = new Message(null, null, msgdata);
            channel.send(msg);
            return true;
        }else{
            return false;
        }
    }
    
    public int size(){
        return set.size();
    }
    
    public void receive(Message msg) {
        if (msg.getSrc().equals(channel.getAddress())) return;
        
        DataMessage msgdata = (DataMessage) msg.getObject();
        synchronized(set) {
            switch (msgdata.command){
                case 1: //add
                    set.add((T)msgdata.content);
                    break;
                case 2: //remove
                    set.remove((T)msgdata.content);
                    break;
            }
        }
    }
    
    public void getState(OutputStream output) throws Exception {
        synchronized(set) {
            Util.objectToStream(set, new DataOutputStream(output));
        }
    }
    
    public void setState(InputStream input) throws Exception {
        Set<T> list;
        list=(Set<T>)Util.objectFromStream(new DataInputStream(input));
        
        synchronized(set) {
            set.clear();
            set.addAll(list);
        }
    }
}
