package com.eshop.app.filter;

import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.Set;

@Setter
@ToString
public class UserFilter {
    private Long id;
    private String userName;
    private String title;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private Boolean active;
    private String uid;
    private Long parentId;
    private Boolean hasParent;
    private String treePath;
    private String walletAddress;
    private Set<Long> roles;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getTitle() {
        if (title == null || title.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(title);
    }

    public Optional<String> getUserName() {
        if (userName == null || userName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userName);
    }

    public Optional<String> getEmail() {
        if (email == null || email.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(email);
    }

    public Optional<String> getFirstName() {
        if (firstName == null || firstName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(firstName);
    }

    public Optional<String> getLastName() {
        if (lastName == null || lastName.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(lastName);
    }

    public Optional<Boolean> getActive() {
        return Optional.ofNullable(active);
    }

    public Optional<String> getUid() {
        if (uid == null || uid.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(uid);
    }

    public Optional<Long> getParentId() {
        return Optional.ofNullable(parentId);
    }

    public Optional<Boolean> getHasParent() {
        return Optional.ofNullable(hasParent);
    }

    public Optional<String> getTreePath() {
        if (treePath == null || treePath.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(treePath);
    }

    public Optional<String> getWalletAddress() {
        if (walletAddress == null || walletAddress.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(walletAddress);
    }

    public Optional<Set<Long>> getRoles() {
        return Optional.ofNullable(roles);
    }
}
