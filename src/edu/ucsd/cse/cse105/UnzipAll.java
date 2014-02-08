package edu.ucsd.cse.cse105;
import java.io.File;

public class UnzipAll {
	public static void main(String[] args) {
		File dir = new File(".");
		String[] list = dir.list();
		for(int i=0; i<list.length; i++) {
			if (list[i].matches(".+\\.hw\\d")){
				String account = list[i].substring(0,list[i].lastIndexOf('.'));
				try {
				Runtime.getRuntime().exec("mkdir " + account);
				Runtime.getRuntime().exec("unzip " + list[i] + " -d " + account);
				} catch (Exception e) {
					System.out.println("didn't work for " + account);
				}
			}
		}
	}
}
