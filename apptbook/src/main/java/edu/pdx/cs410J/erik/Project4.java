package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {
    public static final String README = "By: Erik Paulson\n" +
            "CS410J Project 4: A REST-ful Appointment Book Web Service \n" +
            "A command line program which sends requests to a specified \n" +
            "REST-ful Appointment Book service to create and query appointments.";

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
        String hostname = null;
        String portString = null;
        boolean doSearch = false;

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
                    argList.remove(argument);
                    break;
                case "-textFile":
                    textFile = getParameterAndShift(argList);
                    break;
                case "-pretty":
                    prettyFile = getParameterAndShift(argList);
                    break;
                case "-hostname":
                    hostname = getParameterAndShift(argList);
                    break;
                case "-port":
                    portString = getParameterAndShift(argList);
                    break;
                case "-search":
                    doSearch = true;
                    argList.remove(argument);
                    break;
            }
        }

        int expectedNumArgs = doSearch ? 7 : 8;
        // Validate the number of args
        if (argList.size() != expectedNumArgs) {
            System.err.println("Incorrect command line arguments.");
            System.exit(1);
        }

        String owner = null;
        String description = null;
        String startDate = null;
        String startTime = null;
        String startAmOrPm = null;
        String endDate = null;
        String endTime = null;
        String endAmOrPm = null;
        // Get all the arguments into easy to read variables
        if (!doSearch) {
            owner       = argList.get(0);
            description = argList.get(1);
            startDate   = argList.get(2);
            startTime   = argList.get(3);
            startAmOrPm = argList.get(4);
            endDate     = argList.get(5);
            endTime     = argList.get(6);
            endAmOrPm   = argList.get(7);
        } else {
            owner       = argList.get(0);
            startDate   = argList.get(1);
            startTime   = argList.get(2);
            startAmOrPm = argList.get(3);
            endDate     = argList.get(4);
            endTime     = argList.get(5);
            endAmOrPm   = argList.get(6);
        }


        if (description != null && description.equals("")) {
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

        // If both were given as options, communicate with the server
        if (hostname != null && portString != null) {
            int port;
            try {
                port = Integer.parseInt( portString );

            } catch (NumberFormatException ex) {
                usage("Port \"" + portString + "\" must be an integer");
                return;
            }

            AppointmentBookRestClient client = new AppointmentBookRestClient(hostname, port);

            HttpRequestHelper.Response response = null;
            try {
                if (!doSearch) {
                response = client.createAppointment(owner, description, startDateAndTime, endDateAndTime);
                    if (printDescription) {
                        checkResponseCode( HttpURLConnection.HTTP_OK, response);
                        if (printDescription) {
                            System.out.println(response.getContent());
                        }
                    }
                } else {
                    if (description != null) {
                        usage("description and -search cannot both be specfied.");
                    }
                    response = client.searchAppointments(owner, startDateAndTime, endDateAndTime);
                    checkResponseCode(HttpURLConnection.HTTP_OK, response);
                    System.out.println(response.getContent());
                }

            } catch ( IOException ex ) {
                error("While contacting server: " + ex);
                return;
            }
        }
        else {
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
        }



        System.exit(0);
    }

    /**
     * A helper function to parse options that have a string after them
     *
     * @param argList The list of command line arguments
     * @return The string given in the option
     */
    private static String getParameterAndShift(ArrayList<String> argList) {
        String returnValue = null;
        try {
            returnValue = argList.get(1);
        } catch (IndexOutOfBoundsException e) {
            usage(argList.get(0) + " must be followed by a string.");
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

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void mainOld(String... args) {
        String hostName = null;
        String portString = null;
        String key = null;
        String value = null;

        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if ( portString == null) {
                portString = arg;

            } else if (key == null) {
                key = arg;

            } else if (value == null) {
                value = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );

        } else if ( portString == null) {
            usage( "Missing port" );
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        HttpRequestHelper.Response response;
        try {
            if (key == null) {
                // Print all key/value pairs
                response = client.getAllKeysAndValues();

            } else if (value == null) {
                // Print all values of key
                response = client.getValues(key);

            } else {
                // Post the key/value pair
                response = client.addKeyValuePair(key, value);
            }

            checkResponseCode( HttpURLConnection.HTTP_OK, response);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(response.getContent());

        System.exit(0);
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                    response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        System.exit(1);
    }
}