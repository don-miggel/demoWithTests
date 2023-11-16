package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.DeletedEmployeeDto;
import com.example.demowithtests.dto.EmployeeReadDto;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.service.emailSevice.EmailSenderService;
import com.example.demowithtests.util.annotations.entity.ActivateCustomAnnotations;
import com.example.demowithtests.util.annotations.entity.Name;
import com.example.demowithtests.util.annotations.entity.ToLowerCase;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import com.example.demowithtests.util.exception.ResourceWasDeletedException;
import com.example.demowithtests.util.mappers.EmployeeMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class EmployeeServiceBean implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmailSenderService emailSenderService;
    private final EmployeeMapper employeeMapper;

    @Override
    @ActivateCustomAnnotations({Name.class, ToLowerCase.class})
    // @Transactional(propagation = Propagation.MANDATORY)
    public Employee create(Employee employee) {
        //return employeeRepository.save(employee);
        return employeeRepository.saveAndFlush(employee);
    }

    /**
     * @param employee
     * @return
     */
    @Override
    public void createAndSave(Employee employee) {
        employeeRepository.saveEmployee(employee.getName(), employee.getEmail(), employee.getCountry(), String.valueOf(employee.getGender()));
    }

    // This method selects all active employees, the results are used by another methods, like getAll()
    private List<Employee> getAllActiveEmployees(){
        return employeeRepository
                .findAll()
                .stream()
                .filter(emp->emp.getIsDeleted()
                        .equals(Boolean.FALSE))
                .toList();
    }

    @Override
    public List<EmployeeReadDto> getAll() {

        return getAllActiveEmployees().stream().map(emp->employeeMapper.toEmployeeReadDto(emp)).toList();
    }

    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        log.debug("getAllWithPagination() - start: pageable = {}", pageable);
        Page<Employee> list = employeeRepository.findAll(pageable);
        log.debug("getAllWithPagination() - end: list = {}", list);
        return list;
    }

    @Override
    public Employee getById(Integer id) {

        return getEmployee(id);
        /*
        var employee = employeeRepository.findById(id)
              .orElseThrow(ResourceNotFoundException::new);
        if (employee.getIsDeleted()) {
            throw new EntityNotFoundException("Employee was deleted with id = " + id);
        }
        return employee;

         */
    }

    @Override
    public Employee updateById(Integer id, Employee employee) {

        var empToUpd = getEmployee(id);

        empToUpd.setName(employee.getName());
        empToUpd.setEmail(employee.getEmail());
        empToUpd.setCountry(employee.getCountry());
        return employeeRepository.save(empToUpd);
        /*
        return employeeRepository.findById(id).filter(empl -> empl.getIsDeleted().equals(Boolean.FALSE))
                .map(entity -> {
                    entity.setName(employee.getName());
                    entity.setEmail(employee.getEmail());
                    entity.setCountry(employee.getCountry());
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));

         */
    }

    //It is a private method, which purpose to select active and existing in the DB users
    private Employee getEmployee(Integer id){

        var employeeFound = employeeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Employee not found with id = " + id));
        if(employeeFound.getIsDeleted().equals(Boolean.TRUE))
            throw new ResourceWasDeletedException();
        return employeeFound;
    }

    //Set Premium status to all Ukrainians
    public void setAllUkrainiansPremiumStatus(){
        getAllActiveEmployees().stream().filter(emp->
                        emp.getCountry().equals("Ukraine") && emp.getIsValid().equals(Boolean.FALSE))
                .forEach(emp->
                {
                    emp.setIsValid(Boolean.TRUE);
                    employeeRepository.save(emp);
                }
                );
    }

    //Change Employee status, this method acts like a toggle, if status is FALSe, it changes it to TRUE
    // and vice versa
    public Employee changeValidStatus(Integer id){
        Employee emp = getEmployee(id);
        if (emp.getIsValid().equals(Boolean.FALSE))
            emp.setIsValid(Boolean.TRUE);
        else
            emp.setIsValid(Boolean.FALSE) ;
        employeeRepository.save(emp);
        return emp;
    }

    @Override
    public DeletedEmployeeDto removeById(Integer id) {
        //repository.deleteById(id);
        var employee = getEmployee(id);
        employee.setIsDeleted(Boolean.TRUE);
        //employeeRepository.delete(employee);
        Employee deletedEmployee = employeeRepository.save(employee);
        return new DeletedEmployeeDto(deletedEmployee);
    }

    @Override
    public List<DeletedEmployeeDto> removeAll() {
        List<Employee> allEmployees = employeeRepository.findAll();
        allEmployees
                .stream()
                .filter(
                        emp ->emp
                                .getIsDeleted().equals(Boolean.FALSE)
                )
                .forEach(emp->{
                    emp.setIsDeleted(Boolean.TRUE);
                    employeeRepository.save(emp);
                }
                );
        return employeeRepository.findAll()
                .stream()
                .filter(emp->
                        emp.getIsDeleted()
                                .equals(Boolean.TRUE)
                )
                .map(DeletedEmployeeDto::new)
                .toList();
    }

    /*@Override
    public Page<Employee> findByCountryContaining(String country, Pageable pageable) {
        return employeeRepository.findByCountryContaining(country, pageable);
    }*/

    @Override
    public Page<Employee> findByCountryContaining(String country, int page, int size, List<String> sortList, String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        return employeeRepository.findByCountryContaining(country, pageable);
    }

    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }

    @Override
    public List<String> getAllEmployeeCountry() {
        log.info("getAllEmployeeCountry() - start:");
        List<Employee> employeeList = getAllActiveEmployees();
        List<String> countries = employeeList.stream()
                .map(country -> country.getCountry())
                .collect(Collectors.toList());
        /*List<String> countries = employeeList.stream()
                .map(Employee::getCountry)
                //.sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());*/

        log.info("getAllEmployeeCountry() - end: countries = {}", countries);
        return countries;
    }

    @Override
    public List<String> getSortCountry() {
        List<Employee> employeeList = getAllActiveEmployees();
        return employeeList.stream()
                .map(Employee::getCountry)
                .filter(c -> c.startsWith("U"))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<String> findEmails() {
        var employeeList = getAllActiveEmployees();

        var emails = employeeList.stream()
                .map(Employee::getEmail)
                .collect(Collectors.toList());

        var opt = emails.stream()
                .filter(s -> s.endsWith(".com"))
                .findFirst()
                .orElse("error?");
        return Optional.ofNullable(opt);
    }

    @Override
    public List<EmployeeReadDto> filterByCountry(String country) {
        return employeeRepository.findEmployeesByCountry(country)
                .stream()
                .map(employee ->employeeMapper.toEmployeeReadDto(employee)).toList();
    }

    @Override
    public Set<String> sendEmailsAllUkrainian() {
        var ukrainians = employeeRepository.findAllUkrainian()
                .orElseThrow(() -> new EntityNotFoundException("Employees from Ukraine not found!"));
        var emails = new HashSet<String>();
        ukrainians.forEach(employee -> {
            emailSenderService.sendEmail(
                    /*employee.getEmail(),*/
                    "kaluzny.oleg@gmail.com", //для тесту
                    "Need to update your information",
                    String.format(
                            "Dear " + employee.getName() + "!\n" +
                                    "\n" +
                                    "The expiration date of your information is coming up soon. \n" +
                                    "Please. Don't delay in updating it. \n" +
                                    "\n" +
                                    "Best regards,\n" +
                                    "Ukrainian Info Service.")
            );
            emails.add(employee.getEmail());
        });

        return emails;
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<EmployeeReadDto> findByNameContaining(String name) {
        return employeeRepository.findByNameContaining(name)
                .stream()
                .map(emp->employeeMapper.toEmployeeReadDto(emp)).toList();
    }

    /**
     * @param name
     * @param id
     * @return
     */
    @Override
    public void updateEmployeeByName(String name, Integer id) {
        /*var employee = employeeRepository.findById(id)
                .map(entity -> {
                    entity.setName(name);
                    return employeeRepository.save(entity);
                })
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id = " + id));
        return employee;*/

        employeeRepository.updateEmployeeByName(name, id);
    }


}
