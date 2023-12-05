package com.example.demowithtests;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.linesOf;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Employee Repository Tests")
public class RepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;


    @Test
    @Order(1)
    @Rollback(value = false)
    @DisplayName("Save employee test")
    public void saveEmployeeTest() {

        var employee = Employee.builder()
                .name("Mark")
                .country("England")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UK")
                                .build())))
                .gender(Gender.M)
                .build();

        employeeRepository.save(employee);

        Assertions.assertThat(employee.getId()).isGreaterThan(0);
        Assertions.assertThat(employee.getId()).isEqualTo(1);
        Assertions.assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(2)
    @DisplayName("Get employee by id test")
    public void getEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        Assertions.assertThat(employee.getId()).isEqualTo(1);
        Assertions.assertThat(employee.getName()).isEqualTo("Mark");
    }

    @Test
    @Order(3)
    @DisplayName("Get employees test")
    public void getListOfEmployeeTest() {

        var employeesList = employeeRepository.findAll();

        Assertions.assertThat(employeesList.size()).isGreaterThan(0);

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("Update employee test")
    public void updateEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employee.setName("Martin");
        var employeeUpdated = employeeRepository.save(employee);

        Assertions.assertThat(employeeUpdated.getName()).isEqualTo("Martin");

    }

    @Test
    @Order(5)
    @DisplayName("Find employee by gender test")
    public void findByGenderTest() {

        var employees = employeeRepository.findByGender(Gender.M.toString(), "UK");

        assertThat(employees.get(0).getGender()).isEqualTo(Gender.M);
    }

    @Test
    @Order(6)
    @Rollback(value = false)
    @DisplayName("Delete employee test")
    public void deleteEmployeeTest() {

        var employee = employeeRepository.findById(1).orElseThrow();

        employeeRepository.delete(employee);

        Employee employeeNull = null;

        var optionalEmployee = Optional.ofNullable(employeeRepository.findByName("Martin"));

        if (optionalEmployee.isPresent()) {
            employeeNull = optionalEmployee.orElseThrow();
        }

        Assertions.assertThat(employeeNull).isNull();
    }

    @Test
    @DisplayName("Find employees by countries and titles")
    public void testEmplByTitlesAndCountries(){

        var empl1 = Employee.builder()
                .name("Dr. Jose Flores")
                .country("Spain")
                .email("drjoseflor@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("ES")
                                .city("Barcelona")
                                .build())))
                .gender(Gender.M)
                .build();

        var empl2 = Employee.builder()
                .name("Dr. Perdo Sanches")
                .country("Spain")
                .email("drperdos@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("ES")
                                .city("Madrid")
                                .build())))
                .gender(Gender.M)
                .build();

        var empl3 = Employee.builder()
                .name("Mr. Joe Doe")
                .country("USA")
                .email("jdoe@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("US")
                                .city("Dallas")
                                .build())))
                .gender(Gender.M)
                .build();
        employeeRepository.save(empl1);
        employeeRepository.save(empl2);
        employeeRepository.save(empl3);

        List<Employee> extractedEmployees = employeeRepository.findEmployeesByCountryAndNameStartsWith("Spain", "Dr.");
        Assertions.assertThat(extractedEmployees.size()).isEqualTo(2);
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getCountry().equals("Spain")));
        Assertions.assertThat(extractedEmployees.get(0).getEmail()).isEqualTo("drjoseflor@gmail.com");
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getName().startsWith("Dr.")));
        Assertions.assertThat(extractedEmployees.get(0).getAddresses().stream().toList().get(0).getCity().equals("Barcelona"));


        extractedEmployees = employeeRepository.findEmployeesByCountryAndNameStartsWith("USA", "Mr.");
        Assertions.assertThat(extractedEmployees.size()).isEqualTo(1);
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getCountry().equals("USA")));
        Assertions.assertThat(extractedEmployees.get(0).getEmail()).isEqualTo("jdoe@gmail.com");
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getName().startsWith("Mr.")));
        Assertions.assertThat(extractedEmployees.get(0).getAddresses().stream().toList().get(0).getCity().equals("Dallas"));
    }

    @Test
    @DisplayName("Find employees by name containing")
    public void testFindEmployeesByNameContaining() {

        var empl1 = Employee.builder()
                .name("Dr. John Davies")
                .country("United Kingdom of Great Britain and Northern Ireland")
                .email("drjohn.doe@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UK")
                                .city("Liverpool")
                                .build())))
                .gender(Gender.M)
                .build();

        var empl3 = Employee.builder()
                .name("Mr. Joe Neilson")
                .country("Kingdom of Denmark")
                .email("jdoe@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("DK")
                                .city("Copenhagen")
                                .build())))
                .gender(Gender.M)
                .build();

        var empl4 = Employee.builder()
                .name("James Johnson")
                .country("Federated States of Micronesia")
                .email("jjohnson@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("FM")
                                .city("Palikir")
                                .build())))
                .gender(Gender.M)
                .build();

        var empl5 = Employee.builder()
                .name("Joe O'Neil")
                .country("Republic of Ireland")
                .email("joneil@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("IE")
                                .city("Dublin")
                                .build())))
                .gender(Gender.M)
                .build();

        employeeRepository.save(empl1);
        employeeRepository.save(empl3);
        employeeRepository.save(empl4);
        employeeRepository.save(empl5);

        List<Employee> extractedEmployees = employeeRepository.findByNameContaining("John");
        Assertions.assertThat(extractedEmployees.size()).isEqualTo(2);
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getName().trim().contains("john")));

        extractedEmployees = employeeRepository.findByNameContaining("Neil");
        Assertions.assertThat(extractedEmployees.size()).isEqualTo(2);
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getName().trim().contains("neil")));

        extractedEmployees = employeeRepository.findByNameContaining("Joe");
        Assertions.assertThat(extractedEmployees.size()).isEqualTo(2);
        Assertions.assertThat(extractedEmployees.stream().allMatch(emp->emp.getName().trim().contains("joe")));
    }

}
