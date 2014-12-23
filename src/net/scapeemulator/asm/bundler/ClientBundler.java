package net.scapeemulator.asm.bundler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

import net.scapeemulator.asm.bundler.trans.CachePathTransformer;
import net.scapeemulator.asm.bundler.trans.FileTransformer;
import net.scapeemulator.asm.bundler.trans.HostnameTransformer;
import net.scapeemulator.asm.bundler.trans.RsaTransformer;
import net.scapeemulator.asm.bundler.trans.Transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClientBundler {

	private static final Logger logger = LoggerFactory.getLogger(ClientBundler.class);

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		ClientBundler bundler = new ClientBundler();
		bundler.bundle();
	}

	public void bundle() throws IOException, NoSuchAlgorithmException {
		/* unpack source files */
		logger.info("Starting client bundler...");
		Application client = Application.unpackJar(new File("data/asm/runescape.jar"));
		Application glClient = Application.unpack200(new File("data/asm/runescape_gl.pack200"));
		Application loader = Application.unpackJar(new File("data/asm/loader.jar"));
		Application glLoader = Application.unpackJar(new File("data/asm/loader_gl.jar"));
		Application jogl = Application.unpackJar(new File("data/asm/jogl.jar"));

		/* update client RSA keys */
		logger.info("Transforming RSA keys...");
		Transformer transformer = new RsaTransformer();
		client.transform(transformer);
		glClient.transform(transformer);

		/* remove hostname protection */
		logger.info("Transforming hostname protection...");
		transformer = new HostnameTransformer();
		client.transform(transformer);
		glClient.transform(transformer);

		/* write everything but the loaders back out */
		logger.info("Bundling clients and libraries...");
		File clientOut = new File("data/game/jaggrab/runescape.pack200");
		File glClientOut = new File("data/game/jaggrab/runescape_gl.pack200");
		File joglOut = new File("data/game/jaggrab/jogl.pack200");
		client.packJar(new File("data/game/jaggrab/runescape.jar")); // unsigned client output
		client.pack200(clientOut);
		glClient.pack200(glClientOut);
		jogl.pack200(joglOut);

		/* compute SHA1 checksums */
		logger.info("Computing SHA1 checksums and file sizes...");
		Resource[] resources = new Resource[] {
			Resource.create(new File("data/asm/game_unpacker.dat")),
			Resource.create(clientOut),
			Resource.NULL
		};
		Resource[] glResources = new Resource[] {
			Resource.create(new File("data/asm/game_unpacker.dat")),
			Resource.create(glClientOut),
			Resource.NULL,
			Resource.create(joglOut),
			Resource.NULL,
			Resource.create(new File("data/asm/windows-x86/jogl.dll")),
			Resource.create(new File("data/asm/windows-x86/jogl_awt.dll")),
			Resource.create(new File("data/asm/windows-amd64/jogl.dll")),
			Resource.create(new File("data/asm/windows-amd64/jogl_awt.dll")),
			Resource.create(new File("data/asm/linux-x86/libjogl.so")),
			Resource.create(new File("data/asm/linux-x86/libjogl_awt.so")),
			Resource.create(new File("data/asm/linux-x86/libgluegen-rt.so")),
			Resource.create(new File("data/asm/linux-amd64/libjogl.so")),
			Resource.create(new File("data/asm/linux-amd64/libjogl_awt.so")),
			Resource.create(new File("data/asm/linux-amd64/libgluegen-rt.so")),
			Resource.create(new File("data/asm/mac-ppc/libjogl.jnilib")),
			Resource.create(new File("data/asm/mac-ppc/libjogl_awt.jnilib")),
			Resource.create(new File("data/asm/mac-universal/libjogl.jnilib")),
			Resource.create(new File("data/asm/mac-universal/libjogl_awt.jnilib")),
			Resource.create(new File("data/asm/solaris-amd64/libjogl.so")),
			Resource.create(new File("data/asm/solaris-amd64/libjogl_awt.so")),
			Resource.create(new File("data/asm/solaris-amd64/libgluegen-rt.so")),
			Resource.create(new File("data/asm/solaris-sparcv9/libjogl.so")),
			Resource.create(new File("data/asm/solaris-sparcv9/libjogl_awt.so")),
			Resource.create(new File("data/asm/solaris-sparcv9/libgluegen-rt.so")),
			Resource.create(new File("data/asm/solaris-sparc/libjogl.so")),
			Resource.create(new File("data/asm/solaris-sparc/libjogl_awt.so")),
			Resource.create(new File("data/asm/solaris-sparc/libgluegen-rt.so")),
			Resource.create(new File("data/asm/solaris-x86/libjogl.so")),
			Resource.create(new File("data/asm/solaris-x86/libjogl_awt.so")),
			Resource.create(new File("data/asm/solaris-x86/libgluegen-rt.so"))
		};

		/* update SHA1 checksums in the loader */
		logger.info("Transforming SHA1 checksums and file sizes...");
		transformer = new FileTransformer(resources);
		loader.transform(transformer);
		transformer = new FileTransformer(glResources);
		glLoader.transform(transformer);

		/* transform cache path */
		logger.info("Transforming cache path...");
		transformer = new CachePathTransformer();
		loader.transform(transformer);
		glLoader.transform(transformer);

		/* write the loaders back out */
		logger.info("Bundling loaders...");
		loader.packJar(new File("data/game/jaggrab/loader.jar"));
		glLoader.packJar(new File("data/game/jaggrab/loader_gl.jar"));

		/* copy natives/unpacker */
		logger.info("Copying native libraries and game unpacker...");
		copy("data/asm/game_unpacker.dat", "data/game/jaggrab/unpackclass.pack");
		copy("data/asm/windows-x86/jogl.dll", "data/game/jaggrab/jogl_0_0.lib");
		copy("data/asm/windows-x86/jogl_awt.dll", "data/game/jaggrab/jogl_0_1.lib");
		copy("data/asm/windows-amd64/jogl.dll", "data/game/jaggrab/jogl_1_0.lib");
		copy("data/asm/windows-amd64/jogl_awt.dll", "data/game/jaggrab/jogl_1_1.lib");
		copy("data/asm/linux-x86/libjogl.so", "data/game/jaggrab/jogl_2_0.lib");
		copy("data/asm/linux-x86/libjogl_awt.so", "data/game/jaggrab/jogl_2_1.lib");
		copy("data/asm/linux-x86/libgluegen-rt.so", "data/game/jaggrab/jogl_2_2.lib");
		copy("data/asm/linux-amd64/libjogl.so", "data/game/jaggrab/jogl_3_0.lib");
		copy("data/asm/linux-amd64/libjogl_awt.so", "data/game/jaggrab/jogl_3_1.lib");
		copy("data/asm/linux-amd64/libgluegen-rt.so", "data/game/jaggrab/jogl_3_2.lib");
		copy("data/asm/mac-ppc/libjogl.jnilib", "data/game/jaggrab/jogl_4_0.lib");
		copy("data/asm/mac-ppc/libjogl_awt.jnilib", "data/game/jaggrab/jogl_4_1.lib");
		copy("data/asm/mac-universal/libjogl.jnilib", "data/game/jaggrab/jogl_5_0.lib");
		copy("data/asm/mac-universal/libjogl_awt.jnilib", "data/game/jaggrab/jogl_5_1.lib");
		copy("data/asm/solaris-amd64/libjogl.so", "data/game/jaggrab/jogl_6_0.lib");
		copy("data/asm/solaris-amd64/libjogl_awt.so", "data/game/jaggrab/jogl_6_1.lib");
		copy("data/asm/solaris-amd64/libgluegen-rt.so", "data/game/jaggrab/jogl_6_2.lib");
		copy("data/asm/solaris-sparcv9/libjogl.so", "data/game/jaggrab/jogl_7_0.lib");
		copy("data/asm/solaris-sparcv9/libjogl_awt.so", "data/game/jaggrab/jogl_7_1.lib");
		copy("data/asm/solaris-sparcv9/libgluegen-rt.so", "data/game/jaggrab/jogl_7_2.lib");
		copy("data/asm/solaris-sparc/libjogl.so", "data/game/jaggrab/jogl_8_0.lib");
		copy("data/asm/solaris-sparc/libjogl_awt.so", "data/game/jaggrab/jogl_8_1.lib");
		copy("data/asm/solaris-sparc/libgluegen-rt.so", "data/game/jaggrab/jogl_8_2.lib");
		copy("data/asm/solaris-x86/libjogl.so", "data/game/jaggrab/jogl_9_0.lib");
		copy("data/asm/solaris-x86/libjogl_awt.so", "data/game/jaggrab/jogl_9_1.lib");
		copy("data/asm/solaris-x86/libgluegen-rt.so", "data/game/jaggrab/jogl_9_2.lib");

		/* sign loaders */
		logger.info("Signing loaders...");
		Runtime.getRuntime().exec("jarsigner -keystore data/asm/scapeemu.keystore -storepass scapeemu data/game/jaggrab/loader.jar scapeemu");
		Runtime.getRuntime().exec("jarsigner -keystore data/asm/scapeemu.keystore -storepass scapeemu data/game/jaggrab/loader_gl.jar scapeemu");

		/* all done! */
		logger.info("Client bundler completed successfully.");
	}

	private void copy(String srcPath, String destPath) throws IOException {
		Path src = new File(srcPath).toPath();
		Path dest = new File(destPath).toPath();
		Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
	}

}
