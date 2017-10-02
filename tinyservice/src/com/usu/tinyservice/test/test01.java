package com.usu.tinyservice.test;

import com.usu.tinyservice.annotations.TinyService;

public class test01 extends Thread {
	public void run() {
		System.out.println("hello!");
	}
	
	@TinyService
	public class User {
		public String name;
		public String city;
	}
	
	public static void main(String args[]) {
		new test01().start();
	}
}
