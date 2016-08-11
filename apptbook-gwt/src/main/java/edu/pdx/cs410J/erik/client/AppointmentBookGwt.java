package edu.pdx.cs410J.erik.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.thirdparty.common.css.compiler.ast.StringCharStream;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * The implementation for the AppointmentBook user interface
 */
public class AppointmentBookGwt implements EntryPoint {
    private final Alerter alerter;
    private final String CURR_USER = "Current user: ";
    private final String README = "<h2><u>Project 5: A fully featured rich internet application</u></h2><br/>" +
            "This project features a GWT-based application capable of storing and accessing appointment<br/>" +
            "books remotely on a server. Through simple text fields, the user may view, add to, and search his or her<br/>" +
            "appointment book. The application supports multiple users as well.";
    String currentOwner;

    AppointmentBookServiceAsync asyncService;

    @VisibleForTesting
    TextArea textArea;

    TabPanel tabPanel;

    VerticalPanel addPanel;
    VerticalPanel searchPanel;
    VerticalPanel switchUserPanel;
    VerticalPanel readmePanel;
    HTML readme;

    Label descriptionLabelAdd;
    TextBox descriptionTextBoxAdd;
    Label startLabelAdd;
    TextBox startTextBoxAdd;
    Label endLabelAdd;
    TextBox endTextBoxAdd;
    Button submitNewAppointment;

    Label startLabelSearch;
    TextBox startTextBoxSearch;
    Label endLabelSearch;
    TextBox endTextBoxSearch;
    Button submitSearch;

    Label currentUserLabel;
    TextBox switchTextBox;
    Button submitSwitchUser;

    public AppointmentBookGwt() {
        this(new Alerter() {
            @Override
            public void alert(String message) {
                Window.alert(message);
            }
        });
    }

    @VisibleForTesting
    AppointmentBookGwt(Alerter alerter) {
        this.alerter = alerter;

        asyncService = GWT.create(AppointmentBookService.class);

        addWidgets();
        addHandlers();
    }

    /**
     * Create and add widgets to various panels.
     */
    private void addWidgets() {
        textArea = new TextArea();
        tabPanel = new TabPanel();
        addPanel = new VerticalPanel();
        searchPanel = new VerticalPanel();
        switchUserPanel = new VerticalPanel();
        readmePanel = new VerticalPanel();
        readme = new HTML(README);
        readmePanel.add(readme);

        descriptionLabelAdd = new Label("Description: ");
        descriptionTextBoxAdd = new TextBox();
        startLabelAdd = new Label("Start time: ");
        startTextBoxAdd = new TextBox();
        startTextBoxAdd.getElement().setPropertyString("placeholder", "MM/dd/yyyy hh:mm aa");
        endLabelAdd = new Label("End time: ");
        endTextBoxAdd = new TextBox();
        endTextBoxAdd.getElement().setPropertyString("placeholder", "MM/dd/yyyy hh:mm aa");
        submitNewAppointment = new Button("Submit");

        startLabelSearch = new Label("Start time: ");
        startTextBoxSearch = new TextBox();
        startTextBoxSearch.getElement().setPropertyString("placeholder", "MM/dd/yyyy hh:mm aa");
        endLabelSearch = new Label("End time: ");
        endTextBoxSearch = new TextBox();
        endTextBoxSearch.getElement().setPropertyString("placeholder", "MM/dd/yyyy hh:mm aa");
        submitSearch = new Button("Search");

        switchTextBox = new TextBox();
        currentUserLabel = new Label(CURR_USER + "temp");
        switchTextBox.getElement().setPropertyString("placeholder", "Enter username");
        submitSwitchUser = new Button("Switch user");

        addPanel.add(descriptionLabelAdd);
        addPanel.add(descriptionTextBoxAdd);
        addPanel.add(startLabelAdd);
        addPanel.add(startTextBoxAdd);
        addPanel.add(endLabelAdd);
        addPanel.add(endTextBoxAdd);
        addPanel.add(submitNewAppointment);

        searchPanel.add(startLabelSearch);
        searchPanel.add(startTextBoxSearch);
        searchPanel.add(endLabelSearch);
        searchPanel.add(endTextBoxSearch);
        searchPanel.add(submitSearch);

        switchUserPanel.add(currentUserLabel);
        switchUserPanel.add(switchTextBox);
        switchUserPanel.add(submitSwitchUser);
    }

