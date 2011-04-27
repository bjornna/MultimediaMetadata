package org.hygga.integration;

import javax.inject.Inject;

import junit.framework.Assert;

import org.arquillian.examples.TemperatureConverter;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TemperatureConverterTestCase {
    @Inject
    private TemperatureConverter converter;

    @Deployment
    public static JavaArchive createTestArchive() {

	return ShrinkWrap.create(JavaArchive.class, "test.jar")
	.addClasses(TemperatureConverter.class)
	.addAsManifestResource(
	EmptyAsset.INSTANCE,
	ArchivePaths.create("beans.xml"));

    }

    @Test
    public void testConvertToCelsius() {
	Assert.assertEquals(converter.convertToCelsius(32d), 0d);
	Assert.assertEquals(converter.convertToCelsius(212d), 100d);

    }

    @Test
    public void testConvertToFarenheit() {

	Assert.assertEquals(converter.convertToFarenheit(0d), 32d);

	Assert.assertEquals(converter.convertToFarenheit(100d), 212d);

    }

}
