package external.dependency;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Connection {
    private String hostname;
    private String username;
    private String password;
}
