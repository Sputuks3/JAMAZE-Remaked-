package org.brendanchong.jamaze.GameEngine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
/**
 * Welcome to The AMAZE! Game. This game was built on the world-of-zuul game.
 * This is the main functional class. To play this game, just create an instance of this class 
 * on the object bench or add a main method within this class:
 *  
 *  public static void main(String[] args)
 *  {
 *      new GameInterface();    <-- Has to be the name of this class
 *  }
 *  
 * Navigate your way around the maze, using directions north, south, west, east.
 * Warning - Beware of booby traps.
 * 
 * Brendan Chong, 2301426
 * 14/11/05
 */
public class GameInterface implements ActionListener
{
    
    Parser parser;
    JTextArea statusbox;
    JTextField commandbox;
    JButton button1;
    DrawSpace ds;
    private Room currentRoom;
    private Room maze[][];
    private final int rowscols = 4;
    
    /**
     * Set up GUI, rooms and link them together, etc.
     */
    public GameInterface()
    {
        createComponents();
        createRooms();
        parser = new Parser();
        printWelcome();
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Random random = new Random();
        maze = new Room[rowscols][];
        
        for(int i = 0; i < rowscols; i++)
            maze[i] = new Room[rowscols];
            
        initialize_rooms();
        
        Room entrance = new Room("entrance");
        
        Room temp = maze[0][random.nextInt(rowscols)];
        
        entrance.setExits(null, null, temp, null);
        
        Room exit = new Room("exit");
        
        temp = maze[rowscols - 1][random.nextInt(rowscols)];
        
        temp.setExits(null, null, exit, null);

        currentRoom = entrance;
    }
    //initialize room positions eg. 1A, 1B and so on
    private void initialize_rooms()
    {
        final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWYXZ";
        String desc;

        for(int i = 0; i < rowscols; i++)
        {
            for(int j = 0; j < rowscols; j++)
            {
                 
                desc = "" + (i + 1);
                desc += "" + alpha.charAt(j);
                maze[i][j] = new Room(desc);
            }
        }
        
        set_paths();
    }
    //link room exits
    private void set_paths()
    {
        setSouthExits();
        setEastExits();
        setNorthExits();
        setWestExits();
        
    }
    
    private void setSouthExits()    //link south exits
    {
        for(int col = 0; col < rowscols; col++)
        {
            for(int row = 0; row < rowscols - 1; row++)
                maze[row][col].setExits(null, null, maze[row + 1][col], null);
        }
    }
    
    private void setEastExits()     //link east exits
    {
        for(int row = 0; row < rowscols; row++)
        {
            for(int col = 0; col < rowscols - 1; col++)
                maze[row][col].setExits(null, maze[row][col + 1], null, null);
        }
    }
    
    private void setNorthExits()    //link north exits
    {
        for(int col = 0; col < rowscols; col++)
        {
            for(int row = rowscols - 1; row > 0; row--)
                maze[row][col].setExits(maze[row - 1][col], null, null, null);
        }
    }
                
    private void setWestExits()     //link west exits
    {
        for(int row = 0; row < rowscols; row++)
        {
            for(int col = rowscols - 1; col > 0; col--)
                maze[row][col].setExits(null, null, null, maze[row][col - 1]);
        }
    }
    
    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        String strext;
        writeToDisplay("");
        writeToDisplay("Welcome to AMAZE!");
        writeToDisplay("Navigate your way around until you find an exit.");
        writeToDisplay("Beware of traps!!");
        writeToDisplay("Type 'help' if you need help.");
        writeToDisplay("");
        writeToDisplay("You are at " + currentRoom.getDescription());
        strext = "Exits: ";
        if(currentRoom.northExit != null)
        {
            strext += "north ";
            DrawSpace.hasNorth = true;
        }
        if(currentRoom.eastExit != null)
        {    
            strext += "east ";
            DrawSpace.hasEast = true;
        }
        if(currentRoom.southExit != null)
        {
            strext += "south ";
            DrawSpace.hasSouth = true;
        }
        if(currentRoom.westExit != null)
        {    
            strext += "west ";
            DrawSpace.hasWest = true;
        }
        writeToDisplay(strext);
        writeToDisplay("");
        //ds.repaint();
    }
    
        /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            writeToDisplay("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            showHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit"))
            wantToQuit = quit(command);

        return wantToQuit;
    }
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void showHelp() 
    {
        String msg = "Navigate your way around this maze\n";
        msg += " until you find you way out.\n";
        msg += "Each room is marked by a number followed by a letter,\n";
        msg += "The number denotes row and letter by column.\n\n"; 
        
        msg += "Your command words are:";
        msg += "   go quit help";
        
        JOptionPane.showMessageDialog(null, msg, "Game Help", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /** 
     * Try to go to one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            writeToDisplay("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north"))
            nextRoom = currentRoom.northExit;
        if(direction.equals("east"))
            nextRoom = currentRoom.eastExit;
        if(direction.equals("south"))
            nextRoom = currentRoom.southExit;
        if(direction.equals("west"))
            nextRoom = currentRoom.westExit;

        if (nextRoom == null)
            writeToDisplay("There is no door!");
        else {
            currentRoom = nextRoom;
            writeToDisplay("You are at " + currentRoom.getDescription());
            if(currentRoom.getDescription().equals("exit"))
            {   
                writeToDisplay("\n\nCongratulations, you completed the maze!");
                writeToDisplay("You won!!");
            }
            else
            {
                String strext = "Exits: ";
                if(currentRoom.northExit != null)
                {    
                    strext += "north ";
                    DrawSpace.hasNorth = true;
                }
                else
                    DrawSpace.hasNorth = false;
                if(currentRoom.eastExit != null)
                {    
                    strext += "east ";
                    DrawSpace.hasEast = true;
                }
                else
                    DrawSpace.hasEast = false;
                if(currentRoom.southExit != null)
                {
                    strext += "south ";
                    DrawSpace.hasSouth = true;
                }
                else
                    DrawSpace.hasSouth = false;
                if(currentRoom.westExit != null)
                {
                    strext += "west ";
                    DrawSpace.hasWest = true;
                }
                else
                    DrawSpace.hasWest = false;
                writeToDisplay(strext);
                ds.repaint();
            }
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            writeToDisplay("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
    
    // set up the GUI, buttons etc
    public void createComponents()
    {
        
        JFrame gamewin = new JFrame();
        gamewin.setTitle("The AMAZE!!");
        gamewin.setSize(665, 410);
        gamewin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamewin.setVisible(true);
        
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(2,1));
        ds = new DrawSpace(45, 45);
        mapPanel.add(ds);
        
        statusbox = new JTextArea(6, 50);
        JScrollPane scroller = new JScrollPane(statusbox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mapPanel.add(scroller);
        
        
        JPanel commandPanel = new JPanel();
        commandbox = new JTextField("<Enter Command>", 20);
        commandPanel.add(commandbox);
        button1 = new JButton("Execute");
        button1.addActionListener(this);
        commandPanel.add(button1);
        
        
        Container container = gamewin.getContentPane();
        container.add(mapPanel, BorderLayout.CENTER);
        container.add(commandPanel, BorderLayout.SOUTH);
        gamewin.setContentPane(container);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        Command command = parser.getCommand(commandbox.getText());
           if(processCommand(command))
           {
            JOptionPane.showMessageDialog(null, "Thank you for playing. Good bye.", "Bye...", JOptionPane.INFORMATION_MESSAGE);
            System.exit(1);
           }
            
    }
       
    public void writeToDisplay(String str)
    {
        statusbox.append(str + "\n");
    }

}

