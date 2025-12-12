package external.dependecy;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class Connection {
    private String host;
    private String name;
    private String password;
}
