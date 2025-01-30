package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
//    @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.user.user_Id = :userId ORDER BY sp.subscription_Id DESC")
//    List<SubscriptionPlan> findLatestSubscriptionPlanByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT s.subscription_id FROM SubscriptionPlan s WHERE s.user_id = :userId ORDER BY s.subscription_id DESC LIMIT 1", nativeQuery = true)
    Long findLatestSubscriptionIdByUserId(@Param("userId") Long userId);


}
