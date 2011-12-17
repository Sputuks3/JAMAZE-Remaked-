package org.brendanchong.jamaze.GameEngine;
import java.util.StringTokenizer;

/*
 * The getCommand method takes a string as a parameter and return 
 * command tokens.
 */

public class Parser 
{

    private CommandWords commands;  // holds all valid command words

    public Parser() 
    {
        commands = new CommandWords();
    }

    public Command getCommand(String commandLine) 
    {
        
        String word1;
        String word2;


        StringTokenizer tokenizer = new StringTokenizer(commandLine);

        if(tokenizer.hasMoreTokens())
            word1 = tokenizer.nextToken();      // get first word
        else
            word1 = null;
        if(tokenizer.hasMoreTokens())
            word2 = tokenizer.nextToken();      // get second word
        else
            word2 = null;

        // note: we just ignore the rest of the input line.

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).

        if(commands.isCommand(word1))
            return new Command(word1, word2);
        else
            return new Command(null, word2);
    }
}
