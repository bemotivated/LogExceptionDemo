package com.example.demo.Dao;


import com.example.demo.Entity.ExceptionLogExt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionLogRepository extends JpaRepository<ExceptionLogExt, String>,JpaSpecificationExecutor {
    //https://www.jianshu.com/p/376872de82c7
   /* @Query(value = "select f from FileVO f where f.clientfilename = ?1 and f.md5 = ?2 ")
    List<FileVO> findByClientFileNameAndMD5(String clientfilename, String md5);*/


}