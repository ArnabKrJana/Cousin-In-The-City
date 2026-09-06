package com.oneforth.cousininthecitybackend.repository

import com.oneforth.cousininthecitybackend.model.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, String>
