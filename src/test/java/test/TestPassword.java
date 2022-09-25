package test;

import cn.hutool.core.codec.Base58;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.SM3;
import com.gitee.ixtf.Jcodec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TestPassword {
    public static void main(String[] args) {
        System.out.println(IdUtil.objectId());
        System.out.println(IdUtil.fastSimpleUUID());
        System.out.println(IdUtil.fastSimpleUUID().length());

        final var file = FileUtil.file("/Users/ixtf/com.gitee.ixtf/core/build.gradle");
        final var bytes = DigestUtil.sha256(file);
        System.out.println(DigestUtil.sha256Hex(file));
        System.out.println(Base58.encode(bytes));

        System.out.println(DigestUtil.bcrypt("1234567890"));
        System.out.println(Jcodec.password("1234567890"));
        System.out.println(DigestUtil.digester("sm3").digestHex(file));
        System.out.println(SM3.create().digestHex(file));

        final var wrap = ByteBuffer.wrap(new byte[16]);
        final var uuid = UUID.fastUUID();
        wrap.putLong(uuid.getMostSignificantBits());
        wrap.putLong(uuid.getLeastSignificantBits());
        System.out.println(uuid);
        final var encode = Base58.encode(wrap.array());
        System.out.println(encode);
        final var decode = Base58.decode(encode);
        final var wrap1 = ByteBuffer.wrap(decode);
        final var l1 = wrap1.getLong(); final var l2 = wrap1.getLong();
        System.out.println(new UUID(l1, l2));

        System.out.println(Base58.encode(IdUtil.fastSimpleUUID().getBytes(StandardCharsets.UTF_8)));

        var s = IdUtil.fastSimpleUUID();
        for (int i = 0; i < 10; i++) {
            s = Base58.encode(s.getBytes(StandardCharsets.UTF_8));
            System.out.println(s);
        }
    }
}
