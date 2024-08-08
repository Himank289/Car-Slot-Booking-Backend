package vw.him.car.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vw.him.car.entity.Car;
import vw.him.car.exception.CarNotFoundException;
import vw.him.car.exception.NotAuthorizedException;
import vw.him.car.service.CarServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CarRestControllerTest {

    @Autowired
     MockMvc mockMvc;

    @Mock
    CarServiceImpl carService;

    @InjectMocks
     CarRestController carRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(carRestController).build();
    }


    @Test
    public void testGetAllCars() throws Exception {
        Car car1 = new Car(1L, "Toyota", "Red","Camry","Sedan","Dashing Sedan",102300L,2022,"https://imgd.aeplcdn.com/370x208/n/qcqkpua_1557411.jpg?q=80");
        Car car2 = new Car(2L, "Honda","Black", "Accord","Sedan","A 5 Seater Sedan",4344000L,2023,"https://imgd.aeplcdn.com/664x374/cw/ec/21613/Honda-Accord-Right-Front-Three-Quarter-82683.jpg?v=201711021421&q=80");
        List<Car> cars = Arrays.asList(car1, car2);

        when(carService.getAllCars()).thenReturn(cars);

        mockMvc.perform(get("/cars/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].brand").value("Toyota"));
    }


    @Test
    public void testGetCarById() throws Exception {
        Long carId = 1L;
        Car car1 = new Car(1L, "Toyota", "Red","Camry","Sedan","Dashing Sedan",102300L,2022,"https://imgd.aeplcdn.com/370x208/n/qcqkpua_1557411.jpg?q=80");

        when(carService.getCarById(carId)).thenReturn(Optional.of(car1));

        mockMvc.perform(get("/cars/{id}", carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));

    }


    @Test
    public void testGetCarById_NotFound() {
        Long carId = 1L;
        when(carService.getCarById(carId)).thenThrow(new CarNotFoundException("Car not found"));

        try {
            mockMvc.perform(get("/cars/{id}", carId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCreateCar() throws Exception {
        String jwt = "mock-jwt";
        Car car1 = new Car(1L, "Toyota", "Red","Camry","Sedan","Dashing Sedan",102300L,2022,"https://imgd.aeplcdn.com/370x208/n/qcqkpua_1557411.jpg?q=80");

        when(carService.createCar(eq(jwt), any(Car.class))).thenReturn(car1);

        mockMvc.perform(post("/cars/")
                        .header("Authorization", jwt)
                        .content(asJsonString(car1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"));

    }

    @Test
    public void testUpdateCar() throws Exception {
        String jwt = "mock-jwt";
        Long carId = 1L;

        Car carDetails = new Car(1L, "Toyota", "Blue","Camry","Sedan","Dashing Sedan",102300L,2022,"https://imgd.aeplcdn.com/370x208/n/qcqkpua_1557411.jpg?q=80");

        when(carService.updateCar(eq(jwt), eq(carId), any(Car.class))).thenReturn(Optional.of(carDetails));

        mockMvc.perform(put("/cars/{id}", carId)
                        .header("Authorization", jwt)
                        .content(asJsonString(carDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }


    @Test
    public void testUpdateCar_NotFound() throws Exception {
        String jwt = "mock-jwt";
        Long carId = 1L;

        Car carDetails = new Car(1L, "Toyota", "Blue","Camry","Sedan","Dashing Sedan",102300L,2022,"https://imgd.aeplcdn.com/370x208/n/qcqkpua_1557411.jpg?q=80");

        when(carService.updateCar(eq(jwt), eq(carId), any(Car.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/cars/{id}", carId)
                        .header("Authorization", jwt)
                        .content(asJsonString(carDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCar() throws Exception {
        String jwt = "mock-jwt";
        Long carId = 1L;

        when(carService.deleteCar(eq(jwt), eq(carId))).thenReturn(true);

        mockMvc.perform(delete("/cars/{id}", carId)
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testDeleteCar_NotAuthorized() {
        String jwt = "mock-jwt";
        Long carId = 1L;

        when(carService.deleteCar(eq(jwt), eq(carId))).thenThrow(new NotAuthorizedException("Unauthorized operation"));

        try {
            mockMvc.perform(delete("/cars/{id}", carId)
                            .header("Authorization", jwt)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }







}
