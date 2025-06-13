package com.test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HashMapInternal {
    public static void main(String[] args) {
//        HashMap<String, Integer> map = new HashMap<>();
//        map.put("A", 100);
//        map.put("A", 200);
//        map.put("B", 200);
//
//        System.out.println(map.get("A")); // Fetches 100
//        HashMap<Student, String> map = new HashMap<>();
//        Student s1 = new Student(1, "John");
//        Student s2 = new Student(1, "John");
//
//        map.put(s1, "Math");
//        
//        System.out.println(map.get(s2)); // Fetches "Math" because equals() and hashCode() are overridden
    	//------------------
    	
//    	Student s1 = new Student(1);
//    	Student s2 = new Student(1);
//
//    	System.out.println(s1.equals(s2));  // true
//
//    	Set<Student> set = new HashSet<>();
//    	set.add(s1);
//    	set.add(s2);
//    	System.out.println(set.size());     // 1 (no duplicates!)
    	
    	
    	//------------------

    	Student s1 = new Student(1);
    	Student s2 = new Student(1);

    	System.out.println(s1.equals(s2));  // false

    }
}
class Student {
    int id;
    Student(int id) { this.id = id; }
}

//class Student {
//    int id;
//
//    Student(int id) {
//        this.id = id;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        Student s = (Student) obj;
//        return this.id == s.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Integer.hashCode(id);
//    }
//}
