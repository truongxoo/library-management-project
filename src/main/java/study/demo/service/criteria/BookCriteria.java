package study.demo.service.criteria;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.StringFilter;
@Data
@Builder
@AllArgsConstructor
public class BookCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private IntegerFilter id;

    private StringFilter edition;

    private StringFilter title;

    private IntegerFilter quantity;

    private IntegerFilter categoryId;

    private IntegerFilter authorId;
    
    private InstantFilter releaseDate;

    @Override
    public BookCriteria copy() {
        return new BookCriteria(this);
    }
    
    public BookCriteria() {}
    
    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.edition = other.edition == null ? null : other.edition.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.releaseDate = other.releaseDate == null ? null : other.releaseDate.copy();

    }

    public IntegerFilter id() {
        if (id == null) {
            id = new IntegerFilter();
        }
        return id;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public IntegerFilter categoryId() {
        if (categoryId == null) {
            categoryId = new IntegerFilter();
        }
        return categoryId;
    }

    public IntegerFilter authorId() {
        if (authorId == null) {
            authorId = new IntegerFilter();
        }
        return authorId;
    }
    public InstantFilter releaseDate() {
        if (releaseDate == null) {
            releaseDate = new InstantFilter();
        }
        return releaseDate;
    }

}
