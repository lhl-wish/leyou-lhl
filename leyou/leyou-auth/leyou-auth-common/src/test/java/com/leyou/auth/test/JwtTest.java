package com.leyou.auth.test;

import com.leyou.common.JwtUtils;
import com.leyou.common.RsaUtils;
import com.leyou.pojo.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "E:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "E:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "lhl123");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU5NzM5OTA5NX0.s46-peRxbBEfhrOvWKRNWqZ0GuoxdyXy3XA_DXtQGCTRBEe0FmQAyhUACdThjzE4LHlvlSXA8tlw50gd-Km8iAQ1G0vbWSg-2VVSwDfnc5L1h649OAxXDQVJvXdvCIzRa4FO5yPUgc_WBX-CYJHWhkcAtW7CNJrv4nmY0I91HMk";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}