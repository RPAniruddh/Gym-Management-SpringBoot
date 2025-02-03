package com.gym.management.fitness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.gym.management.fitness.dto.MemberDTO;
import com.gym.management.fitness.models.Exercise;
import com.gym.management.fitness.models.Workout;
import com.gym.management.fitness.models.WorkoutExercise;
import com.gym.management.fitness.repository.ExerciseRepository;
import com.gym.management.fitness.repository.WorkoutRepository;
import com.gym.management.fitness.service.FitnessServiceImpl;

@ExtendWith(MockitoExtension.class)
class FitnessServiceApplicationTests {
	@Mock
	private WorkoutRepository workoutRepository;

	@Mock
	private ExerciseRepository exerciseRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private FitnessServiceImpl fitnessService;

	private MemberDTO memberDTO;
	private Workout workout;
	private Exercise exercise;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		memberDTO = new MemberDTO();
		memberDTO.setId(1);
		memberDTO.setFirstName("John");
		memberDTO.setLastName("Doe");

		workout = new Workout();
		workout.setId(1);
		workout.setMemberId(1);
		workout.setMemberFirstName("John");
		workout.setMemberLastName("Doe");
		workout.setWorkoutName("Morning Workout");
		workout.setWorkoutDate(LocalDateTime.now());

		exercise = new Exercise();
		exercise.setId(1);
		exercise.setName("Push Up");

		String memberServiceUrl = "http://localhost:8082/api/members/get/1";
		when(restTemplate.getForObject(memberServiceUrl, MemberDTO.class)).thenReturn(memberDTO);
		when(workoutRepository.findById(anyInt())).thenReturn(Optional.of(workout));
		when(exerciseRepository.findById(anyInt())).thenReturn(Optional.of(exercise));
		when(workoutRepository.save(any(Workout.class))).thenReturn(workout);
		when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);
	}

	@Test
	void testCreateWorkout() {
		Workout createdWorkout = fitnessService.createWorkout(1, "Morning Workout");

		assertEquals("John", createdWorkout.getMemberFirstName());
		assertEquals("Doe", createdWorkout.getMemberLastName());
		assertEquals("Morning Workout", createdWorkout.getWorkoutName());
	}

	@Test
	void testAddExerciseToWorkout() {
		Workout updatedWorkout = fitnessService.addExerciseToWorkout(1, 1, 3, 10, 50.0);

		assertEquals(1, updatedWorkout.getExercises().size());
	}

	@Test
	void testGetMemberWorkouts() {
		when(workoutRepository.findByMemberId(anyInt())).thenReturn(Arrays.asList(workout));

		List<Workout> workouts = fitnessService.getMemberWorkouts(1);

		assertEquals(1, workouts.size());
		assertEquals("John", workouts.get(0).getMemberFirstName());
	}

	@Test
	void testDeleteMemberWorkouts() {
		when(workoutRepository.findByMemberId(anyInt())).thenReturn(Arrays.asList(workout));

		fitnessService.deleteMemberWorkouts(1);

		verify(workoutRepository).deleteAll(any(List.class));
	}

	@Test
	void testCreateExercise() {
		Exercise createdExercise = fitnessService.createExercise(exercise);

		assertEquals("Push Up", createdExercise.getName());
	}

	@Test
	void testGetAllExercises() {
		when(exerciseRepository.findAll()).thenReturn(Arrays.asList(exercise));

		List<Exercise> exercises = fitnessService.getAllExercises();

		assertEquals(1, exercises.size());
		assertEquals("Push Up", exercises.get(0).getName());
	}

	@Test
	void testRemoveExerciseFromWorkout() {
		WorkoutExercise workoutExercise = new WorkoutExercise();
		workoutExercise.setExercise(exercise);
		workout.getExercises().add(workoutExercise);

		Workout updatedWorkout = fitnessService.removeExerciseFromWorkout(1, 1);

		assertEquals(0, updatedWorkout.getExercises().size());
	}
}
