package handlers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {
  
  public String epochToReadable(long epochDateTime) {
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd"); // HH:mm:ss");

    try {
      return simpleDate.format(new Date(epochDateTime * 1000L));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns time in h:mm:ss / mm:ss, or s
   * @param milliseconds
   * @return formatted time
   */
  public static String format(long milliseconds) {
    long seconds = milliseconds / 1000;
    return seconds >= 60 
      ? seconds >= 3600 
      // h:mm:ss
        ? seconds / 3600 + ":" +
          (seconds / 60 % 60 < 10 ? "0" : "") + seconds / 60 % 60 + ":" +
          (seconds % 60 < 10 ? "0" : "") + seconds % 60
        // mm:ss
        : (seconds / 60 + ":" +
          (seconds % 60 < 10 ? "0" : "") + seconds % 60)
      // s.ddd s
      : (double) milliseconds / 1000 + " s";
  }
}