package academy.devdojo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnimePostResponse {
    private Long id;
    private String name;
}
