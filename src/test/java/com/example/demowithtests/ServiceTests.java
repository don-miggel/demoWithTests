package com.example.demowithtests;

import com.example.demowithtests.domain.Address;
import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.EmployeeBannedDto;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.EmployeeServiceBean;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service Tests")
public class ServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceBean service;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = Employee
                .builder()
                .id(1)
                .name("Mark")
                .country("UK")
                .email("test@mail.com")
                .gender(Gender.M)
                .build();
    }

    @Test
    @DisplayName("Save employee test")
    public void whenSaveEmployee_shouldReturnEmployee() {

        when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(employee);
        var created = service.create(employee);
        assertThat(created.getName()).isSameAs(employee.getName());
        verify(employeeRepository).save(employee);
    }

    @Test
    @DisplayName("Get employee by exist id test")
    public void whenGivenId_shouldReturnEmployee_ifFound() {

        Employee employee = new Employee();
        employee.setId(88);
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        Employee expected = service.getById(employee.getId());
        assertThat(expected).isSameAs(employee);
        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    @DisplayName("Throw exception when employee not found test")
    public void should_throw_exception_when_employee_doesnt_exist() {

        when(employeeRepository.findById(anyInt())).thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> employeeRepository.findById(anyInt()));
    }

    @Test
    @DisplayName("Read employee by id test")
    public void readEmployeeByIdTest() {

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        Employee expected = service.getById(employee.getId());
        assertThat(expected).isSameAs(employee);
        verify(employeeRepository).findById(employee.getId());
    }

    @Test
    @DisplayName("Read all employees test")
    public void readAllEmployeesTest() {

        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        var list = employeeRepository.findAll();
        assertThat(list.size()).isGreaterThan(0);
        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("Delete employee test")
    public void deleteEmployeeTest() {

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        service.removeById(employee.getId());
        verify(employeeRepository).delete(employee);
    }

    @Test
    @DisplayName("Ban employee by id")
    public void testBanEmplById(){

        Optional<Employee>  employee = Optional.of(new Employee());
        employee.get().setId(111);

        Employee bannedEmployee = Employee.builder().id(111).isBanned(Boolean.TRUE).build();

        EmployeeBannedDto empl = new EmployeeBannedDto(employee.get(), "Abusive comments", (short) 31);

        doReturn(employee).when(employeeRepository).findById(empl.getId());
        doReturn(bannedEmployee).when(employeeRepository).save(employee.get());
        service.banEmployee(111, "Abusive comments", (short) 31);
        verify(employeeRepository, times(1)).findById(employee.get().getId());
        verify(employeeRepository, times(1)).save(employee.get());

    }

    @Test
    @DisplayName("Set all Ukrainians premium status")
    public void testSetAllUkrainiansPremiumStatus(){

        var empl1 = Employee.builder()
                .name("Ivan Petrenko")
                .country("Ukraine")
                .isDeleted(Boolean.FALSE)
                .isValid(Boolean.FALSE)
                .email("ipetr@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UA")
                                .city("Lviv")
                                .build())))
                .gender(Gender.M)
                .build();

        List<Employee> employees = List.of(empl1);
        doReturn(employees).when(employeeRepository).findAll();

        doReturn(empl1).when(employeeRepository).save(empl1);
        service.setAllUkrainiansPremiumStatus();
        verify(employeeRepository, times(1)).findAll();
        verify(employeeRepository, times(1)).save(empl1);

    }

    @Test
    @DisplayName("Change employee status")
    public void testSetEmployeeStatus(){

        var emplWithoutPremium = Optional.of(Employee.builder()

                .name("Ivan Petrenko")
                .country("Ukraine")
                .isDeleted(Boolean.FALSE)
                .isValid(Boolean.FALSE)
                .email("ipetr@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UA")
                                .city("Lviv")
                                .build())))
                .gender(Gender.M)
                .build()
        );

        var emplWithPremium = Employee.builder()
                .name("Ivan Petrenko")
                .country("Ukraine")
                .isDeleted(Boolean.FALSE)
                .isValid(Boolean.TRUE)
                .email("ipetr@gmail.com")
                .addresses(new HashSet<>(Set.of(
                        Address
                                .builder()
                                .country("UA")
                                .city("Lviv")
                                .build())))
                .gender(Gender.M)
                .build();

        doReturn(emplWithoutPremium).when(employeeRepository).findById(111);
        doReturn(emplWithPremium).when(employeeRepository).save(emplWithoutPremium.get());
        service.changeValidStatus(111);
        verify(employeeRepository, times(1)).findById(111);
        verify(employeeRepository, times(1)).save(emplWithoutPremium.get());

   }

}