    /**
     * This method is responsible for adding event handlers to various objects.
     */
    private void addHandlers() {
        submitNewAppointment.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String description = descriptionTextBoxAdd.getText();
                String start = startTextBoxAdd.getText();
                String end = endTextBoxAdd.getText();

                asyncService.addAppointment(currentOwner, description, start, end, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                       checkThrowable(throwable);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        asyncQueryApptBook();
                    }
                });
            }
        });

        submitSearch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                asyncSearchAppts();
            }
        });

        submitSwitchUser.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                currentOwner = switchTextBox.getText();
                currentUserLabel.setText(currentOwner);
                asyncQueryApptBook();
            }
        });

        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> selectionEvent) {
                // If you select the search tab and if there is something written there, do the search.
                if (selectionEvent.getSelectedItem() == 1) {
                    if (startTextBoxSearch.getText() != "" && endTextBoxSearch.getText() != "") {
                        asyncSearchAppts();
                    }
                }
                // Otherwise, just query the current state of the user's appointment book
                else {
                    asyncQueryApptBook();
                }
            }
        });
    }

    /**
     * Helper function for checking exceptions
     *
     * @param throwable The expection that was thrown.
     */
    private void checkThrowable(Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) {
            alerter.alert("Your date formatting is incorrect. Dates must be in the format MM/dd/yyyy hh:mm aa");
        }
        else {
            alerter.alert(throwable.getMessage());
        }
    }

    /**
     * Display a window prompt for the user's name. GWT widgets are overpowered for such a simple task. The extension
     * of the JNI is a very cool feature.
     *
     * @return The user's name.
     */
    public static native String prompt() /*-{
        var name = null;
        while (name === null) {
            name = $wnd.prompt("Enter your name: ");
        }
        return name;
    }-*/;

    @Override
    public void onModuleLoad() {
        currentOwner = prompt();
        currentUserLabel.setText(CURR_USER + currentOwner);
        RootPanel rootPanel = RootPanel.get();
        rootPanel.add(tabPanel);
        tabPanel.add(addPanel, "Add appointment");
        tabPanel.add(searchPanel, "Search appointments");
        tabPanel.add(switchUserPanel, "Switch user");
        tabPanel.add(readmePanel, "README");
        tabPanel.selectTab(0);
        rootPanel.add(textArea);
        asyncQueryApptBook();
    }

    /**
     * Get the current user's AppointmentBook and display its pretty-printed version on screen.
     */
    private void asyncQueryApptBook() {
        asyncService.getAppointmentBook(currentOwner, new AsyncCallback<AppointmentBook>() {
            @Override
            public void onFailure(Throwable throwable) {
                checkThrowable(throwable);
            }

            @Override
            public void onSuccess(AppointmentBook appointmentBook) {
                addAppointmentBookToTextArea(appointmentBook);
            }
        });
    }

    /**
     * Call the service's search functionality and handle any errors.
     */
    private void asyncSearchAppts() {
        String start = startTextBoxSearch.getText();
        String end = endTextBoxSearch.getText();
        asyncService.searchAppointments(currentOwner, start, end, new AsyncCallback<AppointmentBook>() {
            @Override
            public void onFailure(Throwable throwable) {
                checkThrowable(throwable);
            }

            @Override
            public void onSuccess(AppointmentBook appointmentBook) {
                addAppointmentBookToTextArea(appointmentBook);
            }
        });
    }

    /**
     * A helper function for displaying the appointments on-screen
     *
     * @param apptBook The {@link edu.pdx.cs410J.erik.client.AppointmentBook} to display.
     */
    private void addAppointmentBookToTextArea(AppointmentBook apptBook) {
        if (apptBook == null) {
            textArea.setText("Hello " + currentOwner + ", you do not have an appointment book yet. Add an appointment " +
                    "to create one.");
            textArea.setCharacterWidth(textArea.getText().length());
            return;
        }
        Collection<Appointment> appointments = apptBook.getAppointments();
        if (appointments == null) return;
        int size = appointments.size();
        String prettyString = PrettyPrinter.getPrettyString(apptBook);
        textArea.setVisibleLines(40);
        int longest = 0;
        for (String s : prettyString.split("\n")) {
            if (s.length() > longest) longest = s.length();
        }
        textArea.setCharacterWidth(longest);
        textArea.setText(prettyString);

    }

    @VisibleForTesting
    interface Alerter {
        void alert(String message);
    }

}
