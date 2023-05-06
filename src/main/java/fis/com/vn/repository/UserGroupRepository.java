package fis.com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fis.com.vn.table.UserGroup;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long>{

    @Query(value = "select * from user_group where (:gname is null or group_name like %:gname%) " +
            "and (:desc is null or description like %:desc%) " +
            "and (:status is null or status = :status)",
            countQuery = "select count(1) from user_group where (:gname is null or group_name like %:gname%) " +
                    "and (:desc is null or description like %:desc%) " +
                    "and (:status is null or status = :status)", nativeQuery = true)
    Page<UserGroup> selectParams( @Param("gname") String gname,
                                  @Param("desc") String desc,
                                  @Param("status") Integer status,
                                  Pageable pageable);

    Iterable<UserGroup> findByStatus(Integer ttNhomHoatdong);

    long countByGroupName(String name);

    @Query(nativeQuery = true,
    value = "select count(1) from user_group where group_name = :name and (:id is null or id != :id)")
    long countUpdate(@Param("name") String name,@Param("id") Long id);

}
