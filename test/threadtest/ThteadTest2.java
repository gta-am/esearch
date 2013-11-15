package threadtest;

import org.junit.Test;

/**
 * desc:
 * User: weiguili(li5220008@gmail.com)
 * Date: 13-11-14
 * Time: 下午4:47
 */
public class ThteadTest2 {
    @Test
    public void testThreadTest(){
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
