package app.dao.security;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.security.SRole;


public interface SRoleRepository extends JpaRepository<SRole,Long> {



}