package org.hygga.integration;

import java.util.Calendar;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.hygga.pictureservice.PersonService;
import org.hygga.pictureservice.domain.Person;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(Arquillian.class)
public class PersonServiceTestCase {

    private static final String PICASA_ID = "12345";
    @EJB
    PersonService personService;

    @Deployment
    public static JavaArchive createTestArchive() {
	return ShrinkWrap
		.create(JavaArchive.class, "test.jar")
		.addClasses(Person.class, PersonService.class)
		.addAsManifestResource(EmptyAsset.INSTANCE,
			ArchivePaths.create("beans.xml"))
		.addAsManifestResource("META-INF/test-persistence.xml",
			ArchivePaths.create("persistence.xml"));
    }

    @Test
    public void testSave() {
	Person person = new Person();
	person.setDisplay("DISPLAY");
	person.setEmail("test@test.no");
	person.setModifiedTime(Calendar.getInstance().getTime());
	person.setPicasaId(PICASA_ID);
	personService.saveAndFlush(person);

	Person found = personService.findByPicasaId(PICASA_ID);
	Assert.assertNotNull(found);
	Assert.assertEquals(PICASA_ID, found.getPicasaId());
    }
}
