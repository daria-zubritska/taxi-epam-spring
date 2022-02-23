package com.epam.project.spring.taxispring.model.service;

public class CarService {

    public static int getTypeId(String type){

        switch(type.toLowerCase()){
            case("cheap") :
            {
                return 1;
            }
            case("comfort") :
            {
                return 2;
            }
            case("business") :
            {
                return 3;
            }
        }

        return 0;
    }
}
