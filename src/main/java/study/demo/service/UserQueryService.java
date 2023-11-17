package study.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.demo.entity.Member;
import study.demo.entity.Member_;
import study.demo.entity.User;
import study.demo.enums.EGender;
import study.demo.enums.EUserStatus;
import study.demo.repository.MemberRepository;
import study.demo.repository.UserRepository;
import study.demo.service.criteria.UserCriteria;
import study.demo.service.dto.response.AdminUserDto;
import study.demo.utils.ConverterUtil;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.RangeFilter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<Member> {
    
    private final MemberRepository memberRepository;
    
    private final ConverterUtil converterUtil;
    
    public List<AdminUserDto> findBycriteria(UserCriteria criteria, Pageable pabePageable) {
        log.info("find by criteria : {}", criteria);
        Specification<Member> spec = createSpecification(criteria);
        Page<Member> members = memberRepository.findAll(spec, pabePageable);
        return members.stream()
                .map(converterUtil::convertAdminUserDto)
                .toList();
    }
    
   protected Specification<Member> createSpecification(UserCriteria criteria) {
       Specification<Member> specification = Specification.where(null);
       if (criteria != null) {
           
           if (criteria.getUserId() != null) {
               specification = specification.and(buildRangeSpecification(criteria.getUserId(), Member_.userId));
           }
           if (criteria.getEmail() != null) {
               specification = specification.and(buildStringSpecification(criteria.getEmail(), Member_.email));
           }
           if (criteria.phone() != null) {
               specification = specification.and(buildStringSpecification(criteria.getPhone(), Member_.phone));
           }
//           if (criteria.userStatus() != null) {
//               RangeFilter<EUserStatus> globalStringFilter = new RangeFilter<>();
//               List<EUserStatus> userStatuses = new ArrayList<>();
//               userStatuses.add(EUserStatus.ACTIVATED);
//               userStatuses.add(EUserStatus.BLOCKED);
//               userStatuses.add(EUserStatus.UNVERIFY);
//               userStatuses.add(EUserStatus.VULNERABLE);
//               globalStringFilter.setIn(userStatuses);
//               specification = specification.and(buildRangeSpecification(criteria.getUserStatus()), Member_.userStatus);
//           }
           if (criteria.firstName() != null) {
               specification = specification.and(buildStringSpecification(criteria.getFirstName(), Member_.firstName));
           }
           if (criteria.lastName() != null) {
               specification = specification.and(buildStringSpecification(criteria.getLastName(), Member_.lastName));
           }
           if (criteria.birthday() != null) {
               specification = specification.and(buildRangeSpecification(criteria.getBirthday(), Member_.birthday));
           }
           
//           if (criteria.gender() != null) {
//               specification = specification.and(buildStringSpecification(criteria.getGender(), Member_.gender));
//           }
//           if (criteria.gender() != null) {
//               EGender gender =null;
//               for(EGender e: EGender.values() ) {
//                   criteria.gender().equals(e);
//                   gender=e;
//                   break ;
//               }
//               specification = specification.and(buildSpecification(gender, ,root -> root.get("gender"));
//           }
//           if (criteria.borrowerRecord() != null) {
//               specification = specification.and(buildSpecification(criteria.getBorrowerRecord(),
//                       root -> root.join(Member_.borrowerRecord, JoinType.LEFT).get(BorrowerRecord_.user)));
//           }

       }
       return specification;
   }

}
