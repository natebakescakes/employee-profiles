package com.example.employeeprofiles.controller;

import com.example.employeeprofiles.model.Employee;
import com.example.employeeprofiles.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    public void whenGet_thenReturns200() throws Exception {
        mockMvc.perform(get("/employee")).andExpect(status().isOk());
    }

    @Test
    public void whenGet_thenMapsToRepository() throws Exception {
        // when
        mockMvc.perform(get("/employee"));

        // then
        verify(employeeRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    public void whenPostValidInput_thenReturns200() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        mockMvc.perform(post("/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostNullValue_thenReturns400() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");

        mockMvc.perform(post("/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostValidInput_thenMapsToRepository() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        mockMvc.perform(post("/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"));

        // then
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Alex");
        assertThat(employeeCaptor.getValue().getLastName()).isEqualTo("Test");
    }

}