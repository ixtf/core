package test;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.gitee.ixtf.J;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJwt {
    @Test
    void test1(){
        final var key = "1234567890".getBytes();
        final var token1 = JWTUtil.createToken(Map.of("path", "path"), key);
        final var token2 = JWTUtil.createToken(Map.of("path", "path"), key);
        assertEquals(token1,token2);
    }
    @Test
    void test_ps256(){
        final var id = "ES256";
        final var signer = JWTSignerUtil.createSigner(id, KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id)));
        final var sign1 = JWT.create().setSigner(signer).setPayload("sub", "1234567890")
                .setPayload("name", "looly")
                .setPayload("admin", true)
                .sign();
        final var sign2 = JWT.create().setSigner(signer).setPayload("sub", "1234567890")
                .setPayload("name", "looly")
                .setPayload("admin", true)
                .sign();
        System.out.println(DigestUtil.digester("sm3").digestHex("123"));
        System.out.println(sign1);
        System.out.println(sign2);
    }
}
