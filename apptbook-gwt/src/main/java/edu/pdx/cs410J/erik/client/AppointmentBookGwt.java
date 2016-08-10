package edu.pdx.cs410J.erik.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.thirdparty.common.css.compiler.ast.StringCharStream;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * A basic GWT class that makes sure that we can send an appointment book back from the server
 */
public class AppointmentBookGwt implements EntryPoint {
    private final Alerter alerter;
    private final String CURR_USER = "Current user: ";
    String currentOwner;

    PingServiceAsync asyncService;

    @VisibleForTesting
    TextArea textArea;

    TabPanel tabPanel;

    VerticalPanel addPanel;
    VerticalPanel searchPanel;
    VerticalPanel switchUserPanel;

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

        asyncService = GWT.create(PingService.class);

        addWidgets();
        addHandlers();
    }

    private void addWidgets() {
        textArea = new TextArea();

        tabPanel = new TabPanel();
        addPanel = new VerticalPanel();
        searchPanel = new VerticalPanel();
        switchUserPanel = new VerticalPanel();

        descriptionLabelAdd = new Label("Description: ");
        descriptionTextBoxAdd = new TextBox();
        startLabelAdd = new Label("Start time: ");
        startTextBoxAdd = new TextBox();
        endLabelAdd = new Label("End time: ");
        endTextBoxAdd = new TextBox();
        submitNewAppointment = new Button("Submit");

        startLabelSearch = new Label("Start time: ");
        startTextBoxSearch = new TextBox();
        endLabelSearch = new Label("End time: ");
        endTextBoxSearch = new TextBox();
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
                        alerter.alert(throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        asyncQueryApptBook();
                    }
                });
            }
        });
    }

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
        tabPanel.selectTab(0);
        rootPanel.add(textArea);
        asyncQueryApptBook();
    }

    private void asyncQueryApptBook() {
        asyncService.getAppointmentBook(currentOwner, new AsyncCallback<AppointmentBook>() {
            @Override
            public void onFailure(Throwable throwable) {
                alerter.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(AppointmentBook appointmentBook) {
                addAppointmentBookToTextArea(appointmentBook);
            }
        });
    }

    private void addAppointmentBookToTextArea(AppointmentBook apptBook) {
        if (apptBook == null) return;
        Collection<Appointment> appointments = apptBook.getAppointments();
        if (appointments == null || appointments.size() == 0) return;
        int size = appointments.size();
        String prettyString = PrettyPrinter.getPrettyString(apptBook);
        //textArea.setVisibleLines(prettyString.length() - prettyString.replace("\n", "").length());
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
