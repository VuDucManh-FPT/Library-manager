package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    Provider findByProviderId(int providerId);
}
