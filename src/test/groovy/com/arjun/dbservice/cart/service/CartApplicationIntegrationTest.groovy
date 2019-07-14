package com.arjun.dbservice.cart.service

import com.arjun.dbservice.cart.CartApplication
import com.arjun.dbservice.cart.controller.model.SendToMQRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@SpringBootTest(classes = CartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class CartApplicationIntegrationTest extends Specification {

    @Autowired
    MockMvc mvc

    ObjectMapper objectMapper = new ObjectMapper()

    def 'check sendDataToMQ()' (){
        given:
        SendToMQRequest request = new SendToMQRequest(productId: 'p1', userId: 'u1', quantity: 1)
        String data = objectMapper.writeValueAsString(request)

        when:
        ResultActions resultActions = mvc.perform(post("/data").content(data).contentType(MediaType.APPLICATION_JSON))

        then:
        assert resultActions
                .andExpect(content().string("{\"message\":\"Data send to MQ successfully\"}"))
    }
}
