package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.SystemPropertyDto;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_PROPERTY")
public class SystemPropertyEntity implements Convertible<SystemPropertyDto> {

    private String key;
    private String value;
    private String description;

    @Id
    @Column(name = "NAME", nullable = false)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "VALUE", nullable = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SystemPropertyEntity that = (SystemPropertyEntity) o;
        return Objects.equals(key, that.key)
                && Objects.equals(value, that.value)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, description);
    }

    @Override
    public SystemPropertyDto convert() {
        SystemPropertyDto dto = new SystemPropertyDto();
        dto.setKey(this.key);
        dto.setValue(this.value);
        dto.setDescription(this.description);
        return dto;
    }
}
