package org.arquillian.examples;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

@Stateless
public class TemperatureConverter {

    private Logger log = Logger.getLogger(TemperatureConverter.class);

    public double convertToCelsius(double f) {
	log.infof("convertToCelsius f=%s", f);
	return ((f - 32) * 5 / 9);

    }

    public double convertToFarenheit(double c) {
	log.debugf("convertToFarenheit c=%s", c);
	return ((c * 9 / 5) + 32);

    }

}
