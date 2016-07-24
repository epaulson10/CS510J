package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main class for the CS410J appointment book Project
 */
public class Project3 {
    public static final String README = "By: Erik Paulson\n" +
            "CS410J Project 3: Pretty Printing an Appointment Book \n" +
            "A command line program that creates an AppointmentBook and Appointment \n" +
            "with the given command line arguments. The user may specify a text file \n" +
            "as an option to store the appointment book from disk or load it. \n"+
            "Additionally, the user may print a textual representation of the AppointmentBook\n" +
            "to a file, or to standard out.";

    public static final String DATE_WRONG_ERROR = "Date is not in 12-hour format: mm/dd/yyyy hh:mm (am|pm)";

    // I like to save memory whenever I can
    private final static byte NUM_OPTIONS = 4;

    /**
     * The main entry point for the Project3 application
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        boolean printDescription = false;
        String textFile = null;
        String prettyFile = null;

        if (args.length == 0) {
            System.err.println("Missing command line arguments");
            System.exit(1);
        }

        // Convert the array to an ArrayList to work with the data easier
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));

        // The options (if any), should be at the front of the arguments
        for (int i = 0; i < NUM_OPTIONS; i++) {
            String argument = argList.get(0);

            // If -README is passed, only print the README and do no more
            if (argument.equals("-README")) {
                System.out.println(README);
                System.exit(0);
            }

            // If the print option is passed, save that info for later and remove that param
            switch (argument) {
                case "-print":
                    printDescription = true;
                    // Remove the options from the list to make parsing arguments easier
                    argList.remove("-print");
                    break;
                case "-textFile":
                    textFile = getFileNameAndShift(argList);
                    break;
                case "-pretty":
                    prettyFile = getFileNameAndShift(argList);
                    break;
            }
        }

        // Validate the number of args
        if (argList.size() != 8) {
            System.err.println("Incorrect command line arguments.");
            System.exit(1);
        }

        // Get all the arguments into easy to read variables
        String owner = argList.get(0);
        String description = argList.get(1);
        String startDate = argList.get(2);
        String startTime = argList.get(3);
        String startAmOrPm = argList.get(4);
        String endDate = argList.get(5);
        String endTime = argList.get(6);
        String endAmOrPm = argList.get(7);

        if (description.equals("")) {
            System.err.println("Description cannot be empty");
            System.exit(1);
        }

        // The dates and time comes in separate, so we need to concatenate them together to feed them to our classes
        String startDateAndTime = startDate + " " + startTime + " " + startAmOrPm;
        String endDateAndTime = endDate + " " + endTime + " " + endAmOrPm;

        if (!isDateValid(startDateAndTime) || !isDateValid(endDateAndTime)) {
            System.err.println(DATE_WRONG_ERROR);
            System.exit(1);
        }

        AppointmentBook book = null;
        if (textFile != null && (new File(textFile).exists())) {
            TextParser parser = new TextParser(textFile);
            try {
                book = (AppointmentBook) parser.parse();
            } catch (ParserException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        } else {
            book = new AppointmentBook(owner);
        }

        // It is a requirement that the name of the owner on the command line matches that in the file.
        if (!book.getOwnerName().equals(owner)) {
            System.err.println("Owner name doesn't match the owner name in the text file.");
            System.exit(1);
        }
        // Create an AppointmentBook and Appointment based off of the command line arguments
        Appointment appointment = new Appointment(description, startDateAndTime, endDateAndTime);
        book.addAppointment(appointment);

        if (printDescription) {
            System.out.println(appointment.toString());
        }

        if (textFile != null) {
            TextDumper dumper = new TextDumper(textFile);
            try {
                dumper.dump(book);
            } catch (IOException e) {
                System.err.println("An error occurred trying to write the AppointmentBook to the file: " + textFile);
                System.exit(1);
            }
        }

        if (prettyFile != null) {
            PrettyPrinter printer = null;
            FileOutputStream fos = null;
            if (prettyFile.equals("-")) {
                printer = new PrettyPrinter(System.out);
            } else {
                try {
                    fos = new FileOutputStream(new File(prettyFile));
                    printer = new PrettyPrinter(fos);
                } catch (IOException e) {
                    System.err.println("An error occurred trying to open file: " + prettyFile);
                    System.exit(1);
                }
            }

            try {
                printer.dump(book);
            } catch (IOException e) {
                String outLocation = prettyFile.equals("-") ? "standard out" : "file: " + prettyFile;
                System.err.println("An error occurred trying to pretty print to " + outLocation);
            }
        }

        System.exit(0);
    }

    /**
     * A helper function to parse options that have a filename after them
     *
     * @param argList The list of command line arguments
     * @return The filename given in the option
     */
    private static String getFileNameAndShift(ArrayList<String> argList) {
        String returnValue = null;
        try {
            returnValue = argList.get(1);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("The name of a textFile must follow the -textFile option.");
            System.exit(1);
        }

        // remove() shifts the elements to the left, so calling this twice pops -textFile and the given filename
        argList.remove(0);
        argList.remove(0);
        return returnValue;
    }

    /**
     * A helper function to validate the date passed in from the command line.
     * A valid date is of the form MM/dd/yyyy HH:mm
     *
     * @param date The date to validate
     * @return true if the date is valid, false otherwise
     */
    private static boolean isDateValid(String date) {
        // mm/dd/yyyy hh:mm
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy h:m a");
        ParsePosition position = new ParsePosition(0);
        simpleDateFormat.parse(date, position);
        return position.getIndex() != 0;

    }

}
