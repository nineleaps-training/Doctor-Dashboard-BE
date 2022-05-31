package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.entities.dtos.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class WrapperTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(ErrorMessage.class);
    }
}
