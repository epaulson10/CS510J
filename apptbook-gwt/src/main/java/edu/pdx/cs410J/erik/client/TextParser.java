package edu.pdx.cs410J.erik.client;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;

/**
 * A class that implements the {@link AppointmentBookParser} interface, used to read an {@link AppointmentBook} from
 * a text file created by {@link edu.pdx.cs410J.erik.TextDumper}
 */
public class TextParser implements AppointmentBookParser {
    File fileToParse;

    /**
     * Initialize this TextParser to read an
     *
     * @param filename
     */
    public TextParser(String filename) {
        fileToParse = new File(filename);
    }

    /**
     * Parses the text file given in the constructor and returns an {@link AbstractAppointmentBook} based on the
     * information in the file. The textfile should be created by {@link edu.pdx.cs410J.erik.TextDumper};
     *
     * @return An {@link edu.pdx.cs410J.AbstractAppointmentBook} containing the appointments stored in the text file
     * @throws ParserException If the file doesn't exist or is malformatted.
     */
    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(fileToParse);
        } catch (FileNotFoundException e) {
            throw new ParserException("File not found", e);
        }

        String owner = "";
        if (scanner.hasNextLine()) {
            owner = scanner.nextLine();
        } else {
            throw new ParserException("Incorrectly formatted AppointmentBook text file");
        }

        AppointmentBook apptBook = new AppointmentBook(owner);

        while (scanner.hasNextLine()) {
            String desc = "";
            String start = "";
            String end = "";
            // Appointments are stored on 3 lines, 1) description 2)start 3) end
            for (int i = 0; i < 3; i++) {
                if (!scanner.hasNextLine()) {
                    throw new ParserException("Incorrectly formatted AppointmentBook text file");
                }
                switch (i) {
                    case 0:
                        desc = scanner.nextLine();
                        break;
                    case 1:
                        start = scanner.nextLine();
                        break;
                    case 2:
                        end = scanner.nextLine();
                        break;
                }

            }
            Date startDate = new Date(Long.parseLong(start));
            Date endDate = new Date(Long.parseLong(end));
            apptBook.addAppointment(new Appointment(desc, startDate, endDate));
        }

        return apptBook;
    }
}
