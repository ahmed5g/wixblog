# Blog Post Web App (Spring Boot + Angular)

A **full-stack Blog Post Web Application** inspired by Medium, built with **Spring Boot (backend)** and **Angular (frontend)**.  
Supports **role-based access** (Admin / Authenticated User / Guest) and features **user authentication, post management, category management, and admin control**.

---

## Table of Contents
- [Features](#features)
- [Project Structure](#project-structure)
- [Workflow](#workflow)
- [Installation](#installation)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Usage](#usage)
- [Tech Stack](#tech-stack)
- [License](#license)

---

## Features

### User Authentication & Authorization
- JWT-based login and registration
- Role-based access:
  - **Guest**: view posts, register/login
  - **Authenticated User**: create, edit, like, comment posts
  - **Admin**: manage users, posts, and categories

### Post Management
- Create, edit, delete posts
- Like and comment functionality
- Share posts externally
- Pagination, search, and category-based filtering

### Category Management
- Parent-child category structure
- Admin CRUD operations for categories
- Browse posts by categories and subcategories

### Admin Management
- View, block, or delete users
- Manage all posts and feature posts on homepage
- Manage categories via admin dashboard

### UI & Navigation
- Responsive and role-adaptive navbar
- Home page displays featured and latest posts

### Technical Features
- RESTful API with Spring Boot
- Secure endpoints using JWT
- Angular frontend with services, route guards, and state management
- Database: Users, Roles, Posts, Categories, Comments, Likes
- Proper relationships:
  - User–Post (1:M)
  - Post–Comment (1:M)
  - Post–Category (M:1)
  - Post–Like (M:M)
  - Category self-reference (parent-child)

---



## Workflow

- **Feature Branching:** Create short-lived feature branches from `backend` or `frontend`.
- Merge completed features into their respective project branch.
- Merge `backend` and `frontend` branches into `main` when features are stable.
- Keeps `main` production-ready and stable.

---

## Installation

### Backend (Spring Boot)
1. Navigate to the backend folder:
   ```bash
   cd backend
   cd backend
Install dependencies (Maven):

   cd backend
Install dependencies (Maven):

mvn clean install
Run the backend server:

mvn spring-boot:run
Backend runs on: http://localhost:8080

Frontend (Angular)
Navigate to the frontend folder:


cd frontend
Install dependencies:

npm install
Run the frontend server:


ng serve
Frontend runs on: http://localhost:4200

Make sure backend is running before using the frontend.
