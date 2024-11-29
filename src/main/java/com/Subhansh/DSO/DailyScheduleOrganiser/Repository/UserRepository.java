package com.Subhansh.DSO.DailyScheduleOrganiser.Repository;

import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.Role;
import com.Subhansh.DSO.DailyScheduleOrganiser.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findAllUsersWithRole(@Param("role") Role role);



    List<User> findByManager(User manager);
}
