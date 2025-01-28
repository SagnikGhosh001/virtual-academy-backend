package com.smsv2.smsv2.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smsv2.smsv2.entity.Marks;

public interface MarksDao extends JpaRepository<Marks, Integer> {

	List<Marks> findByReg_Reg(String reg);
	List<Marks> findByReg_RegAndSemname(String reg,String sem);
	
	Optional<Marks> findByRegId(int id);
//	@Query("SELECT m FROM Marks m WHERE m.sub.id = :subId AND m.reg.id = :studentId")
	Optional<Marks> findBySubIdAndRegId( int subId, int studentId);

}
