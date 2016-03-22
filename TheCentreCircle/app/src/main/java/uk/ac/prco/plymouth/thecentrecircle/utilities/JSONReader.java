package uk.ac.prco.plymouth.thecentrecircle.utilities;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by charliewaite on 22/03/2016.
 */
public class JSONReader {
    /**
     * @param rd
     * @return
     * @throws IOException
     */
    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
