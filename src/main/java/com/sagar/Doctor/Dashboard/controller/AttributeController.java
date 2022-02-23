package com.sagar.Doctor.Dashboard.controller;

import com.sagar.Doctor.Dashboard.entity.Attributes;
import com.sagar.Doctor.Dashboard.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping("api/attribute")
    public Attributes addAttribute(@RequestBody Attributes attributes) {
        return attributeService.addAttribute(attributes);
    }

    @GetMapping("api/attribute")
    public List<Attributes> getAllAttribute() {
        return attributeService.getAllAttribute();
    }


    @GetMapping("api/attribute/{id}")
    public Attributes getAttributeById(@PathVariable("id") Long id) {
        return attributeService.getAttributeById(id);
    }


    @PutMapping("api/attribute/{id}")
    public Attributes updateAttribute(@PathVariable("id") Long id, @RequestBody Attributes attributes) {
        return attributeService.updateAttribute(id, attributes);
    }

    @DeleteMapping("api/attribute/{id}")
    public String deleteAttributeById(@PathVariable("id") Long id) {
        attributeService.deleteAttributeById(id);
        return "Successfully Deleted";

    }
}
