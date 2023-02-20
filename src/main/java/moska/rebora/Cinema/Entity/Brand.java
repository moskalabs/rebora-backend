package moska.rebora.Cinema.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Theater.Entity.Theater;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id", nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String brandName;

    @OneToMany(mappedBy = "brand")
    List<Theater> theaterList = new ArrayList<>();

    public void changeBrandName(String brandName){
        this.brandName = brandName;
    }

    @Builder
    public Brand(String brandName) {
        this.brandName = brandName;
    }
}
