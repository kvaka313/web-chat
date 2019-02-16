package com.infopulse.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "chatusers")
public class ChatUser {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Basic
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_visit")
    private Date lastVisit;

    @OneToOne(mappedBy = "chatuser",cascade = CascadeType.ALL, orphanRemoval = true)
    private Ban ban;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;


}
