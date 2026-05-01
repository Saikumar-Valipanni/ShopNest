# 🛍️ ShopNest — Full Stack E-Commerce Web Application


> A production-ready, full-stack e-commerce platform built with Java and Spring Boot featuring user authentication, product catalog, shopping cart, checkout, and a complete admin panel.

 
📁 **GitHub:** [github.com/yourusername/shopnest](https://github.com/Saikumar-Valipanni/ShopNest))


## 📖 Overview

ShopNest is a complete e-commerce web application built from scratch using Java and Spring Boot. It allows users to register, browse products across multiple categories, manage a shopping cart with live price updates, and complete a full checkout flow. Admins can manage the entire product catalog through a dedicated admin panel.

The project follows a clean layered architecture — Controller → Service (Interface + Implementation) → Repository → Database — with Spring Security handling authentication and authorization throughout.

---

## ✨ Features

### 👤 User Features
- Register and login with email and BCrypt-encrypted password
- Browse homepage with category cards and featured products
- Filter products by category — Electronics, Fashion, Home & Kitchen, Books, Beauty
- Search products by keyword across all categories
- Add products to cart — quantity merges automatically if product already exists
- View cart with per-item subtotals and dynamic total price
- Update quantity and remove individual items from cart
- Three-step checkout — shipping details → payment selection → order confirmed
- Payment options — Credit/Debit Card, UPI, Cash on Delivery
- Order confirmation page with order ID, items, and total amount
- Stock updates automatically after every successful order
- Out of stock products show Sold Out and cannot be added to cart

### 🛠️ Admin Features
- Protected admin panel at `/admin` — accessible only to ADMIN role
- Dashboard with total categories and products count
- Add, edit, and delete categories with image URL and live preview
- Add, edit, and delete products with category, price, stock, and description
- Clean sidebar navigation with data tables

### 🔒 Security Features
- Spring Security 6 with session-based authentication
- BCrypt password hashing — passwords never stored as plain text
- Role-based access control — USER and ADMIN roles
- CSRF protection on all forms via Thymeleaf `th:action`
- Route protection — cart and checkout require login, admin requires ADMIN role
- Custom 404, 403, and 500 error pages via `@ControllerAdvice`

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core programming language |
| Spring Boot | 3.x | Application framework |
| Spring Security | 6.x | Authentication and authorization |
| Spring Data JPA | 3.x | Database abstraction layer |
| Hibernate | 6.x | ORM — maps entities to tables |
| Lombok | Latest | Eliminates boilerplate code |
| Maven | 3.9.x | Dependency management and build |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| Thymeleaf | 3.x | Server-side HTML templating |
| Bootstrap | 5.3 | Responsive UI components |
| Bootstrap Icons | 1.11 | Icon library |
| JavaScript | ES6 | Payment animation, image preview |
| Google Fonts | — | Playfair Display + DM Sans |

### Database
| Technology | Version | Purpose |
|---|---|---|
| MySQL | 8.0 | Relational database |

---

## 🗄️ Database Design

ShopNest uses 7 tables with proper foreign key relationships:

```
users ──────────────────── cart ──────── cart_items ──── product ──── category
  │                          │                               │
  └── orders ─── order_items ┘                               │
                                                             └── (FK to category)
```

### Table Overview

| Table | Description |
|---|---|
| `users` | Registered users with role — USER or ADMIN |
| `category` | Product categories with image URLs |
| `product` | Products linked to categories with stock tracking |
| `cart` | One cart per user — OneToOne with users |
| `cart_items` | Products in cart with quantity — ManyToOne to cart and product |
| `orders` | Confirmed orders with shipping details |
| `order_items` | Snapshot of products at time of purchase |

### Entity Relationships

```
User (1) ──── (1) Cart (1) ──── (N) CartItem (N) ──── (1) Product (N) ──── (1) Category
User (1) ──── (N) Order (1) ──── (N) OrderItem (N) ──── (1) Product
```

---

## 📁 Project Structure

```
shopnest/
├── src/
│   ├── main/
│   │   ├── java/com/codegnan/shopnest/
│   │   │   ├── Config/
│   │   │   │   ├── AppConfig.java              # PasswordEncoder bean
│   │   │   │   └── SecurityConfig.java         # Spring Security configuration
│   │   │   ├── Controller/
│   │   │   │   ├── AdminController.java         # Admin panel routes
│   │   │   │   ├── AuthController.java          # Register and login
│   │   │   │   ├── CartController.java          # Cart operations
│   │   │   │   ├── CategoryController.java      # Category page
│   │   │   │   ├── CheckoutController.java      # Checkout flow
│   │   │   │   └── IndexController.java         # Homepage and search
│   │   │   ├── entity/
│   │   │   │   ├── Cart.java
│   │   │   │   ├── CartItem.java
│   │   │   │   ├── Category.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Role.java
│   │   │   │   └── User.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java  # Custom error handling
│   │   │   ├── Repository/
│   │   │   │   ├── CartItemRepository.java
│   │   │   │   ├── CartRepository.java
│   │   │   │   ├── CategoryRepository.java
│   │   │   │   ├── OrderItemRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   └── Service/
│   │   │       ├── CartItemService.java          # Interface
│   │   │       ├── CartService.java              # Interface
│   │   │       ├── CategoryService.java          # Interface
│   │   │       ├── OrderService.java             # Interface
│   │   │       ├── ProductService.java           # Interface
│   │   │       ├── UserService.java              # Interface
│   │   │       └── Impl/
│   │   │           ├── CartItemServiceImpl.java
│   │   │           ├── CartServiceImpl.java
│   │   │           ├── CategoryServiceImpl.java
│   │   │           ├── CustomUserDetailsService.java
│   │   │           ├── OrderServiceImpl.java
│   │   │           ├── ProductServiceImpl.java
│   │   │           └── UserServiceImpl.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── admin/
│   │       │   │   ├── dashboard.html
│   │       │   │   ├── categories.html
│   │       │   │   ├── category-form.html
│   │       │   │   ├── products.html
│   │       │   │   └── product-form.html
│   │       │   ├── fragments/
│   │       │   │   └── navbar.html
│   │       │   ├── cart.html
│   │       │   ├── category.html
│   │       │   ├── checkout.html
│   │       │   ├── error.html
│   │       │   ├── index.html
│   │       │   ├── login.html
│   │       │   ├── order-success.html
│   │       │   ├── payment.html
│   │       │   ├── register.html
│   │       │   └── search.html
│   │       └── static/
│   │           └── css/
│   │               ├── shopnest.css
│   │               └── admin.css
│   └── test/
├── pom.xml
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Make sure you have these installed:

- Java 17 or higher — [Download](https://www.oracle.com/java/technologies/downloads/)
- Maven 3.9.x or use the included Maven wrapper
- MySQL 8.0 — [Download](https://dev.mysql.com/downloads/)
- IntelliJ IDEA or Eclipse (recommended)
- Git

### Clone the repository

```bash
git clone https://github.com/yourusername/shopnest.git
cd shopnest
```

---

## ⚙️ Configuration

### Step 1 — Create the database

Open MySQL Workbench or terminal and run:

```sql
CREATE DATABASE shopnest_db;
```

### Step 2 — Configure application.properties

Open `src/main/resources/application.properties` and update:

```properties
spring.application.name=ShopNest

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/shopnest_db
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Thymeleaf
spring.thymeleaf.cache=false

# Error handling
spring.mvc.throw-exception-if-no-handler-found=true
server.error.whitelabel.enabled=false
```

### Step 3 — Run the database setup script

Run the complete SQL script to create all tables and insert sample data:

```sql
-- Creates 7 tables with relationships
-- Inserts 5 categories
-- Inserts 50 products (10 per category)
-- Creates admin user
```

The full SQL script is available in `/database/shopnest_setup.sql`

---

## ▶️ Running the Application

### Using Maven Wrapper (recommended)

**Windows:**
```bash
.\mvnw spring-boot:run
```

**Mac or Linux:**
```bash
./mvnw spring-boot:run
```

### Using Maven directly

```bash
mvn spring-boot:run
```

### Building and running the JAR

```bash
mvn clean package -DskipTests
java -jar target/shopnest-0.0.1-SNAPSHOT.jar
```

### Access the application

Open your browser and visit:
```
http://localhost:8080
```

You will be redirected to the login page. Register a new account or use the default credentials below.

---

## 🔑 Default Credentials

### Admin Account
```
Email:    admin@shopnest.com
Password: admin123
URL:      http://localhost:8080/admin
```

### Test User
Register a new account at `http://localhost:8080/register`

---

## 📸 Screenshots

### Homepage
![Homepage](screenshots/homepage.png)

### Category Page
![Category](screenshots/category.png)

### Cart Page
![Cart](screenshots/cart.png)

### Checkout Flow
![Checkout](screenshots/checkout.png)

### Admin Dashboard
![Admin](screenshots/admin.png)

> Add your own screenshots in a `/screenshots` folder in the repository root

---

## 🗺️ URL Routes

### Public Routes
| URL | Description |
|---|---|
| `GET /` | Homepage with categories and featured products |
| `GET /login` | Login page |
| `GET /register` | Register page |
| `GET /category/{id}` | Products filtered by category |
| `GET /search?keyword=` | Search results |

### Authenticated Routes (login required)
| URL | Description |
|---|---|
| `GET /cart` | View cart |
| `POST /cart/add` | Add product to cart |
| `POST /cart/update` | Update item quantity |
| `POST /cart/remove` | Remove item from cart |
| `POST /cart/clear` | Clear entire cart |
| `GET /checkout` | Checkout page |
| `POST /checkout/payment` | Payment page |
| `POST /checkout/place-order` | Place order |
| `GET /checkout/success` | Order confirmation |

### Admin Routes (ADMIN role required)
| URL | Description |
|---|---|
| `GET /admin` | Admin dashboard |
| `GET /admin/categories` | List all categories |
| `GET /admin/categories/add` | Add category form |
| `POST /admin/categories/save` | Save category |
| `GET /admin/categories/edit/{id}` | Edit category form |
| `POST /admin/categories/delete/{id}` | Delete category |
| `GET /admin/products` | List all products |
| `GET /admin/products/add` | Add product form |
| `POST /admin/products/save` | Save product |
| `GET /admin/products/edit/{id}` | Edit product form |
| `POST /admin/products/delete/{id}` | Delete product |

---

## 🏗️ Architecture

ShopNest follows a clean layered architecture:

```
Browser Request
      ↓
Controller Layer     — handles HTTP requests and responses
      ↓
Service Layer        — business logic (Interface + Implementation pattern)
      ↓
Repository Layer     — data access (Spring Data JPA)
      ↓
Database             — MySQL
```

### Key architectural decisions

- **Interface + Implementation pattern** in service layer for loose coupling and testability
- **Constructor injection** throughout — no `@Autowired` field injection
- **`@Transactional`** on all write operations — full rollback on failure
- **Thymeleaf fragments** for shared navbar — written once, reused everywhere
- **`sec:authorize`** for role-based UI element visibility
- **`orphanRemoval = true`** on Cart → CartItems for reliable cart clearing
- **`@ControllerAdvice`** for centralized exception handling
- **`@Transient`** on `getSubtotal()` — calculated in Java, not stored in DB

---

## ☁️ Deployment

ShopNest is deployed on **Koyeb** with **Aiven MySQL** as the cloud database.

### Environment variables required

```
DB_URL        = jdbc:mysql://your-aiven-host:port/defaultdb?ssl-mode=REQUIRED
DB_USERNAME   = your-db-username
DB_PASSWORD   = your-db-password
PORT          = 8080
```

### Deploy your own instance

1. Fork this repository
2. Create a free MySQL database on [Aiven](https://aiven.io)
3. Create a free app on [Koyeb](https://koyeb.com)
4. Connect your forked GitHub repository
5. Set the environment variables above
6. Deploy — Koyeb auto-detects Spring Boot and builds automatically

---

## 🔮 Future Improvements

- Razorpay payment gateway integration (awaiting business verification)
- Order history page for users
- User profile and address management
- Product ratings and reviews
- Email notifications on order confirmation
- REST API layer for mobile app support
- Microservices architecture (Version 2)
- Redis caching for product catalog

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch — `git checkout -b feature/your-feature`
3. Commit your changes — `git commit -m "Add your feature"`
4. Push to the branch — `git push origin feature/your-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Sai Kumar Vallipanni**  
Java Full Stack Developer



> Built with ❤️ using Java and Spring Boot | Documented publicly on LinkedIn day by day
