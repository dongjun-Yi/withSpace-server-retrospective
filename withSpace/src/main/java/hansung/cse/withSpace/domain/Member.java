package hansung.cse.withSpace.domain;


import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.space.MemberSpace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Member {   //회원

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private MemberSpace memberSpace;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<FriendShip> friendRequester = new ArrayList<>(); //친구 신청한 사람

    @OneToMany(mappedBy = "friend", cascade = CascadeType.REMOVE)
    private List<FriendShip> friendReceiver = new ArrayList<>();//친구 신청 받은 사람


    private String email;
    private String password;
    private String memberName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;

    //연관관계 편의 메소드//
    public void setMemberSpace(MemberSpace memberSpace) {
        this.memberSpace = memberSpace;
        memberSpace.setMember(this);
    }

    public Member( String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
    }


}