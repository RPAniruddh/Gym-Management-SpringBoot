package com.gym.management.membership;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gym.management.membership.exception.ResourceNotFoundException;
import com.gym.management.membership.model.Member;
import com.gym.management.membership.repository.MemberRepository;
import com.gym.management.membership.service.MemberServiceImpl;

@ExtendWith(MockitoExtension.class)
class MemberServiceTests {

	@InjectMocks
	private MemberServiceImpl memberService;

	@Mock
	private MemberRepository memberRepository;

	private Member member;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		member = new Member();
		member.setId(1);
		member.setFirstName("John");
		member.setLastName("Doe");
		member.setEmail("john.doe@example.com");
		member.setPhoneNumber("1234567890");
		member.setDateOfBirth(new Date());
	}

	@Test
	public void testCreateMember() {
		when(memberRepository.save(any(Member.class))).thenReturn(member);
		Member createdMember = memberService.createMember(member);
		assertNotNull(createdMember);
		assertEquals(member.getId(), createdMember.getId());
	}

	@Test
	public void testGetMember() {
		when(memberRepository.findById(1)).thenReturn(Optional.of(member));
		Member foundMember = memberService.getMember(1);
		assertNotNull(foundMember);
		assertEquals(member.getId(), foundMember.getId());
	}

	@Test
	public void testGetMemberNotFound() {
		when(memberRepository.findById(1)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			memberService.getMember(1);
		});
	}

	@Test
	public void testGetAllMembers() {
		List<Member> members = Arrays.asList(member);
		when(memberRepository.findAll()).thenReturn(members);
		List<Member> foundMembers = memberService.getAllMembers();
		assertNotNull(foundMembers);
		assertEquals(1, foundMembers.size());
	}

	@Test
	public void testUpdateMember() {
		Member updatedDetails = new Member();
		updatedDetails.setFirstName("Jane");
		updatedDetails.setLastName("Doe");
		updatedDetails.setEmail("jane.doe@example.com");
		updatedDetails.setPhoneNumber("0987654321");
		updatedDetails.setDateOfBirth(new Date());

		when(memberRepository.findById(1)).thenReturn(Optional.of(member));
		when(memberRepository.save(any(Member.class))).thenReturn(updatedDetails);

		Member updatedMember = memberService.updateMember(1, updatedDetails);
		assertNotNull(updatedMember);
		assertEquals(updatedDetails.getFirstName(), updatedMember.getFirstName());
	}

	@Test
	public void testDeleteMember() {
		when(memberRepository.existsById(1)).thenReturn(true);
		doNothing().when(memberRepository).deleteById(1);
		memberService.deleteMember(1);
		verify(memberRepository, times(1)).deleteById(1);
	}

	@Test
	public void testDeleteMemberNotFound() {
		when(memberRepository.existsById(1)).thenReturn(false);
		assertThrows(ResourceNotFoundException.class, () -> {
			memberService.deleteMember(1);
		});
	}

}
