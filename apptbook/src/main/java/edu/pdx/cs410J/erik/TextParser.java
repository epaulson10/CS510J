package edu.pdx.cs410J.erik;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by erpaulso on 7/9/2016.
 */
public class TextParser implements AppointmentBookParser{
    File fileToParse;

    public TextParser(String filename) {
        fileToParse = new File(filename);
    }

    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        Scanner scanner = null;
        try {
             scanner = new Scanner(fileToParse);
        } catch(FileNotFoundException e) {
            throw new ParserException("File not found", e);
        }

        String owner = "";
        if (scanner.hasNextLine()) {
            owner = scanner.nextLine();
        }

        AppointmentBook apptBook = new AppointmentBook(owner);

        while (scanner.hasNextLine()) {
            String desc = "";
            String start = "";
            String end = "";
            for (int i = 0; i <3; i++) {
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
            apptBook.addAppointment(new Appointment(desc, start, end));
        }

        return apptBook;
    }
}
