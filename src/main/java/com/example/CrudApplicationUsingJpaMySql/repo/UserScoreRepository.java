package com.example.CrudApplicationUsingJpaMySql.repo;

import com.example.CrudApplicationUsingJpaMySql.entity.UserScore;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.util.List;

@EnableJpaRepositories
public interface UserScoreRepository extends JpaRepository<UserScore, Long> {

    @Query("from UserScore")
    List<UserScore> findAllInfo();
}
