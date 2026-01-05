package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.EmployeeEntity;
import com.example.multi_tanent.warehouse.mapper.EmployeeMapper;
import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import com.example.multi_tanent.warehouse.repository.DepartmentRepository;
import com.example.multi_tanent.warehouse.repository.EmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        EmployeeEntity employee = employeeMapper.toEntity(request);

        if (request.departmentId() != null) {
            DepartmentEntity department = departmentRepository.findById(request.departmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + request.departmentId()));
            employee.setDepartment(department);
        }

        EmployeeEntity saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> listAll() {
        return employeeRepository.findByActiveTrue()
                .stream()
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.toResponse(employee);
    }

}
