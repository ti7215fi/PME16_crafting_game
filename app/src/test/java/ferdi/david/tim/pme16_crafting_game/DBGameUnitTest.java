package ferdi.david.tim.pme16_crafting_game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tim Fischer on 26.07.2016.
 */
public class DBGameUnitTest {
    private DBGame dbgame;
    private int[][] playground;

    @Before
    public void init() {
        this.dbgame = new DBGame();
        this.playground = new int[][]{
                {0, 1, 5, 1, 1, 2, 3},
                {3, 4, 0, 2, 1, 3, 4},
                {0, 1, 5, 1, 1, 2, 3},
                {3, 4, 0, 2, 1, 3, 4},
                {0, 1, 5, 1, 1, 2, 3},
                {3, 4, 0, 2, 1, 3, 4},
                {0, 1, 5, 1, 1, 2, 3}
        };
    }

    @Test
    public void testSetPlayground() throws Exception {
        this.dbgame.setPlayground(this.playground);
        assertEquals(this.dbgame.getPlayground(), " 0, 1, 5, 1, 1, 2, 3, 3, 4, 0, 2, 1, 3, 4, 0, 1, 5, 1, 1, 2, 3, 3, 4, 0, 2, 1, 3, 4, 0, 1, 5, 1, 1, 2, 3, 3, 4, 0, 2, 1, 3, 4, 0, 1, 5, 1, 1, 2, 3,");
    }

    @Test
    public void testGetPlaygroundAsIntArray() throws Exception {
        this.dbgame.setPlayground(playground);
        assertNotEquals(this.dbgame.getPlayground(), playground);
        assertArrayEquals(this.dbgame.getPlaygroundAsIntArray(), playground);
    }
}
