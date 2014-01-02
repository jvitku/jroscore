package ctu.nengoros.util;

import java.io.File;
import java.text.NumberFormat;

public class SystemInfo {

	public static void infoMb() {
		/* Total number of processors or cores available to the JVM */
		System.out.println("Available processors (cores): " + 
				Runtime.getRuntime().availableProcessors());

		long g = 10000000L;
		
		/* Total amount of free memory available to the JVM */
		System.out.println("Free memory (Mbytes): " + 
				(Runtime.getRuntime().freeMemory())/g);

		/* This will return Long.MAX_VALUE if there is no preset limit */
		long maxMemory = Runtime.getRuntime().maxMemory();
		/* Maximum amount of memory the JVM will attempt to use */
		System.out.println("Maximum memory (bytes): " + 
				(maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

		/* Total memory currently in use by the JVM */
		System.out.println("Total memory (Mbytes): " + 
				Runtime.getRuntime().totalMemory()/g);

		/* Get a list of all filesystem roots on this system */
		File[] roots = File.listRoots();

		/* For each filesystem root, print some info */
		for (File root : roots) {
			System.out.println("File system root: " + root.getAbsolutePath());
			System.out.println("Total space (Mbytes): " + root.getTotalSpace()/g);
			System.out.println("Free space (Mbytes): " + root.getFreeSpace()/g);
			System.out.println("Usable space (Mbytes): " + root.getUsableSpace()/g);
		}
	}

	public static void info() {
		/* Total number of processors or cores available to the JVM */
		System.out.println("Available processors (cores): " + 
				Runtime.getRuntime().availableProcessors());

		/* Total amount of free memory available to the JVM */
		System.out.println("Free memory (bytes): " + 
				Runtime.getRuntime().freeMemory());

		/* This will return Long.MAX_VALUE if there is no preset limit */
		long maxMemory = Runtime.getRuntime().maxMemory();
		/* Maximum amount of memory the JVM will attempt to use */
		System.out.println("Maximum memory (bytes): " + 
				(maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

		/* Total memory currently in use by the JVM */
		System.out.println("Total memory (bytes): " + 
				Runtime.getRuntime().totalMemory());

		/* Get a list of all filesystem roots on this system */
		File[] roots = File.listRoots();

		/* For each filesystem root, print some info */
		for (File root : roots) {
			System.out.println("File system root: " + root.getAbsolutePath());
			System.out.println("Total space (bytes): " + root.getTotalSpace());
			System.out.println("Free space (bytes): " + root.getFreeSpace());
			System.out.println("Usable space (bytes): " + root.getUsableSpace());
		}
	}

	private Runtime runtime = Runtime.getRuntime();

	public String Info() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.OsInfo());
		sb.append(this.MemInfo());
		sb.append(this.DiskInfo());
		return sb.toString();
	}

	public String OSname() {
		return System.getProperty("os.name");
	}

	public String OSversion() {
		return System.getProperty("os.version");
	}

	public String OsArch() {
		return System.getProperty("os.arch");
	}

	public long totalMem() {
		return Runtime.getRuntime().totalMemory();
	}

	public long usedMem() {
		return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	public String MemInfo() {
		NumberFormat format = NumberFormat.getInstance();
		StringBuilder sb = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		sb.append("Free memory: ");
		sb.append(format.format(freeMemory / 1024));
		sb.append("<br/>");
		sb.append("Allocated memory: ");
		sb.append(format.format(allocatedMemory / 1024));
		sb.append("<br/>");
		sb.append("Max memory: ");
		sb.append(format.format(maxMemory / 1024));
		sb.append("<br/>");
		sb.append("Total free memory: ");
		sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
		sb.append("<br/>");
		return sb.toString();

	}

	public String OsInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("OS: ");
		sb.append(this.OSname());
		sb.append("<br/>");
		sb.append("Version: ");
		sb.append(this.OSversion());
		sb.append("<br/>");
		sb.append(": ");
		sb.append(this.OsArch());
		sb.append("<br/>");
		sb.append("Available processors (cores): ");
		sb.append(runtime.availableProcessors());
		sb.append("<br/>");
		return sb.toString();
	}

	public String DiskInfo() {
		/* Get a list of all filesystem roots on this system */
		File[] roots = File.listRoots();
		StringBuilder sb = new StringBuilder();

		/* For each filesystem root, print some info */
		for (File root : roots) {
			sb.append("File system root: ");
			sb.append(root.getAbsolutePath());
			sb.append("<br/>");
			sb.append("Total space (bytes): ");
			sb.append(root.getTotalSpace());
			sb.append("<br/>");
			sb.append("Free space (bytes): ");
			sb.append(root.getFreeSpace());
			sb.append("<br/>");
			sb.append("Usable space (bytes): ");
			sb.append(root.getUsableSpace());
			sb.append("<br/>");
		}
		return sb.toString();
	}
}