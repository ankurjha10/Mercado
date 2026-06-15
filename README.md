<div align="center">

# 🛒 Mercado

### A Production-Ready E-Commerce Backend Built with Spring Boot

<p>
  <strong>Java 21 • Spring Boot 3 • PostgreSQL • Redis • JWT • Docker • AWS EC2 • Nginx</strong>
</p>

<p>
  Mercado is a scalable e-commerce backend application designed with modern backend development practices.
  The project implements secure JWT authentication, role-based authorization, Redis caching,
  PostgreSQL persistence, Docker containerization, and AWS cloud deployment.
</p>

</div>

<hr>

<h2>📖 Overview</h2>

<p>
Mercado is a backend system for an e-commerce platform supporting Customers, Sellers, and Administrators.
The project focuses on building production-oriented backend architecture while following industry-standard
security and deployment practices.
</p>

<p>
The application is fully containerized using Docker and deployed on AWS EC2 behind an Nginx reverse proxy.
Authentication is handled using JWT tokens, while PostgreSQL manages persistent data storage and Redis
provides caching support for improved performance.
</p>

<hr>

<h2>✨ Features</h2>

<h3>🔐 Authentication & Security</h3>

<ul>
<li>User Registration</li>
<li>User Login</li>
<li>JWT Token Authentication</li>
<li>Role-Based Access Control (RBAC)</li>
<li>BCrypt Password Encryption</li>
<li>Stateless Authentication Architecture</li>
<li>Protected API Endpoints</li>
</ul>

<h3>👤 Customer Features</h3>

<ul>
<li>Create Account</li>
<li>Manage Profile</li>
<li>Manage Addresses</li>
<li>Add Products to Cart</li>
<li>Update Cart Items</li>
<li>Place Orders</li>
<li>View Order History</li>
</ul>

<h3>🏪 Seller Features</h3>

<ul>
<li>Create Seller Profile</li>
<li>Add Products</li>
<li>Update Product Information</li>
<li>Delete Products</li>
<li>Manage Inventory</li>
<li>Access Seller-Specific Endpoints</li>
</ul>

<h3>🛠️ Admin Features</h3>

<ul>
<li>Manage Categories</li>
<li>Administrative Access Control</li>
<li>Protected Administrative APIs</li>
</ul>

<h3>⚡ Performance Features</h3>

<ul>
<li>Redis Integration</li>
<li>Database Connection Pooling</li>
<li>Stateless API Design</li>
<li>Dockerized Deployment</li>
<li>Production-Ready Infrastructure</li>
</ul>

<hr>

<h2>🏗️ System Architecture</h2>

<pre>
                 ┌────────────────────┐
                 │      Client        │
                 └─────────┬──────────┘
                           │
                           ▼
                 ┌────────────────────┐
                 │       Nginx        │
                 │ Reverse Proxy      │
                 └─────────┬──────────┘
                           │
                           ▼
                 ┌────────────────────┐
                 │   Spring Boot API  │
                 └───────┬─────┬──────┘
                         │     │
              ┌──────────┘     └──────────┐
              ▼                           ▼
     ┌────────────────┐        ┌────────────────┐
     │ PostgreSQL 17  │        │     Redis      │
     │ Persistent DB  │        │    Caching     │
     └────────────────┘        └────────────────┘
</pre>

<hr>

<h2>🧰 Tech Stack</h2>

<table>
<tr>
<th>Category</th>
<th>Technology</th>
</tr>

<tr>
<td>Language</td>
<td>Java 21</td>
</tr>

<tr>
<td>Framework</td>
<td>Spring Boot 3</td>
</tr>

<tr>
<td>Security</td>
<td>Spring Security + JWT</td>
</tr>

<tr>
<td>ORM</td>
<td>Hibernate / Spring Data JPA</td>
</tr>

<tr>
<td>Database</td>
<td>PostgreSQL 17</td>
</tr>

<tr>
<td>Cache</td>
<td>Redis</td>
</tr>

