package study.demo.service.criteria;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import study.demo.entity.Member;
import study.demo.entity.User;

public class UsersSpecification {

	public static Specification<User> nameLike(String userName) {
		return (root, query, criteriaBuilder) -> userName == null ? null
				: criteriaBuilder.like(root.get("email"), "%" + userName + "%");
	}

	public static Specification<User> roleLike(String roleName) {
		return (root, query, criteriaBuilder) -> roleName == null ? null
				: criteriaBuilder.like(root.get("role"), "%" + roleName + "%");
	}

	public static Specification<User> hasFirstNameLike(String firstName) {
		return (root, query, criteriaBuilder) -> {
			Join<User, Member> userMember = root.join("user");
			return criteriaBuilder.like(userMember.get("firstName"), firstName);
		};

	}
}
