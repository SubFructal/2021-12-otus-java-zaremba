package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone implements Cloneable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "phone_number")
    private String number;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    protected Phone clone() {
        return new Phone(this.id, this.number, this.client);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", client=" + client +
                '}';
    }
}
