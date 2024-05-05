package controllers.evenement;

import modeles.evenement.Evenement;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;






            public class IcsGenerator {

                public static void generateIcsFile(Evenement evenement, String filePath) throws IOException {
                    // Create a new ComponentList for events
                    ComponentList<VEvent> eventList = new ComponentList<>();

                    // Create a new calendar with the ComponentList
                    Calendar calendar = new Calendar(eventList);

                    // Convert Date to DateTime
                    DateTime startDate = new DateTime(Date.from(evenement.getDateDebut().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    DateTime endDate = new DateTime(Date.from(evenement.getDateFin().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                    // Convert DateTime to ZonedDateTime
                    ZonedDateTime startZonedDateTime = startDate.toInstant().atZone(ZoneId.systemDefault());
                    ZonedDateTime endZonedDateTime = endDate.toInstant().atZone(ZoneId.systemDefault());

                    // Create a new VEvent with start date, end date, and summary
                    VEvent vEvent = new VEvent(startZonedDateTime.toInstant(), endZonedDateTime.toInstant(), evenement.getNom());

                    eventList.add(vEvent);
                    System.out.println("Event added to eventList: " + vEvent);

// Output the calendar to an .ics file
                    try (FileOutputStream fout = new FileOutputStream(filePath)) {
                        CalendarOutputter outputter = new CalendarOutputter();
                        outputter.output(calendar, fout);
                        System.out.println("Calendar outputted to file: " + filePath);
                    } catch (IOException e) {
                        throw new IOException("Failed to write calendar to file", e);
                    }
                }
            }