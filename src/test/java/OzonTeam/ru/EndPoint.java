package OzonTeam.ru;

public interface EndPoint {
    String UrlToken = "oauth/v1/auth/token";
    String Auth = "user/v5/";
    String AddProductToCart = "cart/v6/items";
    String WhatProductToCar ="cart/v6/summary";
    String DeleteProductToCart = "cart/v6/items";
    String StatusAuth = "user/v5/account/phone/79269800344/status";
    String GetIdUser = "user/v5/public";
    String SearchEmail = "user/v5/reg/check";
    String CategoryProduct = "cms-api.bx/menu/category/v1";
    String Ipaddres = "location/v2/current";



    String Noutbook = "C:/ProjectIDEA/Ozon/src/test/resources/Noutbook.json";
    String bodyozon = "C:/ProjectIDEA/Ozon/src/test/resources/BodyOzon.json";
}
