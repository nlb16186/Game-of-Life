package src;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.JOptionPane;


public class GameofLifeGUI extends JFrame implements ActionListener{

    //Set the sizes of the cells
    static final int cellsize = 30;
    static final Dimension dimension = new Dimension(cellsize,cellsize);

    //Initialises  a timer for each Iteration of GamePlay
    private Timer timer;

    //A Counter for the Iteration
    private int Iterations = 0;

    //Sets a Two Dimensional Array to scan the Rows and Columns in the grid for AliveLabels
    private AliveLabels[][] celllabels;

    //Tracks the amount of Iterations so far
    private JLabel IterationsLabel = new JLabel("No of Iterations: 0");

    //The Play, Pause, Restart and Instructions Buttons
    private JButton PlayButton = new JButton("Play");
    private JButton PauseButton = new JButton("Pause");
    private JButton RestartButton = new JButton("Restart");
    private JButton InstructionsButton = new JButton("Instructions");

    //A Slider to control the speed of Iterations at 0 to 3 seconds
    JSlider slider = new JSlider(0, 3000);

    //Checks if the game has started
    private boolean GOLStarted = false;

    public GameofLifeGUI(int rows, int columns){

        super("Craig Thomas - Game Of Life");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Used for Creating the labels and calculating the alive ones
        celllabels = new AliveLabels[rows + 2][columns + 2];

        //I and J will be the initial rows and columns
        for(int i = 0; i < rows + 2; i++){
            for(int j = 0; j < columns + 2; j++){
                celllabels[i][j] = new AliveLabels();
            }
        }

        //Creates the Grid at which the game will be played
        JPanel panel = new JPanel(new GridLayout(rows, columns, 1, 1));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //Adds each label to the grid and scans for neighbours in all directions
        for(int i = 1; i< rows + 1; i++){

            for(int j = 1; j < columns + 1; j++){
                panel.add(celllabels[i][j]);

                //North Neighbours
                celllabels[i][j].addNeighbour(celllabels[i-1][j]);
                //South Neighbours
                celllabels[i][j].addNeighbour(celllabels[i+1][j]);
                //West Neighbours
                celllabels[i][j].addNeighbour(celllabels[i][j-1]);
                //East Neighbours
                celllabels[i][j].addNeighbour(celllabels[i][j+1]);

                //North West Neighbours
                celllabels[i][j].addNeighbour(celllabels[i-1][j-1]);
                //North East Neighbours
                celllabels[i][j].addNeighbour(celllabels[i-1][j+1]);
                //South West Neighbours
                celllabels[i][j].addNeighbour(celllabels[i+1][j-1]);
                //South East Neighbours
                celllabels[i][j].addNeighbour(celllabels[i+1][j+1]);

            }

        }
        add(panel, BorderLayout.CENTER);

        //Sets the panel
        panel = new JPanel(new GridLayout(1,3));

        //Sets the buttons on the panel
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));

        //Initialises the Play Button
        PlayButton.addActionListener(this);
        buttonPanel.add(PlayButton);

        //Initialises the Pause button but renders it unusable when not playing
        PauseButton.addActionListener(this);
        PauseButton.setEnabled(false);
        buttonPanel.add(PauseButton);

        //Initialises the Clear Button
        RestartButton.addActionListener(this);
        buttonPanel.add(RestartButton);

        InstructionsButton.addActionListener(this);
        buttonPanel.add(InstructionsButton);

        //Adds the button panel
        panel.add(buttonPanel);

        //Sets up the Iterations Label
        IterationsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(IterationsLabel);

        //Spacing in Slider in milliseconds
        slider.setMajorTickSpacing(500);
        slider.setMinorTickSpacing(250);
        slider.setPaintTicks(true);

        //Location of the labels in the Slider and sets them as numbers between 0 to 3
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        for(int i = 0; i <= 5; i++) {
            labelTable.put( ( i * 1000 ), new JLabel("" + i) );
        }
        //Sets the Slider labels to be visible
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);

        //Adds the Slider to the bottom of the panel
        panel.add(slider);
        add(panel, BorderLayout.SOUTH);

        //Sets location of slider
        setLocation(50, 50);
        pack();
        setVisible(true);

        //Sets the timer to run at the position of the slider
        timer = new Timer(3000 - slider.getValue(), this);

    }


    @Override
    public synchronized void actionPerformed(ActionEvent e) {

        //Checks which button has been pressed
        Object o = e.getSource();

        //If it's the Play button then start the game at the position of the slider and start the counter
        if(o==PlayButton){
            PauseButton.setEnabled(true);
            PlayButton.setEnabled(false);
            GOLStarted = true;
            timer.setDelay(3000 - slider.getValue());
            timer.start();
            return;
        }

        //If it's the Pause Button during gameplay pause the game
        if(o==PauseButton){
            timer.stop();
            GOLStarted = false;
            PauseButton.setEnabled(false);
            PlayButton.setEnabled(true);
            return;

        }

        //If it's the Restart Button then remove the alive labels from the board
        if(o==RestartButton){
            timer.stop();
            GOLStarted = false;
            PauseButton.setEnabled(false);
            PlayButton.setEnabled(true);

            for(int i = 1; i< celllabels.length -1; i++){

                for(int j = 1; j < celllabels[i].length -1; j++){
                    celllabels[i][j].clearBoard();
                }
            }

            //Restarts the counter
            Iterations = 0;
            IterationsLabel.setText("No of Iterations: 0");
            return;
        }

        //If its the Instructions button then show the instructions of the game
        if(o==InstructionsButton){
            PauseButton.setEnabled(true);
            PlayButton.setEnabled(true);
            GOLStarted = false;

            JOptionPane.showMessageDialog(null, "Welcome to the Game of Life. " +
                    "To play, you must begin by clicking on any number of squares and then pressing the Play button to see which the cells live or die. " );
            JOptionPane.showMessageDialog(null,"If there is one or no neighbouring cells it will die from underpopulation. " +
                    "If there are more than three neighbours to a cell it will die from overpopulation. " +
                    "If there are two or three neighbours to a cell it will live. ");
            JOptionPane.showMessageDialog(null,"If there is a dead cell with exactly three alive neighbours this one will be brought to life ");
            JOptionPane.showMessageDialog(null,"To clear the board of all alive cells press the Restart button. " +
                    "To pause the game press the Pause Button. " + "To change the speed of the iterations use the slider. ");
            JOptionPane.showMessageDialog(null,"Enjoy!");

            PauseButton.setEnabled(false);
            return;

        }

        //The delay between Iteration
        timer.setDelay(3000 - slider.getValue());

        //If the game has not started then remain otherwise start the game
        if(!GOLStarted){
            return;
        }
        else {
            Iterations++;
        }

        //Shows the amount of Iteration that have been taken
        IterationsLabel.setText("No of Iterations: " + Iterations);

        //Checks the state if it will live or die
        for(int i = 0; i < celllabels.length; i++) {
            for(int j = 0; j < celllabels[i].length; j++) {
                celllabels[i][j].ScanState();
            }
        }

        //After the first check it will update accordingly to the logic
        for(int i = 0; i < celllabels.length; i++) {
            for(int j = 0; j < celllabels[i].length; j++) {
                celllabels[i][j].UpdateState();
            }
        }
    }
}
