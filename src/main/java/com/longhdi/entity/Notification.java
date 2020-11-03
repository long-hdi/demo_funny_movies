package com.longhdi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(schema = "public", name = "notification")
@NamedQueries({
        @NamedQuery(name = "Notification.findByUserEmail", query = "select n FROM Notification n WHERE n.userEmail = :userEmail")
})
public class Notification implements Serializable {

    @Id
    @SequenceGenerator(name="notification_id_generator",
            sequenceName="notification_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="notification_id_generator")
    @Column(name = "id")
    private Long id;
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    @Column(name = "content", nullable = false)
    private String content;

    public Notification() {
    }

    public Notification(String userEmail, String content) {
        this.userEmail = userEmail;
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
