package br.com.moip.api;

import br.com.moip.models.Customer;

public class APIResources {

    private Customer customerInstance = new Customer();

    /**
     * This method is used to get a instance of customer object.
     *
     * @return  {@code Customer}
     */
    public Customer customers() { return this.customerInstance; }
}
