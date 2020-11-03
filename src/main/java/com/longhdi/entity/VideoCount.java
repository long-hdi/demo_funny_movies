package com.longhdi.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "video_count")
@NamedQueries({
        @NamedQuery(name = "VideoCount.countAllVideo", query = "select c from VideoCount c WHERE c.criteria = 'all'"),
})
public class VideoCount {

    @Id
    @SequenceGenerator(name="video_count_id_generator",
            sequenceName="video_count_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="video_count_id_generator")
    private Long id;
    private Integer value;
    private String criteria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoCount)) return false;
        VideoCount that = (VideoCount) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "VideoCount{" +
                "id=" + id +
                ", value=" + value +
                ", criteria='" + criteria + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void increaseOne() {
        setValue(getValue() + 1);
    }
}
