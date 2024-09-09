package com.eshop.client.service.impl;

import com.eshop.client.entity.OneTimePasswordEntity;
import com.eshop.client.entity.UserEntity;
import com.eshop.client.repository.OneTimePasswordRepository;
import com.eshop.client.service.OneTimePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OneTimePasswordServiceImpl implements OneTimePasswordService {
    private final OneTimePasswordRepository repository;

    @Override
    public JpaRepository<OneTimePasswordEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String create(long userId) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);

        var entity = new OneTimePasswordEntity();
        entity.setPassword(String.valueOf(otp));
        entity.setUser(new UserEntity(){{setId(userId);}});
        repository.save(entity);

        return String.valueOf(otp);
    }

    @Override
    public boolean verify(long userId, String password) {
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
