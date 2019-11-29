package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.Point;

import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AliveLabels extends JLabel implements MouseListener {

    private int CurrentState, NewState;
    private boolean mouseDown = false;
    static final Color[] color = {Color.LIGHT_GRAY, Color.GREEN};
    static final String[] symbol = {"   " , "                  *"};


    //Used to check the neighbours for alive labels
    private int newNeighbour;
    private AliveLabels[] neighbouringCell = new AliveLabels[8];


    public AliveLabels(){
        CurrentState = NewState = 0;
        setOpaque(true);
        setBackground(color[0]);
        setText(symbol[0]);

        addMouseListener(this);
        this.setPreferredSize(GameofLifeGUI.dimension);
    }

    //Adds an neighbour
    public void addNeighbour(AliveLabels al){

        neighbouringCell[newNeighbour++] = al;
    }

    //Checks the state if the neighbours will live or die
    public void ScanState(){

        int	aliveNeighbours = 0;

        //Check the state of the new Neighbour
        for(int i =0; i<newNeighbour; i++)

            aliveNeighbours = aliveNeighbours + neighbouringCell[i].CurrentState;

        //If there is an alive cell go to the logic
        if(CurrentState == 1){

            //Underpopulation:  Any live cell with fewer than two live neighbours dies
            if(aliveNeighbours < 2)
                NewState = 0;
                //Overpopulation:   Any live cell with more than three live neighbours dies
            else if(aliveNeighbours > 3)
                NewState = 0;

        }
        else
        {   //Creation of Life: Any dead cell with three alive neighbours is brought to life
            if(aliveNeighbours == 3)
                NewState = 1;
        }

    }

    //After the check update the cells accordingly
    public void UpdateState(){

        if(CurrentState != NewState){
            CurrentState = NewState;
            setBackground(color[CurrentState]);
            setText(symbol[CurrentState]);
        }

    }

    //Clears the board
    public void clearBoard(){

        if(CurrentState == 1 || NewState == 1){
            CurrentState = NewState = 0;
            setBackground(color[CurrentState]);
            setText(symbol[CurrentState]);

        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //Clicking a cell brings the cells to life

    public void mouseEntered(MouseEvent e) {

        if (mouseDown) {
            CurrentState = NewState = 1;
            setBackground(color[1]);
            setText("                  *");

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    //Registers that the mouse has selected this cell and will either bring it to life or kill it
    @Override
    public void mousePressed(MouseEvent e) {
        if(CurrentState == 0) {
            CurrentState = NewState = 1;
            setBackground(color[1]);
            setText("                  *");

        }
        else {
            CurrentState = NewState = 0;
            setBackground(color[0]);
            setText("   ");

        }
    }

    //Registers that the mouse has been released
    @Override
    public void mouseReleased(MouseEvent e) {

        mouseDown = false;
    }

}
