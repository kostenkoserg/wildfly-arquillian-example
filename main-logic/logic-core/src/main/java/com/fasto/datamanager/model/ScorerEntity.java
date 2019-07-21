package com.fasto.datamanager.model;


import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.ScorerDto;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SCORER")
public class ScorerEntity implements Convertible<ScorerDto> {

    @Id
    @Column(name = "SCORER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scorerId;

    @Column(name = "NAME", nullable = false, length = 255)
    private String name;

    public long getScorerId() {
        return scorerId;
    }

    public void setScorerId(long scorerId) {
        this.scorerId = scorerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScorerEntity that = (ScorerEntity) o;
        return scorerId == that.scorerId &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(scorerId, name);
    }

    @Override
    public ScorerDto convert() {
        ScorerDto dto = new ScorerDto();
        dto.setScorerId(this.scorerId);
        dto.setName(this.name);

        return dto;
    }
}
