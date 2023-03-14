package hansung.cse.withSpace.domain.space;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import hansung.cse.withSpace.domain.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberSpace extends Space {


    //@Id @GeneratedValue
    //@Column(name = "space_id")
    //private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }


    public MemberSpace(Member member) {
        this.member = member;
    }

}


