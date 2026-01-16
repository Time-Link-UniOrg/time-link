# TimeLink - School Management System

A Spring Boot application for managing school operations, including students, teachers, courses, groups, and lesson scheduling.

## 🎯 Project Overview

TimeLink is a comprehensive school management system designed to automate and streamline educational administration. This initial version focuses on core functionality with plans for significant expansion in the future.

**Current Features:**
- Student and Parent Management (Many-to-Many relationship)
- Teacher and Course Management with Qualifications (Many-to-Many relationship)
- Group and Lesson Scheduling
- Attendance Tracking
- Time Conflict Detection for lesson scheduling
- RESTful API with Swagger documentation

**Note:** This is a **Phase 1 implementation** of a larger system. Future phases will include substitute teacher workflows, payment processing, reporting, and advanced analytics.

## 🧠 Key Business Logic

**Time Conflict Detection & Prevention:**
The system implements sophisticated concurrency control to prevent scheduling conflicts. When two requests attempt to schedule a teacher at the same time, only the first succeeds - similar to inventory management in e-commerce where two customers try to purchase the last item.

**Implementation:**
- Database-level validation with custom `@Query` annotations
- Transaction isolation to prevent race conditions
- Real-time availability checking before lesson creation
- Automatic conflict resolution with `TimeConflictException`

**Substitute Teacher Matching Algorithm:**
Complex multi-criteria matching system that finds available substitute teachers by:
1. Checking teacher qualifications (Many-to-Many relationship with courses)
2. Verifying time availability (no overlapping lessons)
3. Excluding teachers with scheduling conflicts
4. Providing ranked list of suitable candidates

This business logic significantly increases project complexity beyond standard CRUD operations.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.4.10**
- **PostgreSQL 18**
- **Spring Data JPA** with Hibernate
- **MapStruct** for DTO mapping
- **Lombok** for boilerplate reduction
- **SpringDoc OpenAPI** (Swagger UI)
- **Maven** for dependency management

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd time-link
```

### 2. Setup PostgreSQL Database
```sql
CREATE DATABASE timelink_db;
```

### 3. Configure Database Connection
Create `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/timelink_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

**Security :** Never commit credentials to version control. Add `application-dev.properties` to `.gitignore`.

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on **http://localhost:8081**

## 📚 API Documentation

Access Swagger UI at:
```
http://localhost:8081/swagger-ui/index.html
```

### Main Endpoints

**Groups:**
- `GET /api/groups` - List all groups
- `POST /api/groups` - Create new group
- `GET /api/groups/{id}` - Get group by ID
- `PUT /api/groups/{id}` - Update group
- `DELETE /api/groups/{id}` - Delete group

**Students:**
- `GET /api/students` - List all students
- `POST /api/students` - Create new student
- `GET /api/students/{id}` - Get student by ID

**Lessons:**
- `POST /api/lessons` - Create lesson with time conflict detection
- `GET /api/lessons/teacher/{teacherId}` - Get teacher's lessons
- `GET /api/lessons/group/{groupId}` - Get group's lessons

**Teachers:**
- `GET /api/teachers` - List all teachers
- `POST /api/teachers/qualifications` - Add teacher qualification

## 🗄️ Database Schema

**Main Tables:**
- `teacher` - Teacher information and credentials
- `student_table` - Student information
- `parent` - Parent/guardian information
- `group_table` - Class groups
- `course` - Available courses
- `lesson` - Scheduled lessons
- `attendance` - Attendance records

**Relationship Tables:**
- `teacher_course_qualification` - Teacher qualifications (Many-to-Many)
- `student_parent` - Student-parent relationships (Many-to-Many)

**Relationships:**
- Student → Group (Many-to-One)
- Lesson → Teacher, Group, Course (Many-to-One)
- Teacher ↔ Course (Many-to-Many via qualifications)
- Student ↔ Parent (Many-to-Many)

## 🧪 Testing

Run all tests:
```bash
mvn test
```

**Test Coverage:**
- Unit Tests: LessonService (15 tests)
- Integration Tests: Substitute flow (3 tests)
- Time conflict detection
- CRUD operations
- Relationship management

## 📁 Project Structure

```
time-link/
├── src/main/java/com/timelink/time_link/
│   ├── controller/     # REST controllers
│   ├── service/        # Business logic
│   ├── repository/     # Data access layer
│   ├── model/          # JPA entities
│   ├── dto/            # Data Transfer Objects
│   ├── mapper/         # MapStruct mappers
│   ├── exception/      # Custom exceptions
│   └── startup/        # Database seeders
├── src/test/
│   ├── service/        # Unit tests
│   └── integration/    # Integration tests
└── pom.xml
```

## 🔮 Future Development

This is **Phase 1** of a comprehensive school management platform. Planned features include:

**Phase 2 - Substitute Teacher System:**
- Automated substitute teacher requests
- Real-time availability matching
- Notification system
- Substitute teacher acceptance workflow

**Phase 3 - Financial Management:**
- Payment tracking and processing
- Billing automation
- Financial reporting

**Phase 4 - Advanced Features:**
- Parent portal with real-time updates
- Teacher dashboard
- Advanced analytics and reporting
- Mobile application
- Multi-language support


## 👥 Authors

Developed by students at Technical University as part of Java Application Development course.

---

**Status:** ✅ Phase 1 Complete | 🚧 Phase 2 In Planning

**Last Updated:** January 2026
