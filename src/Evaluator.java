import java.io.File;
import java.util.List;

abstract class Evaluator {
    protected void traverseFolder(File root, List<File> files) {
        File[] list = root.listFiles();
        if (list == null)
            return;

        for (File f : list) {
            if (f.isDirectory()) {
                traverseFolder(f, files);
            } else if (f.getName().endsWith(".java")) {
                files.add(f);
            }
        }
    }
}
