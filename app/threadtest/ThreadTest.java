/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package threadtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author free
 */
public class ThreadTest {
    
    public static void main(String[] args) throws InterruptedException{

        Poker poker = new Poker();
        Runnable tt = new Player(poker);
        Thread p1 = new Thread(tt,"A");
        p1.start();
        Thread p2 = new Thread(tt,"B");
        p2.start();
        Thread p3 = new Thread(tt,"C");
        p3.start();
        Thread p4 = new Thread(tt,"D");
        p4.start();
    }    
}


class Player implements Runnable{
    public Poker poker;
    public Player(Poker poker) {
        this.poker = poker;
    }
    private int order = 0;//令牌
    private int person = 4;//四个玩家
    public boolean isTaked = false;//是否已经摸过牌了
    private List<Integer> handerCards = new ArrayList<Integer>(); //摸到手的牌
    public Player next = null;//下一个线程
    public void setNext(Player player){
        this.next = player;
        this.next.order = player.order+1;
    }
    public void setPoker(int card){//摸牌
        handerCards.add(card);
    }
    public void printPoker(){
        System.out.println(handerCards);
    }
    public void run(){
        int i =0;
        while(!poker.isSendOver()){
            if(i%4==order){
                System.out.println(i);
                synchronized (this){
                    if(isTaked) {
                        try {wait();} catch (Exception e){e.printStackTrace();}
                    }else {
                        System.out.println(Thread.currentThread().getName()+"摸到牌"+poker.getPoker(i));
                        isTaked = true;
                        this.setNext(this);
                        setPoker(poker.getPoker(i));
                        notify();
                    }
                }
            }else{
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i=i+1;

        }
        printPoker();
    }
    
}

class Poker{
    public final int POKER_NUM = 52;
    private List<Integer> poker = new ArrayList<Integer>();
    Poker(){
        setPoker();
    }
    public void setPoker(){//从新洗牌
        for(int i =0;i<POKER_NUM; i++){
            poker.add(i);
        }
    }

    public int getPoker(int index){//同步摸牌
        int i = poker.get(index);
        poker.remove(index);
        return i;
    }
    public boolean isSendOver(){ //牌是否发完了
        return poker.isEmpty();
    }
    public int getNumPoker(){
        return poker.size();
    }
}




class ThreadTest1 implements Runnable { //extends Thread{
    int tickets = 100;
    String str = new String("");
    public void run(){
        while(true){  
//            synchronized(str){
//                if(tickets>0){
//                    try {Thread.sleep(10);} catch(Exception e){e.printStackTrace();}
//                    System.out.println("run():"+Thread.currentThread().getName()+"正在发牌…… "+tickets--); 
//                }
//            }
            sale();
        }
    }
    
    public synchronized void sale(){
        if(tickets>0){
                    try {Thread.sleep(10);} catch(Exception e){e.printStackTrace();}
                    System.out.println("run():"+Thread.currentThread().getName()+"正在发牌…… "+tickets--); 
                }
    }
}
