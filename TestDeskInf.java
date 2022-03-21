package Tests;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

//ctrl + shift + f10
public class TestDeskInf {
    @Test
    public void testConstructor() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        for (int j = 0; j < 100; j++) {
            Assert.assertEquals(deskOfGame.getStep(j), "1");
        }
    }

    @Test
    public void testSetGetIsGame() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        deskOfGame.setIsGame(true);
        Assert.assertEquals(deskOfGame.getIsGame(), true);
    }

    @Test
    public void testDoStep() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        String[] indexFigure = new String[50];
        boolean[] figure = new boolean[50];
        int[] index = new int[50];
        Random randomIndex = new Random();
        int n;
        for (int i = 0; i < 50; i++) {
            n = randomIndex.nextInt(99);
            if (n % 2 == 1) {
                indexFigure[i] = "X";
                figure[i] = true;
            } else {
                indexFigure[i] = "O";
                figure[i] = false;
            }
            index[i] = n;
            deskOfGame.setStep(n, figure[i]);
        }
        for (int i = 0; i < 50; i++) {
            Assert.assertEquals(deskOfGame.getStep(index[i]), indexFigure[i]);
        }
    }

    @Test
    public void testClearDesk() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        Random randomIndex = new Random();
        int n;
        boolean figure = false;
        for (int i = 0; i < 50; i++) {
            n = randomIndex.nextInt(99);
            if (n % 2 == 1) figure = true;
            else figure = false;
            deskOfGame.setStep(n, figure);
        }
        deskOfGame.clearDesk();
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(deskOfGame.getStep(i), "1");
        }
    }

    @Test
    public void testSetGetFigure() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        Random randomIndex = new Random();
        boolean n = randomIndex.nextBoolean();
        deskOfGame.setFigure(n);
        Assert.assertEquals(deskOfGame.getFigure(), n);
    }

    @Test
    public void testSetGetIsMyStep() {
        InfAboutDesk deskOfGame = new InfAboutDesk();
        Random randomIndex = new Random();
        boolean n = randomIndex.nextBoolean();
        deskOfGame.setIsMyStep(n);
        Assert.assertEquals(deskOfGame.getIsMyStep(), n);
    }

}
