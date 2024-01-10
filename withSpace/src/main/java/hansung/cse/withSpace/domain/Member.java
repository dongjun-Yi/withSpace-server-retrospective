package hansung.cse.withSpace.domain;


import hansung.cse.withSpace.domain.friend.FriendShip;
import hansung.cse.withSpace.domain.space.MemberSpace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "UUID")
    private UUID uuid;

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

    public Member(UUID uuid, String memberName, String email, String password) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.uuid = uuid;

        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        this.status = false;

        //스페이스 생성
        MemberSpace memberSpace = new MemberSpace(this);
        this.memberSpace = memberSpace;
    }

    public void update(String email, String password, String memberName) {
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (memberName != null) {
            this.memberName = memberName;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}