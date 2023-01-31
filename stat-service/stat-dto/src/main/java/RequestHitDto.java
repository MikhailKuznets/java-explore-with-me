import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestHitDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
