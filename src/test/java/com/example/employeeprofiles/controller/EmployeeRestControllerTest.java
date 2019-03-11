package com.example.employeeprofiles.controller;

import com.example.employeeprofiles.model.Employee;
import com.example.employeeprofiles.repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void whenGetEmployee_thenReturns200() throws Exception {
        mockMvc.perform(get("/api/employee")).andExpect(status().isOk());
    }

    @Test
    public void whenGetEmployee_thenMapToFindAll() throws Exception {
        // when
        mockMvc.perform(get("/api/employee"));

        // then
        verify(employeeRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @Ignore
    public void whenGetEmployee_thenReturnsPageRequestJson() {
        throw new NotImplementedException();
    }

    @Test
    public void whenGetEmployeeSingleValid_thenReturns200() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));

        mockMvc.perform(get("/api/employee/1")).andExpect(status().isOk());
    }

    @Test
    public void whenGetEmployeeSingleInvalid_thenReturns404() throws Exception {
        mockMvc.perform(get("/api/employee/1")).andExpect(status().isNotFound());
    }

    @Test
    public void whenGetEmployeeSingleValid_thenMapToFindById() throws Exception {
        // when
        mockMvc.perform((get("/api/employee/1")));

        // then
        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void whenGetEmployeeSingle_thenReturnsEmployeeJson() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));

        MvcResult mvcResult = mockMvc.perform(get("/api/employee/1")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json")).andReturn();

        // then
        assertThat(objectMapper.writeValueAsString(alex))
                .isEqualToIgnoringWhitespace(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void whenPostEmployeeWithValidInput_thenReturns200() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        mockMvc.perform(post("/api/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPostEmployeeWithInvalidInput_thenReturns400() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");

        mockMvc.perform(post("/api/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenPostEmployeeWithValidInput_thenMapsToSave() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        mockMvc.perform(post("/api/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"));

        // then
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Alex");
        assertThat(employeeCaptor.getValue().getLastName()).isEqualTo("Test");
    }

    @Test
    public void whenPostEmployeeWithValidInput_thenReturnsEmployeeJson() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        when(employeeRepository.save(any(Employee.class))).thenReturn(alex);

        MvcResult mvcResult = mockMvc.perform(post("/api/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json")).andReturn();

        // then
        assertThat(objectMapper.writeValueAsString(alex))
                .isEqualToIgnoringWhitespace(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void whenPutEmployeeWithValidInput_thenReturns200() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));
        when(employeeRepository.save(any(Employee.class))).thenReturn(alex);

        // then
        mockMvc.perform(put("/api/employee/1")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutEmployeeWithInvalidInput_thenReturns404() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // then
        mockMvc.perform(put("/api/employee/2")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenPutEmployeeWithValidInput_thenMapsToSave() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));
        mockMvc.perform(post("/api/employee")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json"));

        // then
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Alex");
        assertThat(employeeCaptor.getValue().getLastName()).isEqualTo("Test");
    }

    @Test
    public void whenPutEmployeeWithValidInput_thenReturnsEmployeeJson() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));
        when(employeeRepository.save(any(Employee.class))).thenReturn(alex);

        MvcResult mvcResult = mockMvc.perform(put("/api/employee/1")
                .content(objectMapper.writeValueAsString(alex))
                .contentType("application/json")).andReturn();

        // then
        assertThat(objectMapper.writeValueAsString(alex))
                .isEqualToIgnoringWhitespace(mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void whenDeleteEmployeeWithValidId_thenReturns200() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));

        // then
        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteEmployeeWithInvalidId_thenReturns404() throws Exception {
        // given
        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteEmployeeWithValidId_thenMapsToDelete() throws Exception {
        // given
        Employee alex = new Employee();
        alex.setFirstName("Alex");
        alex.setLastName("Test");

        // when
        when(employeeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(alex));
        mockMvc.perform(delete("/api/employee/1"));

        // then
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, times(1)).delete(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getFirstName()).isEqualTo("Alex");
        assertThat(employeeCaptor.getValue().getLastName()).isEqualTo("Test");
    }
}