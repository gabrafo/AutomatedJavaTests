## AutomatedJavaTests

This repository contains notes and exercises based on the course "Testes automatizados na prática com Spring Boot" available on Udemy. The project focuses on automated testing using a StarWars planets API.

### Overview

This project serves as a practical learning resource for automated testing techniques applied within a Spring Boot environment. The main objective is to explore different testing methodologies and tools, specifically in the context of interacting with a StarWars API that provides information about planets.

### Getting Started

To get started with this project, follow these steps:

1. **Clone the Repository:**
```
git clone https://github.com/gabrafo/AutomatedJavaTests.git
cd AutomatedJavaTests
```

2. **Set Up Environment:**
- Ensure you have Java Development Kit (JDK) installed.
- Set up your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse).

3. **Configure Dependencies:**
- Review the `pom.xml` file for required dependencies.
- Use Maven or Gradle for dependency management and building the project.

### Endpoints
1. **GET:**
   - Retrieves a planet by ID:
       ```
       GET /planets/1
       ```
       
   - Retrieves a planet by name:
       ```
       GET /planets/tatooine
       ```

   - Retrieves all planets using (or not) field filters:
       ```
       GET /planets?climate=arid&terrain=desert
       ```

2. **POST:**
   - Creates a planet:
       ```
       POST /planets
       ```

   -> Request body:
   ```json
    {
      "name": "<string>",
      "climate": "<string>"
      "terrain": "<string>"
    }
   ```

3. **DELETE:**
   - Removes a planet by ID:
       ```
       DELETE /planets/1
       ```

### Course Content

The course "Testes automatizados na prática com Spring Boot" provides a structured approach to understanding and implementing automated tests. Key topics covered include:

- Introduction to automated testing concepts.
- Writing unit tests and integration tests.
- Writing parameterized tests.
- Testing Spring Boot applications.
- Mocking dependencies and using test doubles.
- Checking code coverage with JaCoCo.
- Generating mutated tests with Pitest.
- Using Testcontainers for an isolated test enviroment.

### Contributing

Contributions to enhance this repository are welcome. If you find any issues or improvements, please feel free to open an issue or create a pull request. Your feedback and contributions will help improve this resource for others learning about automated testing with Spring Boot.

### Resources

- Course: ["Testes automatizados na prática com Spring Boot"](https://www.udemy.com/course/testes-automatizados-na-pratica-com-spring-boot/)

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Acknowledgments

Special thanks to the instructor Giuliana Bezerra for her guidance and expertise in automated testing.
