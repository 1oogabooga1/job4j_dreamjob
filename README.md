# Job4j Dream Job

## Overview
In this project I realized a system for searching job and workers. I made possible creation vacancies, candidates and an autherization system. 

---

## Technologies stack
- **Java 17, Spring Boot**
- **Thymeleaf, Bootstrap**
- **PostgreSQL, SQl2o, H2, Liquibase**
- **Maven, GitHub Actions**
- **JUnit 5, Mock testing**

---

## Extended Description
- Main - General information about the project
- Vacancies - list of vacancies. Click on the name redirects the user to the edit page, where it is possible to:
  - Change/upload a photo
  - Edit the title
  - Edit the description
  - Make the vacancy public/private
  - Choose another city from the list
  - Delete the vacancy
  - Confirm changes
  - Cancel the changes
- Candiates - list of candidates. Click on the name redirects the user to the edit page, where it is possible to:
  - Change/upload a photo
  - Edit the name
  - Edit the description
  - Make the candidature public/private
  - Choose another city from the list
  - Delete the candidature
  - Confirm changes
  - Cancel the changes
- Create a vacancy - creation page for a vacancy where user can:
  - Enter a title of the vacancy
  - Upload a photo
  - Enter the description
  - Make the vacancy public/private
  - Choose the city out the list
  - Save the vacancy
  - Cancel
- Create a resume - creation page for a vacancy where user can:
  - Enter user's name
  - Upload a photo
  - Enter the description
  - Choose the city out the list
  - Save the resume
  - Cancel
- Register/user's name -  if user is already logged in, then his name appears if not then button Register:
  - First field - user's name
  - Second field - user's e-mail
  - Third field - user's password
  - Button register
  - Button cancel 
- Login/Logout - if user is already logged in, then the button logout appears if not then button Login:
  - First field - user's e-mail
  - Second field - user's password
  - Linked text, that suggest user to register if he is still not.
  - Button Login
  - Button Cancel
    
---

## How to run 
#### 1. Clone repository:
```bash
git clone https://github.com/1oogabooga1/job4j_dreamjob.git
cd job4j_dreamjob
```
#### 2. Configure DB (PostgreSQL). Example env variables (or use application-local.yml):
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/job4j_dreamjob
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.show-sql=false
```
#### 3.Build and run:
```bash
mvn clean package
java -jar target/job4j_cinema.jar
```
#### 4. Open in browser:
```bash
http://localhost:8080/
```

---

## Links
| Page   | Link   |
|--------|--------|
| Main | http://localhost:8080 |
| Vacancies | http://localhost:8080/vacancies |
| Candidates | http://localhost:8080/candidates |
| Vacancy creation | http://localhost:8080/vacancies/create |
| Resume creation | http://localhost:8080/candidates/create |
| Register | http://localhost:8080/users/register |
| Login | http://localhost:8080/users/login |

---

## Screenshots
<img width="1440" height="865" alt="image" src="https://github.com/user-attachments/assets/855dca0f-2c98-4f7a-9706-fe0af3ab2831" />
<img width="1433" height="863" alt="image" src="https://github.com/user-attachments/assets/319e87e3-4dc1-43a6-954a-14a0881c97e1" />
<img width="1439" height="342" alt="image" src="https://github.com/user-attachments/assets/4e2406a0-5e88-4aa8-a98a-508f9c85ff9e" />
<img width="1437" height="713" alt="image" src="https://github.com/user-attachments/assets/77696eec-f566-4612-8dac-dc08235285e8" />
<img width="1435" height="395" alt="image" src="https://github.com/user-attachments/assets/c9891b2b-16bf-4ab2-9df1-c1b4b8bbe0f0" />
<img width="1435" height="815" alt="image" src="https://github.com/user-attachments/assets/f1a7b848-db14-416e-b181-47a1595ead4d" />
<img width="1439" height="860" alt="image" src="https://github.com/user-attachments/assets/d3a8545d-2806-4f13-8881-582f9a51094c" />
<img width="1438" height="513" alt="image" src="https://github.com/user-attachments/assets/ddd58dc2-0441-4e7c-8a1e-051df830a495" />
