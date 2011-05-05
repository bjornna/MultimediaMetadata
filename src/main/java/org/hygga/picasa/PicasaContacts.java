package org.hygga.picasa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hygga.pictureservice.domain.Person;

public class PicasaContacts {

	private Map<String, Person> persons = new HashMap<String, Person>();

	public void add(Person person) {
		if (person == null || person.getPicasaId() == null) {
			throw new IllegalArgumentException(
					"Person must not be null or picasa id must be set for person: "
							+ person);
		}
		persons.put(person.getPicasaId(), person);
	}

	public Collection<Person> getPersons() {
		return persons.values();
	}

	public Person get(String picasaId) {
		if (picasaId == null) {
			throw new IllegalArgumentException(
					"Parameter picasaId should not be null");
		}
		if (persons.containsKey(picasaId)) {
			return persons.get(picasaId);
		} else {
			return null;
		}
	}
}
