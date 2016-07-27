package ferdi.david.tim.pme16_crafting_game;

import junit.framework.TestCase;


import java.lang.reflect.Method;

/**
 * Created by Administrator on 26.07.2016.
 */
public class GameActivityTest extends TestCase {
    private GameActivity gameActivity = new GameActivity();

    public void testcheckMove()
    {
        boolean test = gameActivity.checkMove(5,4,0,2);
        assertEquals(test, false);
    }

    public void testcheckMove2()
    {
        boolean test = gameActivity.checkMove(0,0,0,1);
        assertEquals(test, true);
    }
}
