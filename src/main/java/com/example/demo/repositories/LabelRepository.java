package com.example.demo.repositories;

import com.example.demo.models.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query("SELECT l FROM Label l WHERE l.user.id_user = :id_user")
    List<Label> findByUserId(@Param("id_user") Long id_user);
}
