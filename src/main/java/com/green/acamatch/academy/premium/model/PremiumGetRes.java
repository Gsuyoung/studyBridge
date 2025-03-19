package com.green.acamatch.academy.premium.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PremiumGetRes {
    private Long acaId;
    private String acaName;
    private String acaPic;
    private LocalDate startDate;
    private LocalDate endDate;
    private int preCheck;
    private LocalDateTime createdAt;
    private Long countPremium;

    // **생성자 추가 (JPA가 new 키워드로 객체를 생성할 때 필요함)**
    public PremiumGetRes(Long acaId, String acaName, String acaPic, LocalDate startDate, LocalDate endDate,
                               int preCheck, LocalDateTime createdAt, Long countPremium) {
        this.acaId = acaId;
        this.acaName = acaName;
        this.acaPic = acaPic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.preCheck = preCheck;
        this.createdAt = createdAt;
        this.countPremium = countPremium;
    }

    // 기본 생성자 추가 (JPA가 리플렉션을 사용할 때 필요함)
    public PremiumGetRes() {}
}
