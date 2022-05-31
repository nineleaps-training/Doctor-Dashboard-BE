package com.dashboard.doctor_dashboard.entities;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

 class EntityTest {
    @Test
    void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(Patient.class);
        new BeanTester().testBean(DoctorDetails.class);
        new BeanTester().testBean(Todolist.class);
        new BeanTester().testBean(Attributes.class);

    }
}
