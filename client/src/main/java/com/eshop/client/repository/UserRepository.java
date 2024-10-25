package com.eshop.client.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.eshop.client.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long> {
	 Optional<UserEntity> findByUserName(String userName);
	 Optional<UserEntity> findByEmail(String email);
	 boolean existsByUid(String uid);
	Optional<UserEntity> findByUid(String uid);

	@Query("SELECT u.country.name AS country,"
			+ " COUNT(u) AS count,"
			+ " (COUNT(u) * 100.0 / (SELECT COUNT(ue) FROM UserEntity ue)) AS percent"
			+ " FROM UserEntity u"
			+ " GROUP BY u.country.name"
			+ " ORDER BY COUNT(u) DESC")
	List<CountryUsers> findAllUserCountByCountry();
}
