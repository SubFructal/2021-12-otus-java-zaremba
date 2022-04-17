package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address implements Cloneable {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "street")
    private String street;

    @Getter
    @Setter
    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private Client client;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(this.id, this.street, this.client);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", client=" + client +
                '}';
    }
}
