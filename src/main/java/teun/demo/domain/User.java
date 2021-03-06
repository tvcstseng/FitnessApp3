package teun.demo.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@NoArgsConstructor(access =  AccessLevel.PUBLIC,force = true)
@Data
@Table(name="userTable")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Date createdAt;

    @NotBlank(message="Gebruikersnaam error, gebruik een andere")
    private String naam;

    @Pattern(regexp="^\\S*$", message = "Gebruikersnaam moet uit 1 woord bestaan")
    @NotBlank(message="Dit veld mag niet leeg zijn")
    private String gebruikersnaam;

    private String gewicht;
    private String lengte;

    @Pattern(regexp="((0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-[12]\\d{3})",message = "Gebruik het format dd-mm-jjjj")
    private String geboortedatum;

    @NotBlank
    @Email(message = "Gebruik een correcte email")
    private String email;

    @Pattern(regexp="^06\\d{8}$", message="Gebruik een 06 nummer")
    private String telefoonnummer;

    @PrePersist
    public void createdAt() {
        createdAt = new Date();
    }

    @ManyToMany
    @Size(min = 1, message = "Kies minimaal 1 groep.")
    private List<Group> groups = new ArrayList<>();

    @OneToMany
    private Set<ExerciseFact> exerciseFacts = new HashSet<>();

    @Override
    public String toString() {
        return this.naam;
    }

    public User(Long id,
                Date date,
                String naam,
                String gebruikersnaam,
                String gewicht,
                String lengte,
                String geboortedatum,
                String email,
                String telefoonnummer,
                List<Group> groups) {
        this.id = id;
        this.createdAt = date;
        this.naam = naam;
        this.gebruikersnaam = gebruikersnaam;
        this.gewicht = gewicht;
        this.lengte = lengte;
        this.geboortedatum = geboortedatum;
        this.email = email;
        this.telefoonnummer = telefoonnummer;
        this.groups = groups;
    }

}