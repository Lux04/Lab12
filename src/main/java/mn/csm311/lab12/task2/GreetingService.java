package mn.csm311.lab12.task2;

/**
 * Энэ класс нь Optional API-г зохистой ашиглаж байгааг харуулна.
 */
public class GreetingService {

    private final UserRepository repository;

    public GreetingService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Хэрэглэгч email-ээр олдсон бол нэрээр, олдоогүй бол зочин гэж мэндчилнэ.
     */
    public String greet(String email) {
        return repository.findByEmail(email)
                .map(user -> "Сайн байна уу, " + user.name() + "!")
                .orElse("Сайн байна уу, Зочин!");
    }
}
