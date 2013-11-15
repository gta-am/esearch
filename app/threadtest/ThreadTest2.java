package threadtest;

import java.util.*;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-12
 * Time: 下午2:24
 */
public class ThreadTest2 {
    /**
     * @param args
     */
    public static void main(String[] args) {
        PokerSender pokers=new PokerSender();
        Players a=new Players("A",pokers);
        Players b=new Players("B",pokers);
        Players c=new Players("C",pokers);
        Players d=new Players("D",pokers);
        a.setNext(b);
        b.setNext(c);
        c.setNext(d);
        d.setNext(a);
        a.start();
    }
}
//扑克发牌器
class PokerSender {
    public final int POKER_NUM = 52;//一共52张
    public final boolean IS_RAMDOM = false;//牌是否随机打乱
    public final int PLAY_NUM =4;//玩家数量
    public int sendNum =0;//已经发了多少张牌
    private List<Integer> poker = new ArrayList<Integer>();//扑克
    public List<Map<String,List<Integer>>> allHanderCards = new ArrayList<Map<String, List<Integer>>>(); //收集所有人手中的牌
    public  int getNext(){
        return sendNum++;
    }
    PokerSender(){
        setPokers();
    }
    public void showAllCards(){
        System.out.println(allHanderCards);
    }
    public synchronized void setPokers(){//从新洗牌
        for(int i =0;i<POKER_NUM; i++){
            poker.add(i);
        }
        if(IS_RAMDOM){ //如果随机则打散
            java.util.Random rand=new Random(); //随机数序列对象
            java.util.Collections.shuffle(poker, rand); //将列表的数据序列打散，按随机数序列
        }
    }

    public synchronized int sendPokers(int index){//同步发牌
        return poker.get(index);
    }
    public boolean isLastTurn(){//是否最后一轮
        return sendNum > getPokersNum()- PLAY_NUM;
    }
    public int getPokersNum(){
        return poker.size();
    }
}

//线程(也就是玩家)
class Players extends Thread {
    private PokerSender pokers;//扑克
    private Players next;//下一家
    public List<Integer> handerCards = new ArrayList<Integer>(); //发到手的牌
    public Players(String name, PokerSender poker){
        this.setName(name);
        this.pokers =poker;
    }
    public void setNext(Players next){
        this.next=next;
    }
    public void setHanderCards(int cards){
        handerCards.add(cards);
    }
    public void cllectAll(){
        if(pokers.isLastTurn()) { //最后一轮，收牌。
            Map<String,List<Integer>> map = new HashMap<String, List<Integer>>();
            map.put(getName(),handerCards);
            pokers.allHanderCards.add(map);
        }
    }
    public void showCards(){
        if(pokers.isLastTurn()) { //最后一轮，该摊牌了。
            System.out.println(handerCards);
        }
    }

    public void run(){
        int sendNum= pokers.getNext();
        while(sendNum<pokers.getPokersNum()){
            sendNum = getPoker(sendNum);
        }
        pokers.showAllCards();
        System.exit(0);
    }

    /**
     * 摸牌
     * @param sendNum 发了多少张牌
     * @return
     */
    private synchronized int getPoker(int sendNum) {
        System.out.println(getName()+"摸到牌"+sendNum);
        setHanderCards(pokers.sendPokers(sendNum));
        this.showCards();
        this.cllectAll();
        if(this.next.getState()== State.NEW){//第一轮 下一个线程如果还没开始则启动它
            this.next.start();
        }else{
            synchronized (this.next) {//唤醒下一个进程
                this.next.notify();
            }
        }
        try { //阻塞当前进程
            this.wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendNum= pokers.getNext();
        if(sendNum%pokers.PLAY_NUM ==0){
            System.out.println("--------------------完成"+sendNum/pokers.PLAY_NUM +"轮----------------");
        }
        return sendNum;
    }
}







