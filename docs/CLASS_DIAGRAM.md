# Class Diagram

```text
                         User <<abstract>>
        ------------------------------------------------
        - userId: int
        - name: String
        - email: String
        - password: String
        - role: String
        - active: boolean
        + canAccessAdminPanel(): boolean
                         /              \
                        /                \
                Customer                  Admin
        + canAccessAdminPanel()    + canAccessAdminPanel()

User 1 ---- 0..1 Account
User 1 ---- 0..* Expense
User 1 ---- 0..* Budget
User 1 ---- 0..* SavingsGoal
Account 1 ---- 0..* Transaction

CrudDAO<T> <<interface>>
        + create(T): boolean
        + update(T): boolean
        + delete(int): boolean
        + findAllByUserId(int): List<T>

ExpenseDAO implements CrudDAO<Expense>
BudgetDAO implements CrudDAO<Budget>
SavingsGoalDAO implements CrudDAO<SavingsGoal>
```

## Layer Flow

```text
JavaFX UI Screens
      |
      v
Service Layer
      |
      v
DAO Layer
      |
      v
DBConnection
      |
      v
MySQL Database
```

## OOP Usage

- Encapsulation: private fields with getters/setters in model classes.
- Inheritance: `Customer` and `Admin` extend `User`.
- Abstraction: `User` is an abstract class.
- Polymorphism: `canAccessAdminPanel()` behaves differently for admin and customer.
- Interfaces: `CrudDAO<T>` defines reusable CRUD behavior.
