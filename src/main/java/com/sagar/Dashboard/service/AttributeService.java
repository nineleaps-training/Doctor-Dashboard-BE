package com.sagar.Dashboard.service;

import com.sagar.Dashboard.entity.Attributes;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AttributeService {
    public Attributes addAttribute(Attributes attributes);
    public List<Attributes> getAllAttribute();
    public Attributes getAttributeById(Long id);
    public Attributes updateAttribute(Long id,Attributes attributes);
    public void deleteAttributeById(Long id);
}
