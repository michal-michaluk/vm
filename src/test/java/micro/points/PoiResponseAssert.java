package micro.points;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

@AllArgsConstructor
public class PoiResponseAssert {
    private final ResponseEntity<String> response;

    public static PoiResponseAssert assertThat(ResponseEntity<String> response) {
        return new PoiResponseAssert(response);
    }

    public PoiResponseAssert isOK() {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return this;
    }

    public PoiResponseAssert isCreated() {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return this;
    }

    public PoiResponseAssert isBadRequest() {
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        return this;
    }

    public PoiResponseAssert hasPointAsBody(Consumer<PointJsonAssert> point) {
        var found = JsonPath.read(response.getBody(), "$");
        point.accept(PointJsonAssert.assertThat(found));
        return this;
    }

    public PoiResponseAssert hasPointWithNameInBody(String name, Consumer<PointJsonAssert> point) {
        JSONArray found = JsonPath.read(response.getBody(), "$.content[?(@.name=='" + name + "')]");
        Assertions.assertThat(found).as("found points for name %s", name).hasSize(1);
        point.accept(PointJsonAssert.assertThat(found.get(0)));
        return this;
    }

    public PoiResponseAssert hasNoPointWithNameInBody(String name) {
        JSONArray found = JsonPath.read(response.getBody(), "$.content[?(@.name=='" + name + "')]");
        Assertions.assertThat(found).as("found points for name %s", name).hasSize(0);
        return this;
    }
}
