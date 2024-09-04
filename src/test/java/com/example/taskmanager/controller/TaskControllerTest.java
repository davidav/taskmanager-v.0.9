package com.example.taskmanager.controller;

import com.example.taskmanager.AbstractTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

class TaskControllerTest extends AbstractTest {


    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "admin@mail.com",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
            void testLogin() throws Exception {


    }




}