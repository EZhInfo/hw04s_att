package com.example.hw04s_att.services;

import com.example.hw04s_att.models.Person;
import com.example.hw04s_att.repositories.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService {
    
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    
    
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Person findByLogin(Person person) {
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }
    
    @Transactional
    public void registerPerson(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }
    
}
