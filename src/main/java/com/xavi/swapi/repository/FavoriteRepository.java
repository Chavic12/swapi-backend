package com.xavi.swapi.repository;

import com.xavi.swapi.entity.Favorite;
import com.xavi.swapi.entity.ResourceType;
import com.xavi.swapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserOrderByCreatedAtDesc(User user);

    List<Favorite> findByUserAndResourceTypeOrderByCreatedAtDesc(User user, ResourceType resourceType);

    Optional<Favorite> findByUserAndResourceTypeAndResourceId(User user, ResourceType resourceType, String resourceId);

    boolean existsByUserAndResourceTypeAndResourceId(User user, ResourceType resourceType, String resourceId);

    void deleteByUserAndId(User user, Long id);
}
