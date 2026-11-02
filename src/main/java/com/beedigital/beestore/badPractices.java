package com.beedigital.beestore;

public class badPractices {

    // This is a variable
    private int x = 10;

    // This is another variable
    private int y = 20;

    // Calculate total price
    public void calculatePrice1() {
        int price = 100;
        int quantity = 5;
        int total = price * quantity;
        System.out.println(total);
    }

    // Calculate total price again
    public void calculatePrice2() {
        int price = 100;
        int quantity = 5;
        int total = price * quantity;
        System.out.println(total);
    }

    // Unused method
    public void unusedMethod() {
        int number = 50;
    }

    // Bad naming
    public void a() {
        int abc = 10;
        int def = 20;
        System.out.println(abc + def);
    }

    // Too many nested conditions
    public void checkNumber() {
        int number = 100;

        if (number > 0) {
            if (number > 10) {
                if (number > 50) {
                    if (number > 90) {
                        System.out.println("Very large number");
                    }
                }
            }
        }
    }

    // Empty method
    public void doNothing() {
    }

    // Duplicate code
    public void displayProduct1() {
        System.out.println("Laptop");
        System.out.println("Computer");
        System.out.println("1000");
    }

    public void displayProduct2() {
        System.out.println("Laptop");
        System.out.println("Computer");
        System.out.println("1000");
    }

    // Magic numbers
    public double calculateDiscount(double price) {
        return price * 0.15;
    }

    // Unnecessary condition
    public boolean isPositive(int number) {
        if (number > 0) {
            return true;
        } else {
            return false;
        }
    }
}