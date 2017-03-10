package jarspec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarCreator implements Runnable {
	private static String PROJECT_DIR = "D:\\Java\\workspaces\\intellij\\pis1lab";

	private static String DESTINATION_DIR = "D:\\Java\\workspaces\\JARs\\output.jar";

	public static void main(String args[]){
		new Thread(new JarCreator()).run();
	}

	public void run() {
		Manifest manifest = new Manifest();
		manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

		JarOutputStream target;
		try{
			target = new JarOutputStream(new FileOutputStream(DESTINATION_DIR), manifest);
			add(new File(PROJECT_DIR), target);
			target.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void add(File source, JarOutputStream target) {
		BufferedInputStream in = null;
		try {
			if (source.isDirectory()) {
				String name = source.getPath().replace("\\", "/");
				if (name.length() != 0) {

					if (!name.endsWith("/")) name += "/";

					JarEntry entry = new JarEntry(name);

					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.closeEntry();
				}
				for (File nestedFile : source.listFiles()) add(nestedFile, target);
                if (in != null) in.close();
				return;
			}

			JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
			entry.setTime(source.lastModified());
			target.putNextEntry(entry);
			in = new BufferedInputStream(new FileInputStream(source));

			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();

            if (in != null) in.close();
		} catch(NullPointerException e){
		    e.printStackTrace();
        } catch (IOException e){
		    e.printStackTrace();
        }
	}
}
