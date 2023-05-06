package fis.com.vn.repository;

import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.LogApiDetail;

public interface LogApiDetailRepository extends CrudRepository<LogApiDetail, Long>{

	LogApiDetail findByLogId(String orDefault);

}
