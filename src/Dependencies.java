import java.util.ArrayList;
import java.util.List;

public class Dependencies {

    private final String baseClass;
    private final List<String> dependencies;

    public Dependencies(String baseClass) {
        this.baseClass = baseClass;
        dependencies = new ArrayList<String>();
    }

    public void addDependency(String depend) {
        dependencies.add(depend);
    }

    public String getName() {
        return baseClass;
    }

    public List<String> getDependencies() {
        return dependencies;
    }
}
