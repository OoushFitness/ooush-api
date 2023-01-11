package com.ooush.api.repository;

import com.ooush.api.entity.Exercise;
import com.ooush.api.specification.ExerciseSpecification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alex Green
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer>, JpaSpecificationExecutor<Exercise> {

	List<Exercise> findAll();

	@Query(value = "SELECT * FROM exercise ex WHERE (ex.bitmap & :searchBitmap) = :searchBitmap", nativeQuery = true)
	List<Exercise> findAll(@Param("searchBitmap") Integer searchBitmap);
	Exercise findUniqueById(Integer id);

}
