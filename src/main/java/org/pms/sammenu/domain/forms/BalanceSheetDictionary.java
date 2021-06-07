package org.pms.sammenu.domain.forms;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "isologismosdictionary")
public class BalanceSheetDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "line")
    private String line;

    @Column(name = "type")
    private String type;

    @Column(name = "nlslang")
    private String language;

    @Column(name = "amount")
    private Double amount;
}
