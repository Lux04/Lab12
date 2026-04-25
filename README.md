# Lab 12 — Designing for Robustness

**Хичээл:** F.CSM311 Программ хангамжийн бүтээлт
**Лекц:** 12 — Бат бөх дизайн ба алдаа зохицуулалт
**Хугацаа:** 90 минут

## Шаардлагатай хэрэгсэл

- Java 17 эсвэл түүнээс дээш
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## Төслийг ажиллуулах

```bash
# Бүх тестийг ажиллуулах
mvn test

# Нэг тасалбараас дагаж тестлэх
mvn test -Dtest=BankAccountTest
mvn test -Dtest=UserRepositoryTest
mvn test -Dtest=FileConfigLoaderTest
mvn test -Dtest=RetryExecutorTest
mvn test -Dtest=SimpleCircuitBreakerTest
```

## Даалгавруудын бүтэц

```
src/main/java/mn/csm311/lab12/
├── task1/   — Fail-fast ба Preconditions
├── task2/   — Optional ба Null Object Pattern
├── task3/   — Exception handling (try-with-resources, translation)
├── task4/   — Retry with exponential backoff + jitter
└── task5/   — Circuit Breaker
```

Даалгавар тус бүрийн тайлбар ба шалгуурыг Lab12.docx баримтаас уншина уу.

## Зөвлөмж

- Даалгаврыг дарааллаар нь хийнэ.
- Тест бүрийг амжилттай гүйцэж дуустал `// TODO` хэсгийг бөглөнө.
- Хугацаа хэмнэхийн тулд IDE-ийн auto-import, JUnit plugin-ыг ашиглана уу.
