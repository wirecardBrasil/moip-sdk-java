/**
 * @author  Danillo Souza   paniko0
 */

package br.com.moip.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class APIDate {

    private Date date;

    /**
     * This method receives an date to set it on {@code this.date}.
     *
     * @param   date
     *          {@code APIDate} a numerical date.
     */
    public void setDate(Date date) { this.date = date; }

    /**
     * This method returns the value of {@code this.date}.
     *
     * @return  {@code APIDate}
     */
    public Date getDate() { return date; }

    /**
     * This method process {@code this.date} to build the accept date
     * format using the pattern {@code "yyyy-MM-dd"}.
     *
     * @return  {@code String}
     *          The date format accept by API.
     */
    public String getFormatedDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(this.getDate());
    }

}
