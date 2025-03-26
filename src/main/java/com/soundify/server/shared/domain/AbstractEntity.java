package com.soundify.server.shared.domain;

import com.soundify.server.shared.persistence.IdJavaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JavaTypeRegistration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@JavaTypeRegistration(javaType = Id.class, descriptorClass = IdJavaType.class)
public abstract class AbstractEntity{

    @jakarta.persistence.Id
    protected Id id;
    @CreatedDate
    protected LocalDateTime createdAt;
    @LastModifiedDate
    protected LocalDateTime updateAt;
    @ColumnDefault("false")
    @Setter
    protected boolean deleted;


    public AbstractEntity(Id id) {
        this.id = id;
    }

    protected AbstractEntity(){}


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
