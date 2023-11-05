package study.demo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.demo.enums.EBookStatus;

@Entity
@Table(name = "book")
@Data
@Builder
@AllArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(name = "title", columnDefinition = "varchar(50)")
    private String title;

    @Column(name = "edition", columnDefinition = "varchar(50)")
    private String edition;

    @Column(name = "price", columnDefinition = "decimal(9,3)")
    private BigDecimal price;

    @Column(name = "quantity", columnDefinition = "Integer")
    private Integer quantity;

    @Column(name = "image", columnDefinition = "varchar(255)")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status", columnDefinition = "varchar(10)")
    private EBookStatus bookStatus;

    @ManyToOne
    @JoinColumn(name = "cateory_id", nullable = false)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
    private List<BorrowerRecord> borrowerRecord;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
