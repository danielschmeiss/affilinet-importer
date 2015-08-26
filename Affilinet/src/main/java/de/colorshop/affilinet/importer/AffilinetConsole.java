package de.colorshop.affilinet.importer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;

import com.google.inject.Inject;

public class AffilinetConsole
{
    @Inject
    private Logger logger;

    private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void start() throws IOException
    {
        printLine("Welcome to the Affilinet Importer.");
        printSeparator();
    }

    private void printSeparator()
    {
        printLine("==================================");
        printLine("");
    }

    public void printLine(String line)
    {
        try
        {
            writer.write(line + System.lineSeparator());
            writer.flush();
        }
        catch (IOException e)
        {
            logger.severe("Can not print value on System.out");
        }
    }

    public String readLine(String format, Object... args) throws IOException
    {
        System.out.print(String.format(format, args));
        String result = "";
        while (result.isEmpty())
        {
            result = reader.readLine().trim();
        }
        return result;
    }

    private static final String LEFT_ALIGN_FORMAT = "| %-1s %-18s | %-60s |";

    public ConsoleOptionEnum getOptions() throws IOException
    {
        printSeparator();
        printLine("Choose your option:");
        printLine("+----------------------+--------------------------------------------------------------+");
        printLine("| OPTION               | DESCRIPTION                                                  |");
        printLine("+----------------------+--------------------------------------------------------------+");
        for (ConsoleOptionEnum option : ConsoleOptionEnum.getSortedValues())
        {
            printLine(String.format(LEFT_ALIGN_FORMAT, option.getId(), option.name(), option.getDescription()));
        }
        printLine("+----------------------+--------------------------------------------------------------+");
        ConsoleOptionEnum result = null;
        while ((result = getOption()) == null)
        {

        }

        return result;
    }

    private ConsoleOptionEnum getOption() throws IOException
    {
        String readLine = readLine("Enter option or number: ").trim();
        try
        {
            try
            {
                int id = Integer.parseInt(readLine);
                ConsoleOptionEnum option = ConsoleOptionEnum.valueWithId(id);
                if (option != null)
                    return option;
            }
            catch (NumberFormatException e)
            {
                // No id entered.
            }
            // So we try the name.
            return ConsoleOptionEnum.valueOf(readLine.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}
