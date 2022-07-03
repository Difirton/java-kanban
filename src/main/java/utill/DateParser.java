package utill;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public final class DateParser {
    private static List<DateTimeFormatter> dateFormats = new ArrayList<>(){
        {
            add(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy.MM.d HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy/MM/d HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy.M.dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy/M/dd HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy-M-d HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy/M/d HH:mm"));
            add(DateTimeFormatter.ofPattern("yyyy.M.d HH:mm"));
            add(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd-MM-yy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d-MM-yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d.MM.yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d-MM-yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d/MM/yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d-M-yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("d.M.yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d-M-yy HH:mm"));
            add(DateTimeFormatter.ofPattern("d/M/yy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd.M.yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd-M-yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd/M/yyyy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd.M.yy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd-M-yy HH:mm"));
            add(DateTimeFormatter.ofPattern("dd/M/yy HH:mm"));
        }
    };

    public static LocalDateTime stringToDate(String input) {
        if(input == null) {
            return null;
        }
        LocalDateTime dateTime = null;
        for (DateTimeFormatter format : dateFormats) {
            try {
                dateTime = LocalDateTime.parse(input, format);
            } catch (DateTimeParseException e) {
                System.out.println("The entered date format is not supported.");
            }
            if (dateTime != null) {
                break;
            }
        }
        return dateTime;
    }
}