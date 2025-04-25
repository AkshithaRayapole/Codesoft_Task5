import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public int getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public String getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return String.format("Roll No: %d, Name: %s, Grade: %s", rollNumber, name, grade);
    }
}

class StudentManagementSystem {
    private final List<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.dat";

    public void addStudent(Student student) {
        students.add(student);
        saveToFile();
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
        saveToFile();
    }

    public Student searchStudent(int rollNumber) {
        return students.stream().filter(s -> s.getRollNumber() == rollNumber).findFirst().orElse(null);
    }

    public void displayAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
        } else {
            students.forEach(System.out::println);
        }
    }

    public void editStudent(int rollNumber, String newName, String newGrade) {
        Student student = searchStudent(rollNumber);
        if (student != null) {
            student.setName(newName);
            student.setGrade(newGrade);
            saveToFile();
        } else {
            System.out.println("Student not found!");
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(new ArrayList<>(students));
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                students.clear();
                students.addAll((List<Student>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found.");
        }
    }
}

public class Task5 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.loadFromFile();

            while (true) {
                System.out.println("\n--- Student Management System ---");
                System.out.println("1. Add Student\n2. Remove Student\n3. Search Student\n4. Display All Students\n5. Edit Student\n6. Exit");
                System.out.print("Enter choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter Roll Number: ");
                        while (!scanner.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid Roll Number: ");
                            scanner.next();
                        }
                        int rollNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Grade: ");
                        String grade = scanner.nextLine().trim();
                        sms.addStudent(new Student(name, rollNumber, grade));
                    }
                    case 2 -> {
                        System.out.print("Enter Roll Number to Remove: ");
                        while (!scanner.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid Roll Number: ");
                            scanner.next();
                        }
                        sms.removeStudent(scanner.nextInt());
                    }
                    case 3 -> {
                        System.out.print("Enter Roll Number to Search: ");
                        while (!scanner.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid Roll Number: ");
                            scanner.next();
                        }
                        Student found = sms.searchStudent(scanner.nextInt());
                        System.out.println(found != null ? found : "Student not found.");
                    }
                    case 4 -> sms.displayAllStudents();
                    case 5 -> {
                        System.out.print("Enter Roll Number to Edit: ");
                        while (!scanner.hasNextInt()) {
                            System.out.print("Invalid input. Enter a valid Roll Number: ");
                            scanner.next();
                        }
                        int roll = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter New Name: ");
                        String newName = scanner.nextLine().trim();
                        System.out.print("Enter New Grade: ");
                        String newGrade = scanner.nextLine().trim();
                        sms.editStudent(roll, newName, newGrade);
                    }
                    case 6 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}