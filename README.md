# Gym Management Microservices Project

## Project Status
- This project is ongoing and under active development.

## Microservices Architecture
#### 1. Member Management Service
- Manages member-related operations
- Handles member profiles, subscriptions, and membership details

#### 2. Fitness Tracking Service
- Manages member fitness progress, workout logs, and performance tracking
## Use Cases in Detail

#### Member Management Service Use Cases
1. Member Registration
   - Create new member profiles
@@ -51,61 +19,3 @@
   - Track membership status (active, expired, pending)
   - Handle membership renewals and upgrades

3. Member Progress Tracking
   - Store and retrieve member fitness goals
   - Track body measurements
   - Record fitness assessments

#### Fitness Tracking Service Use Cases
1. Workout Logging
   - Record exercise routines
   - Track sets, reps, weights
   - Store progress over time
   - 
## Technical Considerations
- Use Spring Boot for microservices
- Implement service discovery with Eureka
- Use API Gateway for routing
- Use MySQL for primary database
