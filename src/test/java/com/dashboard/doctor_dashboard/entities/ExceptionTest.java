package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.exceptions.ErrorDetails;
import com.dashboard.doctor_dashboard.exceptions.ValidationsSchema;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class ExceptionTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(ErrorDetails.class);
        new BeanTester().testBean(ValidationsSchema.class);

    }
}
