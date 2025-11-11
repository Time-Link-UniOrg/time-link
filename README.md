# Time Link - Automation System

A Spring Boot application for automation and time management.

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Git** (for cloning the repository)

## Database Setup

### 1. Install PostgreSQL
Download and install PostgreSQL from [postgresql.org](https://www.postgresql.org/download/)

### 2. Create Database
Open pgAdmin or use psql command line:

```sql
CREATE DATABASE your_database_name;
```

Or via command line:
```bash
psql -U postgres -c "CREATE DATABASE your_database_name;"
```

### 3. Create User (if needed)
```sql
CREATE USER your_username WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE your_database_name TO your_username;
```

## Installation

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd time-link
```

### 2. Configure Database Connection
Create or update `src/main/resources/application-dev.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**⚠️ Security Note:** 
- Never commit `application-dev.properties` with real credentials to version control
- Add `application-dev.properties` to `.gitignore`
- Use environment variables for production

### 3. Install Dependencies
```bash
mvn clean install
```

## Running the Application

### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using Java
```bash
mvn clean package
java -jar target/time-link-0.0.1-SNAPSHOT.jar
```

### Option 3: From IDE (IntelliJ/Eclipse)
1. Import the project as a Maven project
2. Find the main class: `TimeLinkApplication.java`
3. Right-click → Run

## Verify Installation

Once the application starts successfully, verify it's working:

### 1. Check Database Connection
Open your browser and visit:
```
http://localhost:8081/api/test/db-connection
```

You should see:
```
✅ Database connected successfully! 
Database: PostgreSQL
URL: jdbc:postgresql://localhost:5432/your_database_name
```

### 2. Access Swagger UI
View API documentation at:
```
http://localhost:8081/swagger-ui.html
```

### 3. Check Application Health
```
http://localhost:8081/actuator/health
```
(if Spring Boot Actuator is added)

## Configuration

### Application Profiles

The application supports multiple profiles:

- **dev** (development) - default profile with debug logging
- **prod** (production) - optimized for production use

To switch profiles, set in `application.properties`:
```properties
spring.profiles.active=dev
```

Or via command line:
```bash
java -jar -Dspring.profiles.active=prod target/time-link-0.0.1-SNAPSHOT.jar
```

### Port Configuration

Default port: **8081**

To change the port, edit `application.properties`:
```properties
server.port=8080
```

### Environment Variables (Recommended)

Instead of hardcoding credentials, use environment variables:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME:timelink}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
```

Then set environment variables on your system.

## Troubleshooting

### Port Already in Use
If port 8081 is already in use:

**Windows:**
```bash
netstat -ano | findstr :8081
taskkill /PID <process-id> /F
```

**Linux/Mac:**
```bash
lsof -i :8081
kill -9 <process-id>
```

Or change the port in `application.properties`

### Database Connection Failed
1. Verify PostgreSQL is running
2. Check database name, username, and password in your configuration
3. Ensure the database exists
4. Verify PostgreSQL is listening on port 5432

### Application Won't Start
1. Check Java version: `java -version` (should be 17+)
2. Clear Maven cache: `mvn clean`
3. Check for port conflicts
4. Review application logs for errors

## Project Structure

```
time-link/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/timelink/
│   │   │       ├── TimeLinkApplication.java
│   │   │       └── controller/
│   │   │           └── TestController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties (create locally, don't commit)
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

## Tech Stack

- **Spring Boot 3.4.10**
- **Java 17**
- **PostgreSQL**
- **Spring Data JPA**
- **Lombok**
- **SpringDoc OpenAPI (Swagger)**
- **Maven**

## Development

### Adding New Dependencies
Edit `pom.xml` and run:
```bash
mvn clean install
```

### Database Schema
The application uses `spring.jpa.hibernate.ddl-auto=update` which automatically creates/updates tables based on your JPA entities.

**Note:** For production, use `validate` or migration tools like Flyway/Liquibase.

### Hot Reload
Spring Boot DevTools is included for automatic restart during development.

## API Documentation

Once running, access interactive API documentation at:
```
http://localhost:8081/swagger-ui.html
```

## Security Best Practices

1. **Never commit sensitive data** (passwords, API keys) to version control
2. Add `application-dev.properties` to `.gitignore`
3. Use environment variables for credentials
4. Use different credentials for dev/test/prod environments
5. For production, consider using Spring Cloud Config or HashiCorp Vault

## .gitignore Recommendations

Add these to your `.gitignore`:
```
application-dev.properties
application-local.properties
*.log
target/
.env
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

[Add your license information here]

## Support

For issues or questions, create an issue in the repository.

---

**Happy Coding! 🚀**
