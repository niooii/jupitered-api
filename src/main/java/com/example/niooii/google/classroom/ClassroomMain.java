package com.example.niooii.google.classroom;


class ClassroomMain {

    public static void main(String[] args) throws InterruptedException {

        Classroom client = new Classroom("", "");

        String page = client.toString();

        client.login();
    } //finish later

}