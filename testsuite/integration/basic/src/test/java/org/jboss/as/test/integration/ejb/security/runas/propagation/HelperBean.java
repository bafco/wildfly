package org.jboss.as.test.integration.ejb.security.runas.propagation;

import javax.ejb.EJB;
import javax.ejb.Stateful;

@Stateful
public class HelperBean {

    @EJB
    Toner toner;

    public void invokeToner() {
        toner.spill();
    }
}
