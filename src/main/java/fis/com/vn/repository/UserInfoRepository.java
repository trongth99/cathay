package fis.com.vn.repository;

import org.springframework.data.repository.CrudRepository;

import fis.com.vn.table.UserInfo;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);

    long countByUsername(String userName);
}
