package com.eshop.app.entity;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
@Entity
@Where(clause = "deleted=false")
@Table(name = "tbl_notification")
@Audited
public class NotificationEntity extends BaseEntity<Long> implements LogicalDeleted {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notification")
    @SequenceGenerator(name = "seq_notification", sequenceName = "seq_notification", allocationSize = 1, initialValue = 1)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private UserEntity sender;
    @ManyToOne
    @JoinColumn(name = "recipient")
    private UserEntity recipient;
    private String subject;
    private String body;
    @Column(name = "is_read", nullable = false)
    private boolean read;
    @Column(nullable = false)
    private boolean deleted;
    private String role;

    @Override
    public String getSelectTitle() {
        return subject;
    }
}