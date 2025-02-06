package date;

import java.text.SimpleDateFormat;

public class DateFormatTest {
    public static void main(String[] args) {
        String currentDateTime = new SimpleDateFormat("yyyyMMdd HHmmss").format(new java.util.Date());

        String RECV_DATE = currentDateTime.substring(0, 8);
        String RECV_HHMISS = currentDateTime.substring(9);

        System.out.println("RECV_DATE = " + RECV_DATE);
        System.out.println("RECV_HHMISS = " + RECV_HHMISS);
    }
}
