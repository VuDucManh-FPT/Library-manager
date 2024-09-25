package com.example.LibraryManagement.Security;

public class JwtConstants {
    public static final String SECRET_KEY = "zzKuADl6R1vgbGYESb7meWuGvONSRgJ0Ps3rJZkP1HiYCpnsqu3BcZY2usMPxI8A";
    public static final long EXPIRE_DATE = 24 * 60 * 60 * 1000;
    public static final String JWT_COOKIE_NAME = "token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final long REFRESH_EXPIRE_DATE = 30 * 24 * 60 * 60 * 1000;
    public static final String SIGNATURE_ALGORITHM = "HS256";
    public static final long EXPIRATION_TIME = 5 * 60 * 1000;
}
