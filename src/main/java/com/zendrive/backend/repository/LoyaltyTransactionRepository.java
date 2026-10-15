package com.zendrive.backend.repository;

import com.zendrive.backend.entity.LoyaltyTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    Page<LoyaltyTransaction> findByLoyaltyAccountId(Long loyaltyAccountId, Pageable pageable);
    Page<LoyaltyTransaction> findByLoyaltyAccountUserId(Long userId, Pageable pageable);
}
