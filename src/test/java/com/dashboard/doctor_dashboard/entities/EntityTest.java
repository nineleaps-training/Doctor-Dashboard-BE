package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.exceptions.ErrorDetails;
import com.dashboard.doctor_dashboard.exceptions.ValidationsSchema;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

public class EntityTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(Patient.class);
        new BeanTester().testBean(DoctorDetails.class);
        new BeanTester().testBean(Todolist.class);
        new BeanTester().testBean(Attributes.class);

    }
}
