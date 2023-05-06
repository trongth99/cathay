package fis.com.vn.repository;

import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.LogApiDetail;
import fis.com.vn.table.LogApiDetailSeaBank;

public interface LogApiDetailSBRepository extends CrudRepository<LogApiDetailSeaBank, Long> {

	LogApiDetailSeaBank findByLogId(String orDefault);

}
