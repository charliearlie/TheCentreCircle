package uk.ac.prco.plymouth.thecentrecircle.utilities;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by charliewaite on 22/03/2016.
 */
public class CCUtilities {
    /**
     * @param rd
     * @return
     * @throws IOException
     */
    public String readAllJson(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public String getStringDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String[] dateArray = date.split("-");
        date = dateArray[0] + dateArray[1] + dateArray[2];

        return date;
    }
}
