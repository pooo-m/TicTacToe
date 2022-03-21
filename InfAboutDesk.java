package Model;
import java.util.ArrayList;
public class InfAboutDesk {
    private ArrayList<String> desk;
    private boolean myFigure;
    private boolean isMyStep;
    private boolean isGame;
    public InfAboutDesk(){
        desk = new ArrayList<String>();
        for(int i=0;i<100;i++){
            desk.add("1");
        }
        myFigure = false;
        isMyStep = false;
        isGame = false;
    }
    public void setIsGame(boolean g) {isGame = g;}
    public boolean getIsGame(){return isGame;}

    public void setIsMyStep(boolean s){isMyStep = s;}
    public boolean getIsMyStep() {return isMyStep;}

    public void setFigure(boolean f){myFigure = f;}
    public boolean getFigure(){return myFigure;}

    public void setStep(int n, boolean f){
        String s;
        if (f) {s = "X";}
        else {s = "O";}
        desk.set(n,s);
    }

    public String getStep(int n){
        return desk.get(n);
    }

    public void clearDesk(){
        isMyStep = false;
        myFigure = false;
        isGame = false;
        for(int i = 0; i<100;i++){
            desk.set(i,"1");
        }
    }
}
