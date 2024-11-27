package com.eshop.app.service.impl;


import com.eshop.app.entity.OneTimePasswordEntity;
import com.eshop.app.entity.UserEntity;
import com.eshop.app.repository.OneTimePasswordRepository;
import com.eshop.app.service.OneTimePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OneTimePasswordServiceImpl implements OneTimePasswordService {
    private final OneTimePasswordRepository repository;

    @Override
    public JpaRepository<OneTimePasswordEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String create(UUID userId) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        var entity = new OneTimePasswordEntity();
        entity.setPassword(String.valueOf(otp));
        entity.setUser(new UserEntity(){{setId(userId);}});
        repository.save(entity);

        return String.valueOf(otp);
    }

    @Override
    public boolean verify(UUID userId, String password) {
        var optional = repository.findByUserIdAndPasswordAndConsumedFalse(userId, password);
        var verify = optional.isPresent() && !optional.get().isExpired();
        if(verify) {
            OneTimePasswordEntity entity = optional.get();
            entity.setConsumed(true);
            repository.save(entity);
        }
        return verify;
    }
}
