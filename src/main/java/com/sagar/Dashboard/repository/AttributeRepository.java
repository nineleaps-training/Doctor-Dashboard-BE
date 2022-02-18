package com.sagar.Dashboard.repository;

import com.sagar.Dashboard.entity.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes,Long> {
}
