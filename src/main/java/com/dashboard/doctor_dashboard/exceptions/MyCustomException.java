package com.dashboard.doctor_dashboard.exceptions;

public class MyCustomException extends RuntimeException {
    public final String resourceName;
    public final String fieldName;
    public final long fieldValue;

    public MyCustomException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found in Doctor List with %s : %s", resourceName, fieldName, fieldValue)); // Post not found with id : 1
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}