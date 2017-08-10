package com.jiafancreatezipfile;

import java.io.File;
import java.util.ArrayList;

import javax.xml.crypto.Data;

public class ceshi1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			//企业
		A a1 = new A();  
        A a2 = new B();  
        B b = new B();  
        C c = new C();   
        D d = new D(); 
		
       /* System.out.println(a1.show(b));   
        System.out.println(a1.show(c));     
        System.out.println(a1.show(d)); */  
        System.out.println(a2.show(b)); 
        System.out.println(a2.show(c));    
        System.out.println(a2.show(d));    
        System.out.println(b.show(b));     
        System.out.println(b.show(c));     
        System.out.println(b.show(d));       
	}

}
