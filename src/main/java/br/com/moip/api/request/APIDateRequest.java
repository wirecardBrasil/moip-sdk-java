package br.com.moip.api.request;

import java.util.Date;

public class APIDateRequest {

    private Date date;

    public Date getDate() {
        return date;
    }

    /**
     * This constructor receive a APIDate object to fill the attribute date.
     *
     * @param   date
     *          {@code APIDate} a numerical date.
     */
    public void date(Date date) { this.date = date; }
}
