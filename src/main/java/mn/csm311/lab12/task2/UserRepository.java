package mn.csm311.lab12.task2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * User repository using Optional to represent missing values.
 */
public class UserRepository {

    private final Map<String, User> byEmail = new HashMap<>();

    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        byEmail.put(user.email(), user);
    }

    /**
     * Email-ээр хэрэглэгчийг олох.
     */
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email));
    }
}
