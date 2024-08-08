package vw.him.car.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vw.him.car.exception.CarNotFoundException;
import vw.him.car.utils.JwtUtils;
import vw.him.car.entity.Car;
import vw.him.car.repository.CarRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
     CarRepo carrepository;

    @InjectMocks
    CarServiceImpl carservice;

    @Test
    void testGetAllCars(){
        List<Car> lst=new ArrayList<>();
        lst.add(new Car());
        lst.add(new Car());

        when(carrepository.findAll()).thenReturn(lst);

        List<Car>carList=carservice.getAllCars();
        assertNotNull(carList);
        assertEquals(lst.size(),carList.size());
        assertEquals(lst,carList);
    }

    @Test
    void testGetCarById(){
        Long id=1L;
        Car c =new Car();
        when(carrepository.findById(id)).thenReturn(Optional.of(c));

        Optional<Car> foundcar=carservice.getCarById(id);
        assertNotNull(foundcar);
        verify(carrepository).findById(id);

    }

    @Test
    void testGetCarById_CarNotFound() {

        Long id = 1L;
        when(carrepository.findById(id)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(CarNotFoundException.class, () -> carservice.getCarById(id));

        assertNotNull(exception);
        assertEquals("Car Not Found", exception.getMessage());

        verify(carrepository, times(1)).findById(id);
    }

    @Test
    void testDecodeJWT() {

        String jwt = "eyJhbGciOiJIUzM4NCJ9.eyJpYXQiOjE3MjA2NzY0MzYsImV4cCI6MTcyMDc2MjgzNiwiZW1haWwiOiJuaXJtYWxAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX2FkbWluIn0.rigXgyQSQwXXdQ2Zh_sCMnK249eScWGq6mryz0A-kiUaWRvWU4lQ2imwcIVxQc3u";

        String decodedPayload = JwtUtils.decodeJWT(jwt);

        System.out.println(decodedPayload);
        assertNotNull(decodedPayload);
        assertTrue(decodedPayload.contains("\"email\":\"nirmal@gmail.com\""));
        assertTrue(decodedPayload.contains("\"authorities\":\"ROLE_admin\""));
    }

    @Test
    void testDecodeJWTInvalidFormat() {
        String invalidJwt = "eyJhbGciOiJIUzM4NCJ9eyJpYXQiOjE3MjA2NzY0MzYsImV4cCI6MTcyMDc2MjgzNiwiZW1haWwiOiJuaXJtYWxAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOiJST0xFX2FkbWluIn0rigXgyQSQwXXdQ2Zh_sCMnK249eScWGq6mryz0A-kiUaWRvWU4lQ2imwcIVxQc3u";

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> JwtUtils.decodeJWT(invalidJwt));


        assertNotNull(exception);
        assertEquals("Invalid JWT format", exception.getMessage());
    }


}