<tr>
<td>Build Tool</td>
<td>Maven</td>
</tr>

<tr>
<td>Containerization</td>
<td>Docker</td>
</tr>

<tr>
<td>Orchestration</td>
<td>Docker Compose</td>
</tr>

<tr>
<td>Cloud</td>
<td>AWS EC2</td>
</tr>

<tr>
<td>Web Server</td>
<td>Nginx</td>
</tr>

<tr>
<td>API Documentation</td>
<td>Swagger / OpenAPI</td>
</tr>

</table>

<hr>

<h2>🔒 Authorization Model</h2>

<table>
<tr>
<th>Role</th>
<th>Permissions</th>
</tr>

<tr>
<td>CUSTOMER</td>
<td>Cart Management, Orders, Address Management</td>
</tr>

<tr>
<td>SELLER</td>
<td>Product Creation, Product Updates, Inventory Management</td>
</tr>

<tr>
<td>ADMIN</td>
<td>Category Management and Administrative Operations</td>
</tr>

</table>

<hr>

<h2>🚀 Deployment Infrastructure</h2>

<p>
Mercado is deployed on an AWS EC2 Linux instance using Docker Compose.
The deployment stack includes:
</p>

<ul>
<li>Spring Boot Application Container</li>
<li>PostgreSQL Database Container</li>
<li>Redis Cache Container</li>
<li>Nginx Reverse Proxy</li>
<li>AWS EC2 Cloud Infrastructure</li>
</ul>

<h3>Deployment Flow</h3>

<pre>
GitHub
   │
   ▼
AWS EC2
   │
   ▼
Docker Compose
   │
   ├── Spring Boot Container
   ├── PostgreSQL Container
   └── Redis Container
</pre>

<hr>

<h2>📂 Project Structure</h2>

<pre>
src
├── main
│   ├── java
│   │   └── com.shopping.mercado
│   │       ├── controller
│   │       ├── service
│   │       ├── repository
│   │       ├── security
│   │       ├── entity
│   │       ├── dto
│   │       └── config
│   │
│   └── resources
│       ├── application.properties
│       └── application-prod.properties
│
└── test
</pre>

<hr>

<h2>⚙️ Environment Variables</h2>

<pre>
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

JWT_SECRET_KEY=

SPRING_DATA_REDIS_HOST=
SPRING_DATA_REDIS_PORT=

SPRING_CACHE_TYPE=redis
</pre>

<hr>

<h2>📑 API Documentation</h2>

<p>
Swagger UI is integrated into the application for testing and exploring REST APIs.
</p>

<pre>
http://localhost:8090/swagger-ui/index.html
</pre>

<p>
After deployment:
</p>

<pre>
http://YOUR_SERVER_IP/swagger-ui/index.html
</pre>

<hr>

<h2>🎯 Learning Objectives</h2>

<ul>
<li>Designing scalable REST APIs</li>
<li>Implementing JWT Authentication</li>
<li>Applying Role-Based Authorization</li>
<li>Working with Redis Caching</li>
<li>Database Design using PostgreSQL</li>
<li>Containerization using Docker</li>
<li>Cloud Deployment on AWS EC2</li>
<li>Reverse Proxy Configuration using Nginx</li>
<li>Production-Oriented Backend Development</li>
</ul>

<hr>

<h2>📈 Future Enhancements</h2>

<ul>
<li>Payment Gateway Integration</li>
<li>Email Notifications</li>
<li>CI/CD using GitHub Actions</li>
<li>Monitoring & Logging</li>
<li>HTTPS with Custom Domain</li>
<li>Product Search & Filtering</li>
<li>Wishlist Functionality</li>
<li>Order Tracking System</li>
<li>Microservices Migration</li>
</ul>

<hr>

<div align="center">

<h2>👨‍💻 Author</h2>

<b>Ankur</b>

Backend Developer | Java Developer | Cloud Enthusiast

Building scalable backend systems using Java, Spring Boot, PostgreSQL, Redis, Docker and AWS.

</div>
