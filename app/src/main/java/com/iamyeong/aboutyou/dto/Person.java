package com.iamyeong.aboutyou.dto;

public class Person implements Comparable<Person> {



    //다중 조건 필요 (각종 필드 순으로 정렬시키기)



    @Override
    public int compareTo(Person o) {

        /*
        if () {

            return 1;
        } else if () {

            return -1;
        } else {
            return 0;
        }

         */

        return -1;

    }


}
